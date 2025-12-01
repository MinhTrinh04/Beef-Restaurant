package com.eshop.BasketService.Service.Impl;

import com.eshop.BasketService.DTO.CreateOrderFromBasketRequestDto;
import com.eshop.BasketService.DTO.CreatePaymentUrlRequestDto;
import com.eshop.BasketService.DTO.OrderItemRequestDto;
import com.eshop.BasketService.DTO.PaymentUrlResponseDto;
import com.eshop.BasketService.DTO.StockValidationItem;
import com.eshop.BasketService.Exception.BasketNotFoundException;
import com.eshop.BasketService.Exception.StockValidationException;
import com.eshop.BasketService.Model.Basket;
import com.eshop.BasketService.Model.BasketCheckout;
import com.eshop.BasketService.Repository.BasketRepository;
import com.eshop.BasketService.Service.IBasketService;
import com.eshop.BasketService.Service.client.MenuServiceClient;
import com.eshop.BasketService.Service.client.OrderingServiceClient;
import com.eshop.BasketService.Service.client.PaymentServiceClient;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class BasketService implements IBasketService {
    private final BasketRepository basketRepository;
    private final MenuServiceClient menuServiceClient;
    private final OrderingServiceClient orderingServiceClient;
    private final PaymentServiceClient paymentServiceClient;

    @Override
    public Basket getBasketById(String id) {
        Basket existingBasket = basketRepository.findById(id).orElseThrow(() -> new BasketNotFoundException("Basket", "buyerId", id));
        return existingBasket;
    }

    @Override
    @Transactional
    public boolean updateBasket(Basket basket) {
        Basket resBasket = basketRepository.save(basket);
        if (resBasket == null){
            log.error("Error updating/creating basket for buyerId: {}", basket.getBuyerId());
            return false;
        }
        log.info("Basket updated/created successfully for buyerId: {}", basket.getBuyerId());
        return true;

    }

    @Override
    @Transactional
    public boolean deleteBasket(String id) {
        basketRepository.deleteById(id);
        return true;
    }

    @Override
    public ResponseEntity<PaymentUrlResponseDto> checkoutV2(String buyerId, BasketCheckout basketCheckout, String requestId) {
        Basket basket = basketRepository.findById(buyerId)
                .orElseThrow(() -> new BasketNotFoundException("Basket", "buyerId" , buyerId));
        log.info("Basket founded with total items: {}.", basket.getItems().size());

        List<StockValidationItem> validationRequest = basket.getItems().stream()
                .map(item -> {
                    log.info("...Mapping: ProductId=[{}], Units=[{}]", item.getProductId(), item.getUnits());
                    return new StockValidationItem(item.getProductId(), item.getUnits());
                })
                .collect(Collectors.toList());

        try {
            menuServiceClient.validateStock(validationRequest);
            log.info("Stock validation passed for buyerId: {}", buyerId);
        } catch (FeignException.BadRequest e) {
            throw new StockValidationException("Stock validation failed: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Menu service is unavailable: " + e.getMessage());
        }

        BigDecimal totalAmount = basket.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getUnits())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CreateOrderFromBasketRequestDto createOrderRequest = new CreateOrderFromBasketRequestDto(
                buyerId,
                basketCheckout.getUserEmail(),
                basketCheckout.getCity(),
                basketCheckout.getStreet(),
                basketCheckout.getState(),
                basketCheckout.getCountry(),
                basket.getItems().stream()
                        .map(item -> {
                            OrderItemRequestDto dto = new OrderItemRequestDto();
                            dto.setProductId(item.getProductId());
                            dto.setProductName(item.getProductName());
                            dto.setUnitPrice(item.getUnitPrice());
                            dto.setUnits(item.getUnits());
                            dto.setPictureUrl(item.getPictureUrl());
                            return dto;
                        })
                        .collect(Collectors.toList()),
                totalAmount
        );

        Long orderId;
        try {
            ResponseEntity<Long> orderResponse = orderingServiceClient.createOrderFromBasket(createOrderRequest);
            if (orderResponse.getStatusCode().is2xxSuccessful() && orderResponse.getBody() != null) {
                orderId = orderResponse.getBody();
                log.info("✅ Order created successfully with ID: {} for buyerId: {}", orderId, buyerId);
            } else {
                throw new RuntimeException("Failed to create order: " + orderResponse.getStatusCode());
            }
        } catch (Exception e) {
            log.error("❌ Error creating order for buyerId: {}", buyerId, e);
            throw new RuntimeException("Failed to create order: " + e.getMessage());
        }

        CreatePaymentUrlRequestDto paymentRequest = new CreatePaymentUrlRequestDto(
                orderId,
                totalAmount,
                null,
                "vn"
        );

        ResponseEntity<PaymentUrlResponseDto> paymentResponse = paymentServiceClient.createPaymentUrl(paymentRequest);

        if (paymentResponse.getStatusCode().is2xxSuccessful() &&
                paymentResponse.getBody() != null &&
                "00".equals(paymentResponse.getBody().getCode())) {
            log.info("Payment URL created successfully for order: {}", orderId);
        } else {
            log.warn("Payment URL creation returned non-success status for order: {}", orderId);
        }

        return paymentResponse;
    }
}

package com.eshop.basketservice.Controller;

import com.eshop.basketservice.Constants.BasketConstants;
import com.eshop.basketservice.DTO.ResponseDto;
import com.eshop.basketservice.Integrationevents.Events.UserCheckoutAcceptedIntegrationEvent;
import com.eshop.basketservice.Model.Basket;
import com.eshop.basket.model.BasketCheckout;
import com.eshop.basketservice.Repository.BasketRepository;
import com.eshop.basketservice.Service.IBasketService;
import com.eshop.basketservice.Service.IIdentityService;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/api/v1/basket")
@RequiredArgsConstructor
public class BasketController {
    private final IBasketService basketService;
    private final BasketRepository basketRepository;
    private final IIdentityService identityService;
    private final IEventBus eventBus;

    @GetMapping
    public ResponseEntity<Basket> getBasketById() {
        String buyerId = identityService.getUserIdentity();
        Basket basket = basketService.getBasketById(buyerId);
        return ResponseEntity.status(HttpStatus.OK).body(basket);
    }

    @PostMapping
    public ResponseEntity<ResponseDto> updateBasket(@RequestBody Basket basket) {
        String buyerId = identityService.getUserIdentity();
        basket.setBuyerId(buyerId);
        boolean isSuccess = basketService.updateBasket(basket);
        if(isSuccess){
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(BasketConstants.STATUS_200, BasketConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(BasketConstants.STATUS_417, BasketConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping
    public ResponseEntity<ResponseDto> deleteBasket() {
        String buyerId = identityService.getUserIdentity();
        boolean isDeleted = basketService.deleteBasket(buyerId);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(BasketConstants.STATUS_200, BasketConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(BasketConstants.STATUS_417, BasketConstants.MESSAGE_417_DELETE));
        }
    }

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void checkout(@RequestBody BasketCheckout basketCheckout) {
        log.info("Jump here");
        var basket = basketRepository.findById(basketCheckout.getBuyer()).orElse(null);
        if (basket == null) {
            log.error("Basket died");
            return;
        }

        var orderTotal = basket.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        basketCheckout.setOrderTotal(orderTotal);

        var event = new UserCheckoutAcceptedIntegrationEvent(
                basketCheckout.getBuyer(),
                basketCheckout.getCardNumber(),
                basketCheckout.getCardHolderName(),
                basketCheckout.getCardExpiration(),
                basketCheckout.getCardSecurityNumber(),
                basketCheckout.getCardTypeId(),
                basket,
                orderTotal
        );

        // Lời gọi đúng là chỉ truyền vào một tham số event
        eventBus.publish(event);
        log.info("⏳ Publish UserCheckoutAcceptedIntegrationEvent");
    }
}
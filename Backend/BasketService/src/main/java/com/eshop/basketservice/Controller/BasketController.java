package com.eshop.basketservice.Controller;

import com.eshop.basketservice.Constants.BasketConstants;
import com.eshop.basketservice.DTO.ResponseDto;
import com.eshop.basketservice.Model.Basket;
import com.eshop.basketservice.Repository.IBasketRepository;
import com.eshop.basketservice.Service.IBasketService;
import com.eshop.basketservice.Service.IIdentityService;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import com.eshop.basketservice.DTO.BasketCheckout;
import com.eshop.basketservice.Integrationevents.Events.UserCheckoutAcceptedIntegrationEvent;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/basket")
@RequiredArgsConstructor
public class BasketController {

    private final IBasketRepository basketRepository;
    private final IBasketService basketService;
    private final IIdentityService identityService;
    private final IEventBus eventBus; // Tiêm IEventBus từ BuildingBlocks

    @GetMapping
    public ResponseEntity<Basket> getBasket() {
        String userId = identityService.getUserIdentity();
        log.info("Fetching basket for user {}", userId);

        // Trả về giỏ hàng mới nếu không tồn tại
        Basket basket = basketRepository.findById(userId)
                .orElse(new Basket(userId));

        return ResponseEntity.ok(basket);
    }

    @PostMapping
    public ResponseEntity<ResponseDto> updateBasket(@RequestBody Basket basket) {
        String userId = identityService.getUserIdentity();
        basket.setBuyerId(userId);

        boolean isSuccess = basketService.updateBasket(basket);
        if (isSuccess) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(BasketConstants.STATUS_200, BasketConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(BasketConstants.STATUS_417,BasketConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBasket() {
        String userId = identityService.getUserIdentity();
        log.info("Deleting basket for user {}", userId);

        basketRepository.deleteById(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/checkout")
    public ResponseEntity<Void> checkout(
            @RequestBody BasketCheckout checkout,
            @RequestHeader(name = "X-Request-Id", required = false) String requestId) {

        String userId = identityService.getUserIdentity();
        log.info("Checkout initiated for user {}", userId);

        Basket basket = basketRepository.findById(userId)
                .orElse(null);

        if (basket == null || basket.getItems() == null || basket.getItems().isEmpty()) {
            log.warn("User {} attempted checkout with empty or null basket", userId);
            return ResponseEntity.badRequest().build(); // Giống [BadRequest()] trong C#
        }

        // Xử lý Request ID
        UUID eventRequestId = (requestId != null) ? parseOrNewGuid(requestId) : UUID.randomUUID();

        // Tạo sự kiện (Giả định bạn đã có lớp UserCheckoutAcceptedIntegrationEvent)
        IntegrationEvent event = new UserCheckoutAcceptedIntegrationEvent(
                userId,
                checkout.getUserEmail(),
                checkout.getCity(),
                checkout.getStreet(),
                checkout.getState(),
                checkout.getCountry(),
                eventRequestId,
                basket
        );

        // Gửi sự kiện bằng IEventBus
        log.info("Publishing UserCheckoutAcceptedIntegrationEvent: {}", event.getId());
        eventBus.publish(event);

        // Trả về 202 Accepted
        return ResponseEntity.accepted().build();
    }

    private UUID parseOrNewGuid(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException ex) {
            return UUID.randomUUID();
        }
    }
}
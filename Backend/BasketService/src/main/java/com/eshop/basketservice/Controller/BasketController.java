package com.eshop.basketservice.Controller;

import com.eshop.basketservice.Integrationevents.Events.UserCheckoutAcceptedIntegrationEvent;
import com.eshop.basketservice.Model.Basket;
import com.eshop.basket.model.BasketCheckout;
import com.eshop.basketservice.Repository.BasketRepository;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketRepository basketRepository;
    private final IEventBus eventBus;

    @GetMapping("/{id}")
    public ResponseEntity<Basket> getBasketById(@PathVariable String id) {
        return basketRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(new Basket(id)));
    }

    @PostMapping
    public ResponseEntity<Basket> updateBasket(@RequestBody Basket basket) {
        return ResponseEntity.ok(basketRepository.save(basket));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteBasket(@PathVariable String id) {
        basketRepository.deleteById(id);
    }

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void checkout(@RequestBody BasketCheckout basketCheckout) {
        var basket = basketRepository.findById(basketCheckout.getBuyer()).orElse(null);
        if (basket == null) {
            // Có thể ném ra một ngoại lệ ở đây để báo lỗi rõ ràng hơn
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
    }
}
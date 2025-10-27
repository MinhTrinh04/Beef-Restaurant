package com.eshop.BasketService.Controller;

import com.eshop.BasketService.Constants.BasketConstants;
import com.eshop.BasketService.DTO.ResponseDto;
import com.eshop.BasketService.Model.Basket;
import com.eshop.BasketService.Model.BasketCheckout;
import com.eshop.BasketService.Repository.BasketRepository;
import com.eshop.BasketService.Service.IBasketService;
import com.eshop.BasketService.Service.IIdentityService;
import com.eshop.buildingblocks.EventBus.Abstractions.IEventBus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ResponseDto> checkout(@RequestBody BasketCheckout basketCheckout,@RequestHeader(value = "X-Request-Id", required = false) String requestId) {
        String buyerId = identityService.getUserIdentity();
        basketService.checkout(buyerId, basketCheckout, requestId);
        return  ResponseEntity.ok(new ResponseDto(BasketConstants.STATUS_200, BasketConstants.MESSAGE_200));
    }
}
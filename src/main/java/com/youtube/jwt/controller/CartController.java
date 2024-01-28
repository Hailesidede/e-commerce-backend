package com.youtube.jwt.controller;

import com.youtube.jwt.entity.Cart;
import com.youtube.jwt.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping({"/addToCart/{productId}"})
    @PreAuthorize("hasRole('User')")
    public Cart addToCart(@PathVariable (name ="productId") Integer productId){
        return cartService.addToCart(productId);

    }


    @GetMapping({"/getCartDetails"})
    @PreAuthorize("hasRole('User')")
    public List<Cart> getCartDetails(){
        return cartService.getCartDetails();
    }

    @PreAuthorize("hasRole('User')")
    @DeleteMapping({"/deleteCartItem/{cartId}"})
    public void deleteCartItem(@PathVariable("cartId") Integer cartId){
        cartService.deleteCartItem(cartId);

    }
}

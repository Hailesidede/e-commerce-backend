package com.youtube.jwt.controller;

import com.youtube.jwt.entity.OrderDetail;
import com.youtube.jwt.entity.OrderInput;
import com.youtube.jwt.entity.TransactionDetails;
import com.youtube.jwt.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping({"/placeOrder/{isSingleProductCheckout}"})
    @PreAuthorize("hasRole('User')")
    public void placeOrder(@PathVariable(name = "isSingleProductCheckout") boolean isSingleProductCheckout,
            @RequestBody OrderInput orderInput){
        orderDetailService.placeOrder(orderInput,isSingleProductCheckout);

    }

    @GetMapping({"/getOrderDetails"})
    @PreAuthorize("hasRole('User')")
    public List<OrderDetail> getOrderDetails(){
        return orderDetailService.getOrderDetails();

    }


    @GetMapping({"/getAllOrderDetails/{status}"})
    @PreAuthorize("hasRole('Admin')")
    public List<OrderDetail> getAllOrderDetails(@PathVariable (name = "status")String status){
        return orderDetailService. getAllOrders(status);

    }


    @GetMapping({"/markOrderAsDelivered/{orderId}"})
    @PreAuthorize("hasRole('Admin')")
    public void markOrderAsDelivered(@PathVariable("orderId")Integer orderId){
        orderDetailService.markOrderAsDelivered(orderId);
    }


    @GetMapping({"/createTransaction/{amount}"})
    @PreAuthorize("hasRole('User')")
    public TransactionDetails createTransaction(@PathVariable ("amount") Double amount){
       return orderDetailService.createTransaction(amount);
    }
}

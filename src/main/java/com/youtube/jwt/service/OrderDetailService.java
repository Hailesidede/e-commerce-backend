package com.youtube.jwt.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.youtube.jwt.configuration.JwtRequestFilter;
import com.youtube.jwt.dao.CartRepository;
import com.youtube.jwt.dao.OrderDetailDao;
import com.youtube.jwt.dao.ProductDao;
import com.youtube.jwt.dao.UserDao;
import com.youtube.jwt.entity.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailService {

    private static  final  String ORDER_PLACED = "placed";

    private static  final String KEY = "rzp_test_DWVoCSANNP98R1";

    private static  final String KEY_SECRETE = "bGR0KnAMZUcnGITFntqiDalV";

    private static  final String CURRENCY = "USD";


    @Autowired
    private OrderDetailDao orderDetailDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CartRepository cartRepo;

    public void placeOrder(OrderInput orderInput, Boolean isSingleProductCheckout){
        List<OrderProductQuantity> productQuantities = orderInput.getOrderProductQuantityList();

        for(OrderProductQuantity order : productQuantities){

           Product product =  productDao.findById(order.getProductId()).get();

           String currentUserName = JwtRequestFilter.CURRENT_USER;
           User user = userDao.findById(currentUserName).get();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderFullName(orderInput.getFullName());
            orderDetail.setOrderFullAddress(orderInput.getFullAddress());
            orderDetail.setOrderContactNumber(orderInput.getContactNumber());
            orderDetail.setOrderAlternateContact(orderInput.getAlternateContactNumber());
            orderDetail.setOrderStatus(ORDER_PLACED);
            orderDetail.setOrderAmount(product.getProductDiscountedPrice() * order.getQuantity());
            orderDetail.setProduct(product);
            orderDetail.setUser(user);

            if (!isSingleProductCheckout){
                List<Cart> carts = cartRepo.findByUser(user);
                carts.stream().forEach(x->cartRepo.deleteById(x.getCartId()));

            }

            orderDetailDao.save(orderDetail);
        }

    }


    public List<OrderDetail> getOrderDetails(){
        String username = JwtRequestFilter.CURRENT_USER;

        User user = userDao.findById(username).get();
        List<OrderDetail> orders = orderDetailDao.findByUser(user);
        return orders;

    }



    public List<OrderDetail> getAllOrders(String status){
        List<OrderDetail> orders = new ArrayList<>();

        if (status.equals("All")){
            orderDetailDao.findAll().forEach(
                    x->orders.add(x)
            );

        }else {
            orderDetailDao.findByOrderStatus(status).forEach(x->orders.add(x));
        }

        return orders;

    }


    public void markOrderAsDelivered(Integer orderId){
        OrderDetail order = orderDetailDao.findById(orderId).get();

        if (order != null){
            order.setOrderStatus("Delivered");
            orderDetailDao.save(order);
        }
    }


    public TransactionDetails createTransaction(Double amount){

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("amount",amount);
            jsonObject.put("currency",CURRENCY);

            RazorpayClient razorpayClient = new RazorpayClient(KEY,KEY_SECRETE);
            Order order = razorpayClient.orders.create(jsonObject);
            TransactionDetails transactionDetails =  prepareTransactionDetails(order);
            return  transactionDetails;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }


    private TransactionDetails prepareTransactionDetails (Order order){
        String orderId = order.get("id");
        String currency = order.get("currency");
        Integer amount = order.get("amount");

        TransactionDetails transactionDetails = new TransactionDetails(orderId,
                currency,amount,KEY);
        return transactionDetails;

    }
}

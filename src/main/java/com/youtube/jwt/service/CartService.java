package com.youtube.jwt.service;

import com.youtube.jwt.configuration.JwtRequestFilter;
import com.youtube.jwt.dao.CartRepository;
import com.youtube.jwt.dao.ProductDao;
import com.youtube.jwt.dao.UserDao;
import com.youtube.jwt.entity.Cart;
import com.youtube.jwt.entity.Product;
import com.youtube.jwt.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepo;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;



    public Cart addToCart(Integer productId){
        Product product = productDao.findById(productId).get();

        String userName = JwtRequestFilter.CURRENT_USER;
        User user = null;

        if (userName != null){
            user = userDao.findById(userName).get();
        }

        List<Cart> carts = cartRepo.findByUser(user);
        List<Cart> filteredList = carts.stream().filter(x->x.getProduct().getProductId() == productId).collect(Collectors.toList());
        if (filteredList.size() >0){
            return null;
        }

        if (product != null && user != null){
            Cart cart = new Cart(
                    product,user
            );
             return cartRepo.save(cart);
        }
        return null;
    }



    public List<Cart> getCartDetails(){
        String username = JwtRequestFilter.CURRENT_USER;
        User user = userDao.findById(username).get();
        return cartRepo.findByUser(user);
    }



    public void deleteCartItem(Integer cartId){
        cartRepo.deleteById(cartId);
    }


}

package com.youtube.jwt.service;

import com.youtube.jwt.configuration.JwtRequestFilter;
import com.youtube.jwt.dao.CartRepository;
import com.youtube.jwt.dao.ProductDao;
import com.youtube.jwt.dao.UserDao;
import com.youtube.jwt.entity.Cart;
import com.youtube.jwt.entity.Product;
import com.youtube.jwt.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CartRepository cartRepo;



    public Product addNewProduct(Product product){
        Product newProduct = productDao.save(product);
        return newProduct;
    }

    public List<Product> getAllProducts( int pageNumber, String searchKey){
        Pageable pageable = PageRequest.of(pageNumber,12);

        if(searchKey.equals("")){
            return  (List<Product>) productDao.findAll(pageable);
        }else{
            return (List<Product>) productDao.findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(
                    searchKey,searchKey,pageable
            );

        }

    }


    public Product getProductdetailsById(Integer productId){
        return productDao.findById(productId).get();
    }
    public void deleteProductDetails(Integer productId){
        productDao.deleteById(productId);
    }


    public List<Product> getProductDetails(boolean isSingleProductCheckout, int productId){
        if (isSingleProductCheckout && productId != 0){

            List<Product> products = new ArrayList<>();
            Product product = productDao.findById(productId).get();
            products.add(product);
            return products;
        }else{
           String username = JwtRequestFilter.CURRENT_USER;
           User user = userDao.findById(username).get();
           List<Cart> carts = cartRepo.findByUser(user);

           return carts.stream().map(x->x.getProduct()).collect(Collectors.toList());

        }

    }
}

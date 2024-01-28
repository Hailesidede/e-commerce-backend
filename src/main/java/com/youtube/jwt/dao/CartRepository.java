package com.youtube.jwt.dao;

import com.youtube.jwt.entity.Cart;
import com.youtube.jwt.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends CrudRepository<Cart, Integer> {

    public List<Cart> findByUser(User user);
}

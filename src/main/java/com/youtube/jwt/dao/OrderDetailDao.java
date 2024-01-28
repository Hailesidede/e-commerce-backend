package com.youtube.jwt.dao;

import com.youtube.jwt.entity.OrderDetail;
import com.youtube.jwt.entity.User;
import org.hibernate.criterion.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailDao extends CrudRepository<OrderDetail, Integer> {

    public List<OrderDetail> findByUser(User user);

    public  List<OrderDetail> findByOrderStatus(String orderStatus);
}

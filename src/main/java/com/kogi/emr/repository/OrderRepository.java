package com.kogi.emr.repository;

import com.kogi.emr.models.Order;
import com.kogi.emr.models.OrderStatus;
import com.kogi.emr.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long>, JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Page<Order> findByCreatorAndOrderStatus(User user, Pageable pageable, OrderStatus orderStatus);
    Optional<Order> findByIdAndCreator(Long id,User user);
    Optional<Order> findById(Long id);
    Page<Order> findByCreator(User user,Specification <Order> finalSpec, Pageable pageable);
    List<Order> findByCreatorAndOrderStatus(User user, OrderStatus orderStatus);
    Page<Order> findAll(Specification <Order> finalSpec,Pageable pageable);
}

package com.kogi.emr.services;

import com.kogi.emr.models.*;
import com.kogi.emr.payload.request.CreateOrderRequest;
import com.kogi.emr.payload.request.PatchOrderRequest;
import com.kogi.emr.payload.response.MessageResponse;
import com.kogi.emr.payload.response.OrderResponse;
import com.kogi.emr.repository.ItemRepository;
import com.kogi.emr.repository.OrderRepository;
import com.kogi.emr.repository.UserRepository;
import com.kogi.emr.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.kogi.emr.models.ERole.ROLE_ADMIN;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemService itemService;
    @Autowired
    JwtUtils jwtUtils;
     /**
     * This method is used to get a user from a jwt token.
     * @param jwtToken
     * @return an optional user object.
     */
    private Optional<User> getUser(String jwtToken) {
        String email = jwtUtils.getUserNameFromJwtToken(jwtToken);
        return userRepository.findByEmail(email);
    }

    /**
     * This method is used to check if a user is an admin.
     * @param roles
     * @return true if the user is an admin, false otherwise.
     */
    private boolean isAdmin(Set<Role> roles) {
        for (Role role : roles) {
            if (role.getName() == ROLE_ADMIN) {
                return true;
            }
        }
        return false;
    }
     /**
     * This method is used to create a order.
     * @param createOrderRequest
     * @param jwtToken
     * @return 200 OK with the created order if the order is created successfully, 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> createOrder(CreateOrderRequest createOrderRequest, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);
        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        Optional<Item> item = itemRepository.findById(Long.valueOf(createOrderRequest.getItemId()));
        Order newOrder = new OrderBuilder()
                .withQuantity(createOrderRequest.getQuantity())
                .withItem(item.get())
                .withCreator(creator.get())
                .withOrderStatus(OrderStatus.PENDING)
                .build();
        Order createdOrder = orderRepository.save(newOrder);
        return ResponseEntity.ok(new OrderResponse("Order created successfully!", createdOrder));
    }
     /**
     * This method is used to list orders.
     * @param pageable
     * @param jwtToken
     * @return 200 OK with a list of orders(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> listPaged(Pageable pageable, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);
        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        Page<Order> orders = orderRepository.findByCreatorAndOrderStatus(creator.get(), pageable,OrderStatus.PENDING);

        return ResponseEntity.ok(orders);
    }

    /**
     * This method is used to list orders.
     * @param jwtToken
     * @return 200 OK with a list of orders(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> list( String jwtToken) {
        Optional<User> creator = getUser(jwtToken);
        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        List<Order> orders = orderRepository.findByCreatorAndOrderStatus(creator.get(),OrderStatus.PENDING);

        return ResponseEntity.ok(orders);
    }

    /**
     * This method is used to update a order.
     * @param id
     * @param partialUpdateDto
     * @param jwtToken
     * @return 200 OK if the order is updated successfully, 404 NOT FOUND if the order does not exist and 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> patchOrder(Long id, PatchOrderRequest partialUpdateDto, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);

        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        boolean isCreatorAnAdmin = isAdmin(creator.get().getRoles());
        Optional<Order>
            order = orderRepository.findById(id);

        if(order.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        Order orderToUpdate = order.get();
        if (partialUpdateDto.getQuantity() != null) {
            orderToUpdate.setQuantity(partialUpdateDto.getQuantity());
        }

        if (partialUpdateDto.getOrderStatus() != null) {
            orderToUpdate.setOrderStatus(OrderStatus.valueOf(partialUpdateDto.getOrderStatus()));
        }
        Order updatedOrder = orderRepository.save(orderToUpdate);
        return ResponseEntity.ok(new OrderResponse("Order updated successfully!", updatedOrder));

    }

    /**
     * This method is used to list orders.
     * @param jwtToken
     * @return 200 OK with a list of orders(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> checkOut( String jwtToken) {
        Optional<User> creator = getUser(jwtToken);
        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        List<Order> orders = orderRepository.findByCreatorAndOrderStatus(creator.get(),OrderStatus.PENDING);
        for(Order order: orders) {
            order.setOrderStatus(OrderStatus.PAID);
            itemService.updateStock(order.getItem().getId(), order.getQuantity(), jwtToken);
            orderRepository.save(order);
        }
         orders = orderRepository.findByCreatorAndOrderStatus(creator.get(),OrderStatus.PENDING);
        return ResponseEntity.ok(orders);
    }

    /**
     * This method is used to delete a order.
     * @param id
     * @param jwtToken
     * @return 200 OK if the order is deleted successfully, 404 NOT FOUND if the order does not exist.
     */
    @Override
    public ResponseEntity<?> deleteOrder(Long id, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);

        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }

        Optional<Order>
            order = orderRepository.findById(id);


        if(order.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        orderRepository.delete(order.get());
        return ResponseEntity.ok(new MessageResponse("Order deleted"));

    }
}

package com.kogi.emr.services;

import com.kogi.emr.payload.request.CreateOrderRequest;
import com.kogi.emr.payload.request.PatchOrderRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    /**
     * This method is used to create an Order.
     * @param createOrderRequest
     * @param authorizationHeader
     * @return 200 OK with the created order if the order is created successfully, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> createOrder(CreateOrderRequest createOrderRequest, String authorizationHeader);
    /**
     * This method is used to list orders.
     * @param pageable
     * @param jwtToken
     * @return 200 OK with a list of orders(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> listPaged(Pageable pageable, String jwtToken);

    /**
     * This method is used to list orders.
     * @param jwtToken
     * @return 200 OK with a list of orders(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> list( String jwtToken);

    /**
     * This method is used to update an order.
     * @param id
     * @param partialUpdateDto
     * @param jwtToken
     * @return 200 OK if the order is updated successfully, 404 NOT FOUND if the order does not exist and 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> patchOrder(Long id, PatchOrderRequest partialUpdateDto, String jwtToken);
    /**
     * This method is used to delete an order.
     * @param id
     * @param jwtToken
     * @return 200 OK if the order is deleted successfully, 404 NOT FOUND if the order does not exist.
     */
    ResponseEntity<?> deleteOrder(Long id, String jwtToken);


    /**
     * This method is used to check out orders.
     * @param jwtToken
     * @return 200 OK  if the checkout is successful, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> checkOut( String jwtToken);

}

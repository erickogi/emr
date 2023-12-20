package com.kogi.emr.controllers;

import com.kogi.emr.payload.request.CreateOrderRequest;
import com.kogi.emr.payload.request.PatchOrderRequest;
import com.kogi.emr.payload.response.*;
import com.kogi.emr.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * Used to create a order.
     * @param authorizationHeader
     * @param createOrderRequest
     * @param bindingResult
     * @return 200 OK and OrderResponse if the order is created successfully, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "Create am Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @PostMapping("/create")
    @PreAuthorize("hasRole('PHARMACIST') or hasRole('ADMIN')")
    public ResponseEntity<?> createOrder(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody CreateOrderRequest createOrderRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(new RequestValidationErrorResponse("Request Validation errors", bindingResult.getAllErrors()));
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return orderService.createOrder(createOrderRequest, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to list all orders.
     * @param authorizationHeader
     * @param pageable
     * @return 200 OK and OrderPage if the orders are listed successfully, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "List Orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Orders accessible to user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderPage.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @GetMapping("/list/paged")
    @PreAuthorize("hasRole('PHARMACIST') or hasRole('ADMIN')")
    public ResponseEntity<?> listOrdersPaged(
            @RequestHeader("Authorization") String authorizationHeader,
            Pageable pageable) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return orderService.listPaged(pageable, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to list all orders.
     * @param authorizationHeader
     * @return 200 OK and OrdersResponse if the orders are listed successfully, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "List Orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Orders accessible to user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrdersResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @GetMapping("/list")
    @PreAuthorize("hasRole('PHARMACIST') or hasRole('ADMIN')")
    public ResponseEntity<?> listOrders(
            @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return orderService.list(jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to update an Order.
     * @param authorizationHeader
     * @param id
     * @param partialUpdateDto
     * @param bindingResult
     * @return 200 OK and OrderResponse if the order is updated successfully, 401 UNAUTHORIZED if the token is invalid. 400 BAD REQUEST if the order is not found because of role
     */
    @Operation(summary = "Update a Order characteristics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order object if accessible by User",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrderResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Order Not Found for given id and user",
                    content = @Content)
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> patchOrder(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id,
            @Valid @RequestBody PatchOrderRequest partialUpdateDto
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(new RequestValidationErrorResponse("Request Validation errors", bindingResult.getAllErrors()));
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return orderService.patchOrder(id, partialUpdateDto, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }
    /**
     * Used to delete a order.
     * @param authorizationHeader
     * @param id
     * @return 200 OK and MessageResponse if the order is deleted successfully, 401 UNAUTHORIZED if the token is invalid. 400 BAD REQUEST if the order is not found because of role
     */
    @Operation(summary = "Delete a Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order object if accessible by User",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Order Not Found for given id and user",
                    content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteOrder(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @PathVariable Long id) {

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return orderService.deleteOrder(id, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }


    /**
     * Used to checkout user's orders.
     * @param authorizationHeader
     * @return 200 OK , 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "List Orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Checkout successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = OrdersResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @GetMapping("/checkout")
    @PreAuthorize("hasRole('PHARMACIST') or hasRole('ADMIN')")
    public ResponseEntity<?> checkout(
            @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return orderService.checkOut(jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

}



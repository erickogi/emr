package com.kogi.emr.controllers;

import com.kogi.emr.models.Item;
import com.kogi.emr.payload.request.CreateItemRequest;
import com.kogi.emr.payload.request.PatchItemRequest;
import com.kogi.emr.payload.response.ItemPage;
import com.kogi.emr.payload.response.ItemResponse;
import com.kogi.emr.payload.response.MessageResponse;
import com.kogi.emr.payload.response.RequestValidationErrorResponse;
import com.kogi.emr.services.ItemService;
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

@CrossOrigin(origins = "*", maxAge = 3600,allowedHeaders = "*")
@RestController
@RequestMapping("/api/items")
@Validated
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**
     * Used to create a item.
     * @param authorizationHeader
     * @param createItemRequest
     * @param bindingResult
     * @return 200 OK and ItemResponse if the item is created successfully, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "Create a Item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @PostMapping("/create")
    @PreAuthorize("hasRole('PHARMACIST') or hasRole('ADMIN')")
    public ResponseEntity<?> createItem(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody CreateItemRequest createItemRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(new RequestValidationErrorResponse("Request Validation errors", bindingResult.getAllErrors()));
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return itemService.createItem(createItemRequest, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to list all items.
     * @param authorizationHeader
     * @param pageable
     * @return 200 OK and ItemPage if the items are listed successfully, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "List Items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Items accessible to user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemPage.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @GetMapping("/list")
    @PreAuthorize("hasRole('PHARMACIST') or hasRole('ADMIN')")
    public ResponseEntity<?> listItems(
            @RequestHeader("Authorization") String authorizationHeader,
            Pageable pageable) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return itemService.list(pageable, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to search items.
     * @param authorizationHeader
     * @param name
     * @param category
     * @param pageable
     * @return 200 OK and ItemPage if the items are listed successfully, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "Items by name,description,color,date,status. Sorted by any field")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Items accessible to user filtered and sorted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemPage.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @GetMapping("/search")
    @PreAuthorize("hasRole('PHARMACIST') or hasRole('ADMIN')")
    public ResponseEntity<?> searchItems(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "category", required = false) String category,
            Pageable pageable) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return itemService.search(pageable, jwtToken, name, category);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to get a item by its id.
     * @param authorizationHeader
     * @param id
     * @return 200 OK and ItemResponse if the item is retrieved successfully, 401 UNAUTHORIZED if the token is invalid. 400 BAD REQUEST if the item is not found because of role
     */
    @Operation(summary = "Get one item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item object if accessible by User",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Item.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Item Not Found for given id and user",
                    content = @Content)
    })
    @GetMapping("/item")
    @PreAuthorize("hasRole('PHARMACIST') or hasRole('ADMIN')")
    public ResponseEntity<?> item(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(name = "id") Long id) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return itemService.getOne(id, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to update a item.
     * @param authorizationHeader
     * @param id
     * @param partialUpdateDto
     * @param bindingResult
     * @return 200 OK and ItemResponse if the item is updated successfully, 401 UNAUTHORIZED if the token is invalid. 400 BAD REQUEST if the item is not found because of role
     */
    @Operation(summary = "Update a Item characteristics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item object if accessible by User",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ItemResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Item Not Found for given id and user",
                    content = @Content)
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> patchItem(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id,
            @Valid @RequestBody PatchItemRequest partialUpdateDto
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(new RequestValidationErrorResponse("Request Validation errors", bindingResult.getAllErrors()));
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return itemService.patchItem(id, partialUpdateDto, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }
    /**
     * Used to delete a item.
     * @param authorizationHeader
     * @param id
     * @return 200 OK and MessageResponse if the item is deleted successfully, 401 UNAUTHORIZED if the token is invalid. 400 BAD REQUEST if the item is not found because of role
     */
    @Operation(summary = "Delete a Item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item object if accessible by User",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Item Not Found for given id and user",
                    content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteItem(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @PathVariable Long id) {

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return itemService.deleteItem(id, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

}



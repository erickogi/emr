package com.kogi.emr.services;

import com.kogi.emr.payload.request.CreateItemRequest;
import com.kogi.emr.payload.request.PatchItemRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface ItemService {

    /**
     * This method is used to create a item.
     * @param createItemRequest
     * @param authorizationHeader
     * @return 200 OK with the created item if the item is created successfully, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> createItem(CreateItemRequest createItemRequest, String authorizationHeader);
    /**
     * This method is used to list items.
     * @param pageable
     * @param jwtToken
     * @return 200 OK with a list of items(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> list(Pageable pageable, String jwtToken);
    /**
     * This method is used to search items.
     * @param pageable
     * @param jwtToken
     * @param name
     * @param category
     * @return 200 OK with a list of items(Can be empty) if the search is successful, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> search(Pageable pageable, String jwtToken, String name, String category);
    /**
     * This method is used to get an item.
     * @param id
     * @param jwtToken
     * @return 200 OK with item object if the item is found, 404 NOT FOUND if the item does not exist and 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> getOne(Long id, String jwtToken);
    /**
     * This method is used to update a item.
     * @param id
     * @param partialUpdateDto
     * @param jwtToken
     * @return 200 OK if the item is updated successfully, 404 NOT FOUND if the item does not exist and 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> patchItem(Long id, PatchItemRequest partialUpdateDto, String jwtToken);
    /**
     * This method is used to delete a item.
     * @param id
     * @param jwtToken
     * @return 200 OK if the item is deleted successfully, 404 NOT FOUND if the item does not exist.
     */
    ResponseEntity<?> deleteItem(Long id, String jwtToken);

    ResponseEntity<?> updateStock(Long id, Integer quantity,String jwtToken);
}

package com.kogi.emr.services;

import com.kogi.emr.payload.request.CreateCategoryRequest;
import com.kogi.emr.payload.request.PatchCategoryRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CategoryService {

    /**
     * This method is used to create a category.
     * @param createCategoryRequest
     * @param authorizationHeader
     * @return 200 OK with the created card if the category is created successfully, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> createCategory(CreateCategoryRequest createCategoryRequest, String authorizationHeader);
    /**
     * This method is used to list category.
     * @param pageable
     * @param jwtToken
     * @return 200 OK with a list of category(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> listPaged(Pageable pageable, String jwtToken);
    /**
     * This method is used to list category.
     * @param jwtToken
     * @return 200 OK with a list of category(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> list( String jwtToken);

    /**
     * This method is used to update a category.
     * @param id
     * @param partialUpdateDto
     * @param jwtToken
     * @return 200 OK if the card is updated successfully, 404 NOT FOUND if the category does not exist and 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> patchCategory(Long id, PatchCategoryRequest partialUpdateDto, String jwtToken);
    /**
     * This method is used to delete a category.
     * @param id
     * @param jwtToken
     * @return 200 OK if the category is deleted successfully, 404 NOT FOUND if the category does not exist.
     */
    ResponseEntity<?> deleteCategory(Long id, String jwtToken);

}

package com.kogi.emr.services;

import com.kogi.emr.payload.request.CreateCategoryRequest;
import com.kogi.emr.payload.request.CreateTagRequest;
import com.kogi.emr.payload.request.PatchCategoryRequest;
import com.kogi.emr.payload.request.PatchTagRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface TagService {

    /**
     * This method is used to create a tag.
     * @param createTagRequest
     * @param authorizationHeader
     * @return 200 OK with the created card if the tag is created successfully, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> createTag(CreateTagRequest createTagRequest, String authorizationHeader);
    /**
     * This method is used to list tag.
     * @param pageable
     * @param jwtToken
     * @return 200 OK with a list of tag(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> listPaged(Pageable pageable, String jwtToken);
    /**
     * This method is used to list tag.
     * @param jwtToken
     * @return 200 OK with a list of tag(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> list( String jwtToken);

    /**
     * This method is used to update a tag.
     * @param id
     * @param partialUpdateDto
     * @param jwtToken
     * @return 200 OK if the tag is updated successfully, 404 NOT FOUND if the tag does not exist and 400 BAD REQUEST if the user does not exist.
     */
    ResponseEntity<?> patchTag(Long id, PatchTagRequest partialUpdateDto, String jwtToken);
    /**
     * This method is used to delete a tag.
     * @param id
     * @param jwtToken
     * @return 200 OK if the tag is deleted successfully, 404 NOT FOUND if the tag does not exist.
     */
    ResponseEntity<?> deleteTag(Long id, String jwtToken);

}

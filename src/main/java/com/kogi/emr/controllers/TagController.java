package com.kogi.emr.controllers;

import com.kogi.emr.payload.request.CreateCategoryRequest;
import com.kogi.emr.payload.request.CreateTagRequest;
import com.kogi.emr.payload.request.PatchCategoryRequest;
import com.kogi.emr.payload.request.PatchTagRequest;
import com.kogi.emr.payload.response.*;
import com.kogi.emr.services.CategoryService;
import com.kogi.emr.services.TagService;
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
@RequestMapping("/api/tags")
@Validated
public class TagController {
    @Autowired
    private TagService tagService;

    /**
     * Used to create a tag.
     * @param authorizationHeader
     * @param createTagRequest
     * @param bindingResult
     * @return 200 OK and TagResponse if the category is created successfully, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "Create a Tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @PostMapping("/create")
    @PreAuthorize("hasRole('PHARMACIST') or hasRole('ADMIN')")
    public ResponseEntity<?> createTag(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody CreateTagRequest createTagRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(new RequestValidationErrorResponse("Request Validation errors", bindingResult.getAllErrors()));
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return tagService.createTag(createTagRequest, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to list all tags.
     * @param authorizationHeader
     * @param pageable
     * @return 200 OK and TagsPage if the tags are listed successfully, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "List Tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Tags accessible to user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagPage.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @GetMapping("/list/paged")
    @PreAuthorize("hasRole('PHARMACIST') or hasRole('ADMIN')")
    public ResponseEntity<?> listTagsPaged(
            @RequestHeader("Authorization") String authorizationHeader,
            Pageable pageable) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return tagService.listPaged(pageable, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to list all tags.
     * @param authorizationHeader
     * @return 200 OK and CategoryPage if the categories are listed successfully, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "List Tags")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Tags accessible to user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @GetMapping("/list")
    @PreAuthorize("hasRole('PHARMACIST') or hasRole('ADMIN')")
    public ResponseEntity<?> listTags(
            @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return tagService.list(jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to update a tag.
     * @param authorizationHeader
     * @param id
     * @param partialUpdateDto
     * @param bindingResult
     * @return 200 OK and TagResponse if the tag is updated successfully, 401 UNAUTHORIZED if the token is invalid. 400 BAD REQUEST if the tag is not found because of role
     */
    @Operation(summary = "Update a Tag characteristics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag object if accessible by User",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TagResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Tag Not Found for given id and user",
                    content = @Content)
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> patchTag(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id,
            @Valid @RequestBody PatchTagRequest partialUpdateDto
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(new RequestValidationErrorResponse("Request Validation errors", bindingResult.getAllErrors()));
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return tagService.patchTag(id, partialUpdateDto, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }
    /**
     * Used to delete a tag.
     * @param authorizationHeader
     * @param id
     * @return 200 OK and MessageResponse if the tag is deleted successfully, 401 UNAUTHORIZED if the token is invalid. 400 BAD REQUEST if the tag is not found because of role
     */
    @Operation(summary = "Delete a Tag")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag object if accessible by User",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Tag Not Found for given id and user",
                    content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTag(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @PathVariable Long id) {

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return tagService.deleteTag(id, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

}



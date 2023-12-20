package com.kogi.emr.controllers;

import com.kogi.emr.payload.request.CreateCategoryRequest;
import com.kogi.emr.payload.request.PatchCategoryRequest;
import com.kogi.emr.payload.response.*;
import com.kogi.emr.services.CategoryService;
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
@RequestMapping("/api/categories")
@Validated
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * Used to create a category.
     * @param authorizationHeader
     * @param createCategoryRequest
     * @param bindingResult
     * @return 200 OK and CategoryResponse if the tag is created successfully, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "Create a Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category created successfully",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @PostMapping("/create")
    @PreAuthorize("hasRole('PHARMACIST') or hasRole('ADMIN')")
    public ResponseEntity<?> createCategory(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody CreateCategoryRequest createCategoryRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(new RequestValidationErrorResponse("Request Validation errors", bindingResult.getAllErrors()));
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return categoryService.createCategory(createCategoryRequest, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to list all categories.
     * @param authorizationHeader
     * @param pageable
     * @return 200 OK and CategoryPage if the categories are listed successfully, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "List Categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Categories accessible to user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryPage.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @GetMapping("/list/paged")
    @PreAuthorize("hasRole('PHARMACIST') or hasRole('ADMIN')")
    public ResponseEntity<?> listCategories(
            @RequestHeader("Authorization") String authorizationHeader,
            Pageable pageable) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return categoryService.listPaged(pageable, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to list all categories.
     * @param authorizationHeader
     * @return 200 OK and CategoryPage if the categories are listed successfully, 401 UNAUTHORIZED if the token is invalid.
     */
    @Operation(summary = "List Categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Categories accessible to user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoriesResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content)})
    @GetMapping("/list")
    @PreAuthorize("hasRole('PHARMACIST') or hasRole('ADMIN')")
    public ResponseEntity<?> listCategories(
            @RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return categoryService.list(jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

    /**
     * Used to update a category.
     * @param authorizationHeader
     * @param id
     * @param partialUpdateDto
     * @param bindingResult
     * @return 200 OK and CategoryResponse if the category is updated successfully, 401 UNAUTHORIZED if the token is invalid. 400 BAD REQUEST if the category is not found because of role
     */
    @Operation(summary = "Update a Category characteristics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "category object if accessible by User",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CategoryResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Category Not Found for given id and user",
                    content = @Content)
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> patchCategory(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id,
            @Valid @RequestBody PatchCategoryRequest partialUpdateDto
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(new RequestValidationErrorResponse("Request Validation errors", bindingResult.getAllErrors()));
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return categoryService.patchCategory(id, partialUpdateDto, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }
    /**
     * Used to delete a category.
     * @param authorizationHeader
     * @param id
     * @return 200 OK and MessageResponse if the category is deleted successfully, 401 UNAUTHORIZED if the token is invalid. 400 BAD REQUEST if the category is not found because of role
     */
    @Operation(summary = "Delete a Category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category object if accessible by User",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))}),
            @ApiResponse(responseCode = "401", description = "UnAuthorized",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Category Not Found for given id and user",
                    content = @Content)
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCategory(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @PathVariable Long id) {

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            return categoryService.deleteCategory(id, jwtToken);
        } else {
            return ResponseEntity
                    .status(401)
                    .body(new MessageResponse("Invalid token"));
        }
    }

}



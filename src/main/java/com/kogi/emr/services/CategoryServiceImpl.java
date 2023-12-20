package com.kogi.emr.services;

import com.kogi.emr.models.Category;
import com.kogi.emr.models.CategoryBuilder;
import com.kogi.emr.models.Role;
import com.kogi.emr.models.User;
import com.kogi.emr.payload.request.CreateCategoryRequest;
import com.kogi.emr.payload.request.PatchCategoryRequest;
import com.kogi.emr.payload.response.CategoryResponse;
import com.kogi.emr.payload.response.MessageResponse;
import com.kogi.emr.repository.CategoryRepository;
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
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

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
     * This method is used to create a category.
     * @param createCategoryRequest
     * @param jwtToken
     * @return 200 OK with the created category if the category is created successfully, 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> createCategory(CreateCategoryRequest createCategoryRequest, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);
        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        Category newCategory = new CategoryBuilder()
                .withName(createCategoryRequest.getName())
                .build();
        Category createdCategory = categoryRepository.save(newCategory);
        return ResponseEntity.ok(new CategoryResponse("Category created successfully!", createdCategory));
    }

    /**
     * This method is used to list categories.
     * @param jwtToken
     * @return 200 OK with a list of categories(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> list(String jwtToken) {
       List<Category> categories = categoryRepository.findAll();

        return ResponseEntity.ok(categories);
    }

    /**
     * This method is used to list categories.
     * @param pageable
     * @param jwtToken
     * @return 200 OK with a list of categories(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> listPaged(Pageable pageable, String jwtToken) {

        Page<Category>  categories = categoryRepository.findAll(pageable);

        return ResponseEntity.ok(categories);
    }



    /**
     * This method is used to update a category.
     * @param id
     * @param partialUpdateDto
     * @param jwtToken
     * @return 200 OK if the category is updated successfully, 404 NOT FOUND if the category does not exist and 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> patchCategory(Long id, PatchCategoryRequest partialUpdateDto, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);

        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        boolean isCreatorAnAdmin = isAdmin(creator.get().getRoles());
        Optional<Category> category = categoryRepository.findById(id);

        if(category.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        Category categoryToUpdate = category.get();
        if (partialUpdateDto.getName() != null) {
            categoryToUpdate.setName(partialUpdateDto.getName());
        }

        Category updatedCategory = categoryRepository.save(categoryToUpdate);
        return ResponseEntity.ok(new CategoryResponse("Category updated successfully!", updatedCategory));

    }

    /**
     * This method is used to delete a category.
     * @param id
     * @param jwtToken
     * @return 200 OK if the category is deleted successfully, 404 NOT FOUND if the category does not exist.
     */
    @Override
    public ResponseEntity<?> deleteCategory(Long id, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);

        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        boolean isCreatorAnAdmin = isAdmin(creator.get().getRoles());
        Optional<Category> category;
        if(isCreatorAnAdmin){
            category = categoryRepository.findById(id);
        }else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User is not an admin"));
        }

        if(category.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        categoryRepository.delete(category.get());
        return ResponseEntity.ok(new MessageResponse("Category deleted"));

    }
}

package com.kogi.emr.services;

import com.kogi.emr.models.*;
import com.kogi.emr.payload.request.CreateItemRequest;
import com.kogi.emr.payload.request.PatchItemRequest;
import com.kogi.emr.payload.response.ItemResponse;
import com.kogi.emr.payload.response.MessageResponse;
import com.kogi.emr.repository.CategoryRepository;
import com.kogi.emr.repository.ItemRepository;
import com.kogi.emr.repository.TagRepository;
import com.kogi.emr.repository.UserRepository;
import com.kogi.emr.security.jwt.JwtUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.kogi.emr.models.ERole.ROLE_ADMIN;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    SMSService emailService;
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
     * This method is used to create a item.
     * @param createItemRequest
     * @param jwtToken
     * @return 200 OK with the created item if the item is created successfully, 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> createItem(CreateItemRequest createItemRequest, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);
        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        Category category =  null;
        if(createItemRequest.getCategoryID() != null) {
          category =  categoryRepository.findById(Long.valueOf(createItemRequest.getCategoryID())).get();
        }

        if(createItemRequest.getCategoryName() != null) {
            category =  categoryRepository.findByName(createItemRequest.getCategoryName()).get();
        }
        List<Tag> tags = new ArrayList<>();
        Set<Tag> tagSet = new java.util.HashSet<>();
        if(createItemRequest.getTagIDs() != null) {
            tags =  tagRepository.findByIdIn(createItemRequest.getTagIDs());
        }
        tagSet.addAll(tags);
        Item newItem = new ItemBuilder()
                .withName(createItemRequest.getName())
                .withDescription(createItemRequest.getDescription())
                .withReorderPoint(createItemRequest.getReorderPoint())
                .withQuantityAvailable(createItemRequest.getQuantityAvailable())
                .withQuantityOnHold(createItemRequest.getQuantityOnHold())
                .withMinimumStockLevel(createItemRequest.getMinimumStockLevel())
                .withMetadata(createItemRequest.getMetadata())
                .withCategory(category)
                .withTag(tagSet)
                .withCostPrice(createItemRequest.getCostPrice())
                .build();
        Item createdItem = itemRepository.save(newItem);
        return ResponseEntity.ok(new ItemResponse("Item created successfully!", createdItem));
    }
     /**
     * This method is used to list items.
     * @param pageable
     * @param jwtToken
     * @return 200 OK with a list of items(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> list(Pageable pageable, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);
        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        Page<Item> items
             = itemRepository.findAll(pageable);

        return ResponseEntity.ok(items);
    }

    /**
     * This method is used to search items.
     * @param pageable
     * @param jwtToken
     * @param name
    * @param category
     * @return 200 OK with a list of items(Can be empty) if the search is successful, 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> search(Pageable pageable, String jwtToken, String name, String category) {
        Optional<User> creator = getUser(jwtToken);

        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        Specification<Item> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (name != null) {
                predicate = cb.and(predicate, cb.equal(root.get("name"), name));
            }

            if (category != null) {
                Optional<Category> category1 = categoryRepository.findById(Long.valueOf(category));
                if(category1.isPresent()) {
                    predicate = cb.and(predicate, cb.equal(root.get("category"), category1.get()));
                }
            }

            return predicate;
        };
        Page<Item> items = itemRepository.findAll(specification, pageable);
        return ResponseEntity.ok(items);
    }

    /**
     * This method is used to get a item.
     * @param id
     * @param jwtToken
     * @return 200 OK with item object if the item is found, 404 NOT FOUND if the item does not exist and 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> getOne(Long id, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);

        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        Optional<Item> item = itemRepository.findById(id);

        if(item.isPresent()) {
            return ResponseEntity.ok(item);
        }else {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    /**
     * This method is used to update a item.
     * @param id
     * @param partialUpdateDto
     * @param jwtToken
     * @return 200 OK if the item is updated successfully, 404 NOT FOUND if the item does not exist and 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> patchItem(Long id, PatchItemRequest partialUpdateDto, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);

        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }

        Category category =  null;
        if(partialUpdateDto.getCategoryID() != null) {
            category =  categoryRepository.findById(Long.valueOf(partialUpdateDto.getCategoryID())).get();
        }

        if(partialUpdateDto.getCategoryName() != null) {
            category =  categoryRepository.findByName(partialUpdateDto.getCategoryName()).get();
        }
        List<Tag> tags = new ArrayList<>();
        Set<Tag> tagSet = new HashSet<>();
        if(partialUpdateDto.getTagIDs() != null) {
            tags =  tagRepository.findByIdIn(partialUpdateDto.getTagIDs());
        }
        tagSet.addAll(tags);

        boolean isCreatorAnAdmin = isAdmin(creator.get().getRoles());
        Optional<Item> item;
        if(isCreatorAnAdmin){
            item = itemRepository.findById(id);
        }else {

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not have permission to update this item"));
        }

        if(item.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        Item itemToUpdate = item.get();
        if (partialUpdateDto.getName() != null) {
            itemToUpdate.setName(partialUpdateDto.getName());
        }
        if (partialUpdateDto.getCategoryID() != null || partialUpdateDto.getCategoryName() != null || partialUpdateDto.getCategoryUUID() != null) {
            itemToUpdate.setCategory(category);
        }
        if (partialUpdateDto.getDescription() != null) {
            itemToUpdate.setDescription(partialUpdateDto.getDescription());
        }
        if (partialUpdateDto.getMetadata() != null) {
            itemToUpdate.setMetadata(partialUpdateDto.getMetadata());
        }
        if (partialUpdateDto.getQuantityAvailable() != null) {
            itemToUpdate.setQuantityAvailable(partialUpdateDto.getQuantityAvailable());
        }
        if (partialUpdateDto.getQuantityOnHold() != null) {
            itemToUpdate.setQuantityOnHold(partialUpdateDto.getQuantityOnHold());
        }
        if (partialUpdateDto.getTagIDs() != null) {
            itemToUpdate.setTags(tagSet);
        }
        if (partialUpdateDto.getMinimumStockLevel() != null) {
            itemToUpdate.setMinimumStockLevel(partialUpdateDto.getMinimumStockLevel());
        }
        if (partialUpdateDto.getReorderPoint() != null) {
            itemToUpdate.setReorderPoint(partialUpdateDto.getReorderPoint());
        }
        if (partialUpdateDto.getCostPrice() != null) {
            itemToUpdate.setCostPrice(partialUpdateDto.getCostPrice());
        }
        Item updatedItem = itemRepository.save(itemToUpdate);
        return ResponseEntity.ok(new ItemResponse("Item updated successfully!", updatedItem));

    }

    /**
     * This method is used to delete a item.
     * @param id
     * @param jwtToken
     * @return 200 OK if the item is deleted successfully, 404 NOT FOUND if the item does not exist.
     */
    @Override
    public ResponseEntity<?> deleteItem(Long id, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);

        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        boolean isCreatorAnAdmin = isAdmin(creator.get().getRoles());
        Optional<Item> item;
        if(isCreatorAnAdmin){
            item = itemRepository.findById(id);
        }else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not have permission to delete this item"));
        }

        if(item.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        itemRepository.delete(item.get());
        return ResponseEntity.ok(new MessageResponse("Item deleted"));

    }

    @Override
    public ResponseEntity<?> updateStock(Long id, Integer quantity, String jwtToken) {
        Optional<Item> item;
        item = itemRepository.findById(id);
        if(item.isPresent()) {
            Item itemToUpdate = item.get();
            Integer newQuantity = itemToUpdate.getQuantityAvailable() - quantity;
            itemToUpdate.setQuantityAvailable(newQuantity);
            itemRepository.save(itemToUpdate);
            if(newQuantity < itemToUpdate.getMinimumStockLevel()) {
                sendStockAlertSMS(itemToUpdate, jwtToken);
                return ResponseEntity.ok(new MessageResponse("Item stock updated successfully! Item is below minimum stock level!"));
            }
            return ResponseEntity.ok(new MessageResponse("Item stock updated successfully!"));
        }

        return null;
    }

    private void sendStockAlertSMS(Item itemToUpdate, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);
        if (creator.isEmpty()) {
            return;
        }
        List<String> recipients = new ArrayList<>();
        userRepository.findAll().forEach(user -> {
            if(isAdmin(user.getRoles())) {
               recipients.add(user.getMobileNumber());
            }
        });
        String[] recepientsArray = new String[recipients.size()];
        for(int i = 0; i < recipients.size(); i++) recepientsArray[i] = recipients.get(i);
        String subject = "Item stock alert "+itemToUpdate.getName();
        String body = "Item "+itemToUpdate.getName()+" stock is below minimum stock level of"+itemToUpdate.getMinimumStockLevel()+" \n" +
                "Current stock level is "+itemToUpdate.getQuantityAvailable();
        emailService.sendSMSMessage(new EmailDetails(recepientsArray,body,subject));


    }
}

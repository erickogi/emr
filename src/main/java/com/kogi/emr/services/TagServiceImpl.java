package com.kogi.emr.services;

import com.kogi.emr.models.Tag;
import com.kogi.emr.models.Role;
import com.kogi.emr.models.TagBuilder;
import com.kogi.emr.models.User;
import com.kogi.emr.payload.request.CreateTagRequest;
import com.kogi.emr.payload.request.PatchTagRequest;
import com.kogi.emr.payload.response.TagResponse;
import com.kogi.emr.payload.response.MessageResponse;
import com.kogi.emr.repository.TagRepository;
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
public class TagServiceImpl implements TagService {
    @Autowired
    TagRepository tagRepository;

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
     * This method is used to create a tag.
     * @param createTagRequest
     * @param jwtToken
     * @return 200 OK with the created tag if the tag is created successfully, 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> createTag(CreateTagRequest createTagRequest, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);
        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        Tag newTag = new TagBuilder()
                .withName(createTagRequest.getName())
                .build();
        Tag createdTag = tagRepository.save(newTag);
        return ResponseEntity.ok(new TagResponse("Tag created successfully!", createdTag));
    }

    /**
     * This method is used to list tags.
     * @param jwtToken
     * @return 200 OK with a list of tags(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> list(String jwtToken) {
       List<Tag> tags = tagRepository.findAll();
       
        return ResponseEntity.ok(tags);
    }

    /**
     * This method is used to list tags.
     * @param pageable
     * @param jwtToken
     * @return 200 OK with a list of tags(Can be empty) if the list is successful, 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> listPaged(Pageable pageable, String jwtToken) {
       
        Page<Tag>  tags = tagRepository.findAll(pageable);
       
        return ResponseEntity.ok(tags);
    }


 
    /**
     * This method is used to update a tag.
     * @param id
     * @param partialUpdateDto
     * @param jwtToken
     * @return 200 OK if the tag is updated successfully, 404 NOT FOUND if the tag does not exist and 400 BAD REQUEST if the user does not exist.
     */
    @Override
    public ResponseEntity<?> patchTag(Long id, PatchTagRequest partialUpdateDto, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);

        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        boolean isCreatorAnAdmin = isAdmin(creator.get().getRoles());
        Optional<Tag> tag = tagRepository.findById(id);
        
        if(tag.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        Tag tagToUpdate = tag.get();
        if (partialUpdateDto.getName() != null) {
            tagToUpdate.setName(partialUpdateDto.getName());
        }
       
        Tag updatedTag = tagRepository.save(tagToUpdate);
        return ResponseEntity.ok(new TagResponse("Tag updated successfully!", updatedTag));

    }

    /**
     * This method is used to delete a tag.
     * @param id
     * @param jwtToken
     * @return 200 OK if the tag is deleted successfully, 404 NOT FOUND if the tag does not exist.
     */
    @Override
    public ResponseEntity<?> deleteTag(Long id, String jwtToken) {
        Optional<User> creator = getUser(jwtToken);

        if (creator.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User does not exist"));
        }
        boolean isCreatorAnAdmin = isAdmin(creator.get().getRoles());
        Optional<Tag> tag;
        if(isCreatorAnAdmin){
            tag = tagRepository.findById(id);
        }else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: User is not an admin"));
        }

        if(tag.isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }

        tagRepository.delete(tag.get());
        return ResponseEntity.ok(new MessageResponse("Tag deleted"));

    }
}

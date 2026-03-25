package com.example.serviceRest;

import com.example.dto.UserRequestDto;
import com.example.dto.UserResponseDto;
import com.example.serviceApi.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "APIs for managing users")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(summary = "Create a new user", description = "Creates a new user and returns the created user details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<UserResponseDto> createUser(@RequestBody UserRequestDto userRequestDTO){
        UserResponseDto user = userService.createUser(userRequestDTO);

        EntityModel<UserResponseDto> model = EntityModel.of(user);

        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUser(user.getId())).withSelfRel());
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .updateUser(user.getId(), null)).withRel("update"));
        model.add(WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(user.getId()).withRel("delete"));
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getAllUsers(0, 10)).withRel("all-users"));

        return model;
    }

    @Operation(summary = "Update an existing user", description = "Updates user details and returns the updated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public EntityModel<UserResponseDto> updateUser(@PathVariable Long id,
                                                   @RequestBody UserRequestDto userRequestDTO) {
        UserResponseDto user = userService.updateUser(id, userRequestDTO);

        EntityModel<UserResponseDto> model = EntityModel.of(user);

        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUser(id)).withSelfRel());
        model.add(WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(id).withRel("delete"));
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getAllUsers(0, 10)).withRel("all-users"));

        return model;
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
    }

    @Operation(summary = "Get all users", description = "Retrieves a paginated list of users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found")
    })
    @GetMapping
    public CollectionModel<EntityModel<UserResponseDto>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size){
        List<UserResponseDto> users = userService.getAllUsers(page, size);

        List<EntityModel<UserResponseDto>> userModels = users.stream()
                .map(user -> EntityModel.of(user,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                                .getUser(user.getId())).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                                .updateUser(user.getId(), null)).withRel("update"),
                        WebMvcLinkBuilder.linkTo(UserController.class)
                                .slash(user.getId()).withRel("delete")
                ))
                .toList();

        CollectionModel<EntityModel<UserResponseDto>> collectionModel = CollectionModel.of(userModels);

        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getAllUsers(page, size)).withSelfRel());

        return collectionModel;
    }

    @Operation(summary = "Get a user by ID", description = "Retrieves a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public EntityModel<UserResponseDto> getUser(@PathVariable("id") Long userId){
        UserResponseDto user = userService.getUser(userId);

        EntityModel<UserResponseDto> model = EntityModel.of(user);

        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getUser(userId)).withSelfRel());
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .updateUser(userId, null)).withRel("update"));
        model.add(WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(userId).withRel("delete"));
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                .getAllUsers(0, 10)).withRel("all-users"));

        return model;
    }
}

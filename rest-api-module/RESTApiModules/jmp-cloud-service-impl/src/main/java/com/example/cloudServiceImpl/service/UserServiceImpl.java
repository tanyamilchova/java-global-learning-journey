package com.example.cloudServiceImpl.service;

import com.domain.User;
import com.example.cloudServiceImpl.exception.DBException;
import com.example.cloudServiceImpl.repository.UserRepository;
import com.example.cloudServiceImpl.util.Util;
import com.example.dto.UserRequestDto;
import com.example.dto.UserResponseDto;
import com.example.serviceApi.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDTO) {
        Util.validateUser(userRequestDTO);
        try {
            User userToSave = modelMapper.map(userRequestDTO, User.class);

            LOGGER.debug("Start creating user: {}", userToSave);
            User savedUser = userRepository.save(userToSave);
            savedUser.setId(userToSave.getId());
            UserResponseDto userResponseDto = modelMapper.map(savedUser, UserResponseDto.class);
            LOGGER.info("User created successfully: {}", userResponseDto);
            return userResponseDto;
        }catch (DBException exception){
            LOGGER.error( "Cannot create user: {}", userRequestDTO, exception);
            throw exception;
        }
    }


    @Override
    public UserResponseDto updateUser(long userId, UserRequestDto userRequestDTO) {
        Util.validateUser(userRequestDTO);
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow();
        LOGGER.debug("Found user to update: {}", userToUpdate);
        try{
            User mappedUser = modelMapper.map(userRequestDTO, User.class);
            LOGGER.debug("Mapped user for update: {}", mappedUser);

            User updatedUser = userRepository.save(mappedUser);
            LOGGER.debug("Saved updated user: {}", updatedUser);


            UserResponseDto responseDto = modelMapper.map(updatedUser, UserResponseDto.class);
            LOGGER.info("User updated successfully: {}", responseDto);
            return responseDto;
        }catch (DBException exception){
            LOGGER.error( "Cannot update user: {}", userRequestDTO, exception);
            throw exception;
        }
    }

    @Override
    public boolean deleteUser(Long userId) {
        Util.validateId(userId);
        User userToDelete = userRepository.findById(userId)
                .orElseThrow();
        LOGGER.debug("Start deleting an user with id: {}", userId);
        try{
            userRepository.delete(userToDelete);
            LOGGER.info("User deleted successfully with id: {}", userId);
            return true;
        }catch (DBException exception){
            LOGGER.error( "Cannot update user with id : {}", userId, exception);
            throw exception;
        }
    }

    @Override
    public UserResponseDto getUser(Long userId) {
        Util.validateId(userId);
        LOGGER.debug( "Finding a user by id: {}", userId);

        User userToGet = userRepository.findById(userId)
                .orElseThrow(() -> new DBException("Cannot get user with id: " + userId));
        LOGGER.info("User retrieved successfully with id: {}", userId);

        UserResponseDto responseDto = modelMapper.map(userToGet, UserResponseDto.class);
        return responseDto;
    }

    @Override
    public List<UserResponseDto> getAllUsers(int page, int size) {
        Util.validatePagination(page, size);
        LOGGER.debug("Retrieving all users for page {} and size {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        try {
            List<User> users = userRepository.findAll(pageable).getContent();
            LOGGER.debug("Successfully retrieved {} users", users.size());

            List<UserResponseDto> userResponseDtoList = users.stream()
                    .map(user -> {
                       UserResponseDto responseDto =  modelMapper.map(user, UserResponseDto.class);
                       return responseDto;
                    })
                    .collect(Collectors.toList());

            LOGGER.info("Successfully mapped {} users to UserResponseDTO for page {} and size {}",
                    userResponseDtoList.size(), page, size);

            return userResponseDtoList;
        }catch (DBException exception){
            LOGGER.error( "Cannot get all users from page {}, size {}", page, size);
            throw exception;
        }
    }
}

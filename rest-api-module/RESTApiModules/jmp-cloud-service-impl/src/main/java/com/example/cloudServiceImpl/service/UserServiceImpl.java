package com.example.cloudServiceImpl.service;

import com.domain.User;
import com.example.cloudServiceImpl.exception.DBException;
import com.example.cloudServiceImpl.exception.ResourceNotFoundException;
import com.example.cloudServiceImpl.exception.ServiceException;
import com.example.cloudServiceImpl.repository.UserRepository;
import com.example.cloudServiceImpl.util.Util;
import com.example.dto.UserRequestDto;
import com.example.dto.UserResponseDto;
import com.example.serviceApi.UserService;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@NoArgsConstructor
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Transactional
    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDTO) {
        Util.validateUser(userRequestDTO);
        try {
            User userToSave = modelMapper.map(userRequestDTO, User.class);

            log.debug("Start creating user: {}", userToSave);
            User savedUser = userRepository.save(userToSave);
            savedUser.setId(userToSave.getId());
            UserResponseDto userResponseDto = modelMapper.map(savedUser, UserResponseDto.class);
            log.info("User created successfully: {}", userResponseDto);
            return userResponseDto;
        }catch (DBException exception){
            log.error( "Cannot create user: {}", userRequestDTO, exception);
            throw new ServiceException("Failed to create user", exception);
        }
    }


    @Transactional
    @Override
    public UserResponseDto updateUser(long userId, UserRequestDto userRequestDTO) {
        Util.validateUser(userRequestDTO);
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        log.debug("Found user to update: {}", userToUpdate);
        try{
            User mappedUser = modelMapper.map(userRequestDTO, User.class);
            log.debug("Mapped user for update: {}", mappedUser);

            User updatedUser = userRepository.save(mappedUser);
            log.debug("Saved updated user: {}", updatedUser);


            UserResponseDto responseDto = modelMapper.map(updatedUser, UserResponseDto.class);
            log.info("User updated successfully: {}", responseDto);
            return responseDto;
        }catch (DBException exception){
            log.error( "Cannot update user: {}", userRequestDTO, exception);
            throw new ServiceException("Failed to update user", exception);
        }
    }

    @Transactional
    @Override
    public boolean deleteUser(Long userId) {
        Util.validateId(userId);
        User userToDelete = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        log.debug("Start deleting an user with id: {}", userId);
        try{
            userRepository.delete(userToDelete);
            log.info("User deleted successfully with id: {}", userId);
            return true;
        }catch (DBException exception){
            log.error( "Cannot update user with id : {}", userId, exception);
            throw new ServiceException("Failed to delete user", exception);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto getUser(Long userId) {
        Util.validateId(userId);
        log.debug( "Finding a user by id: {}", userId);

        User userToGet = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        log.info("User retrieved successfully with id: {}", userId);

        UserResponseDto responseDto = modelMapper.map(userToGet, UserResponseDto.class);
        return responseDto;
    }

    @Override
    public List<UserResponseDto> getAllUsers(int page, int size) {
        Util.validatePagination(page, size);
        log.debug("Retrieving all users for page {} and size {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        try {
            List<User> users = userRepository.findAll(pageable).getContent();
            log.debug("Successfully retrieved {} users", users.size());

            List<UserResponseDto> userResponseDtoList = users.stream()
                    .map(user -> {
                       UserResponseDto responseDto =  modelMapper.map(user, UserResponseDto.class);
                       return responseDto;
                    })
                    .collect(Collectors.toList());

            log.info("Successfully mapped {} users to UserResponseDTO for page {} and size {}",
                    userResponseDtoList.size(), page, size);

            return userResponseDtoList;
        }catch (Exception exception){
            log.error( "Cannot get all users from page {}, size {}", page, size);
            throw new ServiceException("Failed to retrieve users", exception);
        }
    }
}

package com.example.serviceApi;

import com.example.dto.UserRequestDto;
import com.example.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(UserRequestDto userRequestDTO);

    UserResponseDto updateUser(long id, UserRequestDto userRequestDTO);

    boolean deleteUser(Long userId);

    UserResponseDto getUser(Long userId);

    List<UserResponseDto> getAllUsers(int page, int size);
}

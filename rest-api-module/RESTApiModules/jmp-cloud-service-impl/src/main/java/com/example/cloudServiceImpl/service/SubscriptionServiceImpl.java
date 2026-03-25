package com.example.cloudServiceImpl.service;

import com.domain.Subscription;
import com.domain.User;
import com.example.cloudServiceImpl.exception.DBException;
import com.example.cloudServiceImpl.repository.SubscriptionRepository;
import com.example.cloudServiceImpl.repository.UserRepository;
import com.example.cloudServiceImpl.util.Util;
import com.example.dto.SubscriptionRequestDto;
import com.example.dto.SubscriptionResponseDto;
import com.example.serviceApi.SubscriptionService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionServiceImpl.class);

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public SubscriptionResponseDto createSubscription(SubscriptionRequestDto subscriptionRequestDto) {
        Util.validateSubscription(subscriptionRequestDto);
        try {
            Subscription subscriptionToSave = modelMapper.map(subscriptionRequestDto, Subscription.class);
            subscriptionToSave.setStartDate(LocalDate.now());

            LOGGER.debug("Start creating subscription: {}", subscriptionToSave);
            Subscription savedSubscription = subscriptionRepository.save(subscriptionToSave);

            SubscriptionResponseDto subscriptionResponseDto = modelMapper.map(savedSubscription, SubscriptionResponseDto.class);

            LOGGER.info("Subscription created successfully: {}", subscriptionResponseDto);
            return subscriptionResponseDto;

        } catch (DBException exception) {
            LOGGER.error("Cannot create subscription: {}", subscriptionRequestDto, exception);
            throw exception;
        }
    }


    @Override
    public SubscriptionResponseDto updateSubscription(SubscriptionRequestDto subscriptionRequestDto) {
        Util.validateSubscription(subscriptionRequestDto);
        Subscription subscriptionToUpdate = subscriptionRepository.findById(subscriptionRequestDto.getId())
                .orElseThrow();
        LOGGER.debug("Found subscription to update: {}", subscriptionToUpdate);

        try{
            Subscription mappedSubscription = modelMapper.map(subscriptionRequestDto, Subscription.class);
            mappedSubscription.setStartDate(LocalDate.now());
            User userToUpdate = userRepository.findById(subscriptionRequestDto.getUserId()).orElseThrow();
            mappedSubscription.setUser(userToUpdate);
            LOGGER.debug("Mapped subscription for update: {}", mappedSubscription);

            Subscription updatedSubscription = subscriptionRepository.save(mappedSubscription);
            LOGGER.info("Subscription updated successfully: {}", updatedSubscription);

            SubscriptionResponseDto responseDto = modelMapper.map(updatedSubscription, SubscriptionResponseDto.class);

            return responseDto;
        }catch (DBException exception){
            LOGGER.error( "Cannot update user: {}", subscriptionRequestDto, exception);
            throw exception;
        }
    }

    @Override
    public SubscriptionResponseDto deleteSubscription(Long subscriptionId) {
        Util.validateId(subscriptionId);
        Subscription subscriptionToDelete = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new DBException("Subscription not found with id: " + subscriptionId));
        LOGGER.debug("Start deleting subscription with id: {}", subscriptionId);

        try {
            subscriptionRepository.delete(subscriptionToDelete);
            LOGGER.info("Subscription deleted successfully with id: {}", subscriptionId);

            return modelMapper.map(subscriptionToDelete, SubscriptionResponseDto.class);
        } catch (DBException exception) {
            LOGGER.error("Cannot delete subscription with id: {}", subscriptionId, exception);
            throw exception;
        }
    }

    @Override
    public SubscriptionResponseDto getSubscription(Long subscriptionId) {
        Util.validateId(subscriptionId);
        LOGGER.debug("Finding a subscription by id: {}", subscriptionId);

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new DBException("Cannot get subscription with id: " + subscriptionId));
        LOGGER.info("Subscription retrieved successfully with id: {}", subscriptionId);

        SubscriptionResponseDto responseDto = modelMapper.map(subscription, SubscriptionResponseDto.class);
        responseDto.setId(subscriptionId);
        return responseDto;
    }

    @Override
    public List<SubscriptionResponseDto> getAllSubscription(int page, int size) {
        Util.validatePagination(page, size);
        LOGGER.debug("Retrieving all subscriptions for page {} and size {}", page, size);

        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Subscription> subscriptions = subscriptionRepository.findAll(pageable).getContent();
            LOGGER.debug("Successfully retrieved {} subscriptions", subscriptions.size());

            List<SubscriptionResponseDto> subscriptionResponseDtoList = subscriptions.stream()
                    .map(subscription -> {
                     SubscriptionResponseDto dto =  modelMapper.map(subscription, SubscriptionResponseDto.class);
                        dto.setStartDate(LocalDate.now());
                        dto.setId(subscription.getId());
                        return dto;
                    })

                    .collect(Collectors.toList());
            LOGGER.info("Successfully mapped {} subscriptions to SubscriptionResponseDto for page {} and size {}",
                    subscriptionResponseDtoList.size(), page, size);

            return subscriptionResponseDtoList;
        } catch (DBException exception) {
            LOGGER.error("Cannot get all subscriptions for page {}, size {}", page, size, exception);
            throw exception;
        }
    }
}

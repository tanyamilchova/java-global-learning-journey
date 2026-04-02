package com.example.cloudServiceImpl.service;

import com.domain.Subscription;
import com.domain.User;
import com.example.cloudServiceImpl.exception.DBException;
import com.example.cloudServiceImpl.exception.ResourceNotFoundException;
import com.example.cloudServiceImpl.exception.ServiceException;
import com.example.cloudServiceImpl.repository.SubscriptionRepository;
import com.example.cloudServiceImpl.repository.UserRepository;
import com.example.cloudServiceImpl.util.Util;
import com.example.dto.SubscriptionRequestDto;
import com.example.dto.SubscriptionResponseDto;
import com.example.serviceApi.SubscriptionService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    @Override
    public SubscriptionResponseDto createSubscription(SubscriptionRequestDto subscriptionRequestDto) {
        Util.validateSubscription(subscriptionRequestDto);
        try {
            Subscription subscriptionToSave = modelMapper.map(subscriptionRequestDto, Subscription.class);
            subscriptionToSave.setStartDate(LocalDate.now());

            log.debug("Start creating subscription: {}", subscriptionToSave);
            Subscription savedSubscription = subscriptionRepository.save(subscriptionToSave);

            SubscriptionResponseDto subscriptionResponseDto = modelMapper.map(savedSubscription, SubscriptionResponseDto.class);

            log.info("Subscription created successfully: {}", subscriptionResponseDto);
            return subscriptionResponseDto;

        } catch (DBException exception) {
            log.error("Cannot create subscription: {}", subscriptionRequestDto, exception);
            throw new ServiceException("Failed to create subscription", exception);
        }
    }


    @Transactional
    @Override
    public SubscriptionResponseDto updateSubscription(SubscriptionRequestDto subscriptionRequestDto) {
        Util.validateSubscription(subscriptionRequestDto);
        Subscription subscriptionToUpdate = subscriptionRepository.findById(subscriptionRequestDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subscription not found with id: " + subscriptionRequestDto.getId()
                ));
        log.debug("Found subscription to update: {}", subscriptionToUpdate);

        try{
            Subscription mappedSubscription = modelMapper.map(subscriptionRequestDto, Subscription.class);
            mappedSubscription.setStartDate(LocalDate.now());
            User userToUpdate = userRepository.findById(subscriptionRequestDto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "User not found with id: " + subscriptionRequestDto.getUserId()
                    ));
            mappedSubscription.setUser(userToUpdate);
            log.debug("Mapped subscription for update: {}", mappedSubscription);

            Subscription updatedSubscription = subscriptionRepository.save(mappedSubscription);
            log.info("Subscription updated successfully: {}", updatedSubscription);

            SubscriptionResponseDto responseDto = modelMapper.map(updatedSubscription, SubscriptionResponseDto.class);

            return responseDto;
        }catch (DBException exception){
            log.error( "Cannot update user: {}", subscriptionRequestDto, exception);
            throw new ServiceException("Failed to update subscription", exception);
        }
    }

    @Transactional
    @Override
    public SubscriptionResponseDto deleteSubscription(Long subscriptionId) {
        Util.validateId(subscriptionId);
        Subscription subscriptionToDelete = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subscription not found with id: " + subscriptionId
                ));
        log.debug("Start deleting subscription with id: {}", subscriptionId);

        try {
            subscriptionRepository.delete(subscriptionToDelete);
            log.info("Subscription deleted successfully with id: {}", subscriptionId);

            return modelMapper.map(subscriptionToDelete, SubscriptionResponseDto.class);
        } catch (DBException exception) {
            log.error("Cannot delete subscription with id: {}", subscriptionId, exception);
            throw new ServiceException("Failed to delete subscription", exception);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public SubscriptionResponseDto getSubscription(Long subscriptionId) {
        Util.validateId(subscriptionId);
        log.debug("Finding a subscription by id: {}", subscriptionId);

        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subscription not found with id: " + subscriptionId
                ));
        log.info("Subscription retrieved successfully with id: {}", subscriptionId);

        SubscriptionResponseDto responseDto = modelMapper.map(subscription, SubscriptionResponseDto.class);
        responseDto.setId(subscriptionId);
        return responseDto;
    }

    @Override
    public List<SubscriptionResponseDto> getAllSubscription(int page, int size) {
        Util.validatePagination(page, size);
        log.debug("Retrieving all subscriptions for page {} and size {}", page, size);

        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Subscription> subscriptions = subscriptionRepository.findAll(pageable).getContent();
            log.debug("Successfully retrieved {} subscriptions", subscriptions.size());

            List<SubscriptionResponseDto> subscriptionResponseDtoList = subscriptions.stream()
                    .map(subscription -> {
                     SubscriptionResponseDto dto =  modelMapper.map(subscription, SubscriptionResponseDto.class);
                        dto.setStartDate(LocalDate.now());
                        dto.setId(subscription.getId());
                        return dto;
                    })

                    .collect(Collectors.toList());
            log.info("Successfully mapped {} subscriptions to SubscriptionResponseDto for page {} and size {}",
                    subscriptionResponseDtoList.size(), page, size);

            return subscriptionResponseDtoList;
        } catch (DBException exception) {
            log.error("Cannot get all subscriptions for page {}, size {}", page, size, exception);
            throw new ServiceException("Failed to retrieve subscriptions", exception);
        }
    }
}

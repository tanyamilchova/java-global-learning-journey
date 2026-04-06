package com.example.serviceApi;

import com.example.dto.SubscriptionRequestDto;
import com.example.dto.SubscriptionResponseDto;

import java.util.List;

public interface SubscriptionService {

    SubscriptionResponseDto createSubscription(SubscriptionRequestDto subscriptionRequestDto);

    SubscriptionResponseDto updateSubscription(SubscriptionRequestDto subscriptionRequestDto);

    SubscriptionResponseDto deleteSubscription(Long subscriptionId);

    SubscriptionResponseDto getSubscription(Long subscriptionId);

    List<SubscriptionResponseDto> getAllSubscription(int page, int size);

}

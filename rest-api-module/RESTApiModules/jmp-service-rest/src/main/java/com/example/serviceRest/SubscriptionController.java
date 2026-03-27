package com.example.serviceRest;

import com.example.dto.SubscriptionRequestDto;
import com.example.dto.SubscriptionResponseDto;
import com.example.serviceApi.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/subscription")
@RestController
@Tag(name = "Subscription Controller", description = "APIs for managing subscriptions")
public class SubscriptionController {

    @Autowired
    SubscriptionService subscriptionService;


    @Operation(summary = "Create a new subscription", description = "Creates a new subscription and returns the created subscription details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Subscription created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubscriptionResponseDto createSubscription(@RequestBody SubscriptionRequestDto subscriptionRequestDto){
        return subscriptionService.createSubscription(subscriptionRequestDto);
    }


    @Operation(summary = "Update an existing subscription", description = "Updates subscription details and returns the updated subscription.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription updated successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    @PutMapping
    public SubscriptionResponseDto updateSubscription(@RequestBody SubscriptionRequestDto subscriptionRequestDto){
        return subscriptionService.updateSubscription(subscriptionRequestDto);
    }


    @Operation(summary = "Delete a subscription", description = "Deletes a subscription by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Subscription deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public SubscriptionResponseDto deleteSubscription(@PathVariable("id") Long subscriptionId){
        return subscriptionService.deleteSubscription(subscriptionId);
    }


    @Operation(summary = "Get a subscription by ID", description = "Retrieves a subscription by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscription found"),
            @ApiResponse(responseCode = "404", description = "Subscription not found")
    })
    @GetMapping("/{id}")
    public SubscriptionResponseDto getSubscription(@PathVariable("id") Long subscriptionId){
        return subscriptionService.getSubscription(subscriptionId);
    }


    @Operation(summary = "Get all subscriptions", description = "Retrieves a paginated list of all subscriptions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subscriptions retrieved successfully")
    })
    @GetMapping
    public List<SubscriptionResponseDto> getAllSubscription(@RequestParam int page, @RequestParam int size){
        return subscriptionService.getAllSubscription(page, size);
    }
}

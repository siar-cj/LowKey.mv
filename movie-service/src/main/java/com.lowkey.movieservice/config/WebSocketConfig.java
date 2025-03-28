package com.lowkey.movieservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Define WebSocket endpoint
        registry.addEndpoint("/ws") // WebSocket connection endpoint
                .setAllowedOrigins("*") // Allow connections from any origin
                .withSockJS(); // Fallback option for browsers that don’t support WebSocket
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Configure message broker
        registry.enableSimpleBroker("/topic"); // Prefix for messages sent to clients (subscriptions)
        registry.setApplicationDestinationPrefixes("/app"); // Prefix for messages sent from clients to server
    }
}
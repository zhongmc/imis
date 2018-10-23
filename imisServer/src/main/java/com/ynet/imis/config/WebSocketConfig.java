/**
* WebSocketConfig.java
* @author ZHONGMC
* @description 
* @created Sat Sep 22 2018 09:37:26 GMT+0800 (中国标准时间)
* @copyright YNET
* @last-modified Thu Sep 13 2018 11:25:32 GMT+0800 (中国标准时间)
*/

package com.ynet.imis.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;



@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override 
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/queue", "/topic");
    } 
    
    @Override 
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/endpointChat").withSockJS();
    } 
}


// public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
//     @Override
//     public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
//         stompEndpointRegistry.addEndpoint("/ws/endpointChat").withSockJS();
//     }

//     @Override
//     public void configureMessageBroker(MessageBrokerRegistry registry) {
//         registry.enableSimpleBroker("/queue","/topic");
//     }
// }

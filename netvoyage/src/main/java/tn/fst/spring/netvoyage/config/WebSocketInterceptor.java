package tn.fst.spring.netvoyage.config;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import tn.fst.spring.netvoyage.services.interfaces.ICustomUserDetailsService;
import tn.fst.spring.netvoyage.utils.JWTUtils;

@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {

    private final JWTUtils jwtUtils;
    private final ICustomUserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // Remove "Bearer "

                if (jwtUtils.validateToken(token)) { // 🟰 validateToken already exists
                    String username = jwtUtils.getUsernameFromToken(token); // 🟰 getUsernameFromToken already exists
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    accessor.setUser(authentication);
                } else {
                    throw new RuntimeException("Invalid Token for WebSocket Connection");
                }
            } else {
                throw new RuntimeException("No Authorization Header found for WebSocket Connection");
            }
        }

        return message;
    }
}

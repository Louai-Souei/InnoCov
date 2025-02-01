package covoiturage.project.InnoCov.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            String token = null;

            // Récupérer les en-têtes STOMP
            List<String> authHeaders = (List<String>) servletRequest.getServletRequest().getHeaders("Authorization");
            if (authHeaders != null && !authHeaders.isEmpty()) {
                token = authHeaders.get(0);
            }

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                attributes.put("token", token);
                System.out.println("WebSocket Token: " + token);
            } else {
                System.out.println("WebSocket handshake failed: Missing token");
                return false; // Bloquer la connexion si le token est manquant ou invalide
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // Rien à faire ici
    }
}

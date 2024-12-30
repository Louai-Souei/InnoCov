package covoiturage.project.InnoCov.controller;

import covoiturage.project.InnoCov.entity.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.Date;

@Controller
public class WebSocketController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public Message sendMessage(Message message) {
        // Ajout d'une validation simple pour vérifier que le message n'est pas nul
        System.out.println(message);
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null");
        }

        message.setSentAt(new Date());
        return message;
    }
}

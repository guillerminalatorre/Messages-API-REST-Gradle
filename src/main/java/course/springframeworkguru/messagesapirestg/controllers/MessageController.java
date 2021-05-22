package course.springframeworkguru.messagesapirestg.controllers;

import course.springframeworkguru.messagesapirestg.dto.input.MessageDto;
import course.springframeworkguru.messagesapirestg.exceptions.MessageException;
import course.springframeworkguru.messagesapirestg.exceptions.RecipientException;
import course.springframeworkguru.messagesapirestg.models.Message;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.services.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    public Page<Message> findMessagesByUserFrom(int idUser, Pageable pageable){

        return this.messageService.findByUserFromId(idUser, pageable);
    }

    public Page<Message> findMessagesSentByLabel(int idUser, int idLabel, Pageable pageable){

        return this.messageService.findByUserFromIdAndLabel(idUser, idLabel, pageable);
    }

    public Page<Message> findByRecipientId(int idUser, Pageable pageable){

        return this.messageService.findByRecipientId(idUser, pageable);
    }

    public Page<Message> findMessagesInboxByLabel(int idUser, int idLabel, Pageable pageable){

        return this.messageService.findByRecipientIdAndLabel(idUser, idLabel, pageable);
    }

    public Message send(MessageDto messageDto, User userFrom) throws MessageException, RecipientException {

        return this.messageService.send(messageDto, userFrom);

    }

}

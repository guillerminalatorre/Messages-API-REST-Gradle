package course.springframeworkguru.messagesapirestg.controllers;

import course.springframeworkguru.messagesapirestg.views.MessageInboxView;
import course.springframeworkguru.messagesapirestg.dto.NewMessageDto;
import course.springframeworkguru.messagesapirestg.exceptions.MessageException;
import course.springframeworkguru.messagesapirestg.exceptions.RecipientException;
import course.springframeworkguru.messagesapirestg.models.Message;
import course.springframeworkguru.messagesapirestg.models.Recipient;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.services.MessageService;
import course.springframeworkguru.messagesapirestg.views.MessageSentView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    public Page<MessageSentView> findMessagesByUserFrom(int idUser, Pageable pageable){
        return this.messageService.findByUserFromId(idUser, pageable);
    }

    public Page<MessageSentView> findMessagesSentByLabel(int idUser, int idLabel, Pageable pageable){
        return this.messageService.findByUserFromIdAndLabel(idUser, idLabel, pageable);
    }

    public Page<MessageInboxView> findByRecipientId(int idUser, Pageable pageable){
        return this.messageService.findByRecipientId(idUser, pageable);
    }

    public Page<MessageInboxView> findMessagesInboxByLabel(int idUser, int idLabel, Pageable pageable){
        return this.messageService.findByRecipientIdAndLabel(idUser, idLabel, pageable);
    }

    public Message send(NewMessageDto newMessageDto, User userFrom) throws MessageException, RecipientException {
        return this.messageService.send(newMessageDto, userFrom);

    }

    public Message deleteSent(int idMessage) throws MessageException {
        return this.messageService.deleteSent(idMessage);
    }

    public Recipient deleteInbox(int idMessage, int idUser) throws MessageException {
        return this.messageService.deleteInbox(idMessage, idUser);
    }

}

package course.springframeworkguru.messagesapirestg.controllers;

import course.springframeworkguru.messagesapirestg.dto.NewMessageDto;
import course.springframeworkguru.messagesapirestg.dto.RecipientDto;
import course.springframeworkguru.messagesapirestg.exceptions.MessageException;
import course.springframeworkguru.messagesapirestg.exceptions.RecipientException;
import course.springframeworkguru.messagesapirestg.models.Message;
import course.springframeworkguru.messagesapirestg.models.Recipient;
import course.springframeworkguru.messagesapirestg.models.RecipientType;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.models.employees.Employee;
import course.springframeworkguru.messagesapirestg.services.MessageService;
import course.springframeworkguru.messagesapirestg.utils.ObjectsFactory;
import course.springframeworkguru.messagesapirestg.views.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MessageControllerTest {

    @Mock
    private MessageService messageService;

    private MessageController messageController;

    private ObjectsFactory objectFactory = new ObjectsFactory();

    private ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.messageController = new MessageController(messageService);
    }

    @Test
    public void findMessagesByUserFrom() {
        Message message = objectFactory.createMessage();

        MessageView messageView = objectFactory.createMessageView();

        List<MessageView> messageViewList =  new ArrayList<MessageView>();
        messageViewList.add(messageView);

        Page<MessageView> page = new PageImpl<>(messageViewList);

        when(this.messageService.findByUserFromId(message.getUserFrom().getId(), PageRequest.of(0,1))).thenReturn(page);

        Page<MessageView> page1 = this.messageController.findMessagesByUserFrom(message.getUserFrom().getId(), PageRequest.of(0,1));

        Assert.assertEquals(page, page1);
    }

    @Test
    public void findMessagesSentByLabel() {

        Message message = objectFactory.createMessage();

        MessageView messageView = objectFactory.createMessageView();

        List<MessageView> messageViewList =  new ArrayList<MessageView>();
        messageViewList.add(messageView);

        Page<MessageView> page = new PageImpl<>(messageViewList);

        when(this.messageService.findByUserFromIdAndLabel(message.getRecipientList().get(0).getUser().getId(), 1,PageRequest.of(0,1)))
                .thenReturn(page);

        Page<MessageView> page1 = this.messageController.findMessagesSentByLabel(message.getRecipientList().get(0).getUser().getId(), 1,PageRequest.of(0,1));

        Assert.assertEquals(page, page1);
    }

    @Test
    public void findByRecipientId() {
        Message message = objectFactory.createMessage();

        MessageView messageView = objectFactory.createMessageView();

        List<MessageView> messageViewList =  new ArrayList<MessageView>();
        messageViewList.add(messageView);

        Page<MessageView> page = new PageImpl<>(messageViewList);

        when(this.messageService.findByRecipientId(message.getRecipientList().get(0).getUser().getId(), PageRequest.of(0,1)))
                .thenReturn(page);

        Page<MessageView> page1 = this.messageController.findByRecipientId(message.getRecipientList().get(0).getUser().getId(), PageRequest.of(0,1));

        Assert.assertEquals(page, page1);
    }

    @Test
    public void findMessagesInboxByLabel() {
        Message message = objectFactory.createMessage();

        List<MessageView> messageViewList =  new ArrayList<MessageView>();

        Page<MessageView> page = new PageImpl<>(messageViewList);

        when(this.messageService.findByRecipientIdAndLabel(message.getRecipientList().get(0).getUser().getId(), 1,PageRequest.of(0,1))).thenReturn(page);

        Page<MessageView> page1 = this.messageController.findMessagesInboxByLabel(message.getRecipientList().get(0).getUser().getId(), 1,PageRequest.of(0,1));

        Assert.assertEquals(page, page1);
    }

    @Test
    public void sendOk() throws MessageException, RecipientException {
        NewMessageDto newMessageDto =  objectFactory.createNewMessageDto();

        User user= objectFactory.createUser();

        Message message = objectFactory.createMessage();
        message.setRecipientList(null);

        when(this.messageService.send(newMessageDto, user)).thenReturn(message);

        Message message1 = this.messageController.send(newMessageDto, user);

        Assert.assertEquals(message,message1);
    }

    @Test(expected = MessageException.class)
    public void sendFail1() throws MessageException, RecipientException {
        NewMessageDto newMessageDto =  objectFactory.createNewMessageDto();

        User user= objectFactory.createUser();

        Message message = objectFactory.createMessage();
        message.setRecipientList(null);

        when(this.messageService.send(newMessageDto, user)).thenThrow(MessageException.class);

        Message message1 = this.messageController.send(newMessageDto, user);
    }

    @Test(expected = RecipientException.class)
    public void sendFail2() throws MessageException, RecipientException {
        NewMessageDto newMessageDto =  objectFactory.createNewMessageDto();

        User user= objectFactory.createUser();

        Message message = objectFactory.createMessage();
        message.setRecipientList(null);

        when(this.messageService.send(newMessageDto, user)).thenThrow(RecipientException.class);

        Message message1 = this.messageController.send(newMessageDto, user);
    }

    @Test
    public void deleteSentOk() throws MessageException {
        Message message = objectFactory.createMessage();
        message.setDeletedByUserFrom(true);

        when(this.messageService.deleteSent(message.getId())).thenReturn(message);

        Message message1 = this.messageController.deleteSent(message.getId()) ;

        Assert.assertEquals(message,message1);
    }

    @Test(expected = MessageException.class)
    public void deleteSentFail() throws MessageException {
        Message message = objectFactory.createMessage();
        message.setDeletedByUserFrom(true);

        when(this.messageService.deleteSent(message.getId())).thenThrow(MessageException.class);

        Message message1 = this.messageController.deleteSent(message.getId()) ;
    }

    @Test
    public void deleteInboxOk() throws MessageException {
        Message message = objectFactory.createMessage();
        Recipient recipient = message.getRecipientList().get(0);
        User user = objectFactory.createUser();

        when(this.messageService.deleteInbox(message.getId(),user.getId())).thenReturn(recipient);
        recipient.setDeletedByRecipient(true);

        Recipient recipient1 = this.messageController.deleteInbox(message.getId(),user.getId());

        Assert.assertEquals(recipient,recipient1);
    }

    @Test(expected = MessageException.class)
    public void deleteInboxFail() throws MessageException {
        Message message = objectFactory.createMessage();
        Recipient recipient = message.getRecipientList().get(0);
        User user = objectFactory.createUser();

        when(this.messageService.deleteInbox(message.getId(),user.getId())).thenThrow(MessageException.class);

        Recipient recipient1 = this.messageController.deleteInbox(message.getId(),user.getId());
    }
}
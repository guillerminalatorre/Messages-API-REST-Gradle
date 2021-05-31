package course.springframeworkguru.messagesapirestg.services;

import course.springframeworkguru.messagesapirestg.dto.NewMessageDto;
import course.springframeworkguru.messagesapirestg.exceptions.MessageException;
import course.springframeworkguru.messagesapirestg.exceptions.RecipientException;
import course.springframeworkguru.messagesapirestg.models.*;
import course.springframeworkguru.messagesapirestg.repositories.*;
import course.springframeworkguru.messagesapirestg.utils.ObjectsFactory;
import course.springframeworkguru.messagesapirestg.views.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RecipientTypeRepository recipientTypeRepository;
    @Mock
    private RecipientRepository recipientRepository;
    @Mock
    private AttachmentRepository attachmentRepository;

    private MessageService messageService;

    private ObjectsFactory objectsFactory = new ObjectsFactory();


    private ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        messageService = new MessageService(this.messageRepository,
                this.userRepository, this.recipientTypeRepository,
                this.recipientRepository, this.attachmentRepository);
    }

    @Test
    public void findByUserFromId() {
        Message message = objectsFactory.createMessage();

        MessageView messageView = objectsFactory.createMessageView();

        List<MessageView> messageViewList =  new ArrayList<MessageView>();
        messageViewList.add(messageView);

        Page<MessageView> page = new PageImpl<>(messageViewList);

        when(this.messageRepository.findDistinctByUserFromIdAndIsDeletedByUserFromFalse(message.getUserFrom().getId(), PageRequest.of(0,1)))
                .thenReturn(page);

        Page<MessageView> page1 = this.messageService.findByUserFromId(message.getUserFrom().getId(), PageRequest.of(0,1));

        Assert.assertEquals(page, page1);
    }

    @Test
    public void findByRecipientId() {
        Message message = objectsFactory.createMessage();

        MessageView messageView = objectsFactory.createMessageView();

        List<MessageView> messageViewList =  new ArrayList<MessageView>();
        messageViewList.add(messageView);

        Page<MessageView> page = new PageImpl<>(messageViewList);

        when(this.messageRepository.findDistinctByRecipientListUserIdAndRecipientListIsDeletedByRecipientFalse(message.getRecipientList().get(0).getUser().getId(), PageRequest.of(0,1)))
                .thenReturn(page);

        Page<MessageView> page1 = this.messageService.findByRecipientId(message.getRecipientList().get(0).getUser().getId(), PageRequest.of(0,1));

        Assert.assertEquals(page, page1);
    }

    @Test
    public void findByRecipientIdAndLabel() {
        Message message = objectsFactory.createMessage();

        List<MessageView> messageViewList =  new ArrayList<MessageView>();

        Page<MessageView> page = new PageImpl<>(messageViewList);

        when(this.messageRepository.findDistinctByRecipientListUserIdAndLabelXMessageListUserIdAndLabelXMessageListLabelIdAndLabelXMessageListLabelIsEnabledTrueAndRecipientListIsDeletedByRecipientFalse
                (message.getRecipientList().get(0).getUser().getId(), message.getRecipientList().get(0).getUser().getId(), 1, PageRequest.of(0,1)))
                .thenReturn(page);

        Page<MessageView> page1 = this.messageService.findByRecipientIdAndLabel(message.getRecipientList().get(0).getUser().getId(), 1,PageRequest.of(0,1));

        Assert.assertEquals(page, page1);
    }

    @Test
    public void findByUserFromIdAndLabel() {

        Message message = objectsFactory.createMessage();

        List<MessageView> messageViewList =  new ArrayList<MessageView>();

        Page<MessageView> page = new PageImpl<>(messageViewList);

        when(this.messageRepository.findDistinctByUserFromIdAndLabelXMessageListUserIdAndLabelXMessageListLabelIdAndLabelXMessageListLabelIsEnabledTrueAndIsDeletedByUserFromFalse
                (message.getRecipientList().get(0).getUser().getId(), message.getRecipientList().get(0).getUser().getId(), 1, PageRequest.of(0,1)))
                .thenReturn(page);

        Page<MessageView> page1 = this.messageService.findByUserFromIdAndLabel(message.getRecipientList().get(0).getUser().getId(), 1,PageRequest.of(0,1));

        Assert.assertEquals(page, page1);
    }

    @Test
    public void sendOk() throws MessageException, RecipientException {
        NewMessageDto newMessageDto =  objectsFactory.createNewMessageDto();

        User user= objectsFactory.createUser();

        Message auxMessage = new Message();
        auxMessage.setBody(newMessageDto.getBody());
        auxMessage.setSubject(newMessageDto.getSubject());
        auxMessage.setUserFrom(user);

        Message message = objectsFactory.createMessage();
        message.setRecipientList(null);

        when(this.messageRepository.save(auxMessage)).thenReturn(message);
        when(this.attachmentRepository.saveAll(message.getAttachmentsList())).thenReturn(message.getAttachmentsList());
        when(this.userRepository.findByEmployeeMailUsernameAndIsEnabledTrue(newMessageDto.getRecipients().get(0).getMailUsername())).thenReturn(user);
        when(this.recipientTypeRepository.findByAcronym(objectsFactory.createRecipientType().getAcronym())).thenReturn(objectsFactory.createRecipientType());
        when(this.recipientRepository.saveAll(message.getRecipientList())).thenReturn(message.getRecipientList());

        Message message1 = this.messageService.send(newMessageDto, user);

        Assert.assertEquals(message,message1);
    }

    @Test(expected = MessageException.class)
    public void sendFail1() throws RecipientException, MessageException {
        NewMessageDto newMessageDto =  objectsFactory.createNewMessageDto();

        User user= objectsFactory.createUser();

        Message message = objectsFactory.createMessage();

        when(this.messageRepository.save(message)).thenReturn(null);

        Message message1 = this.messageService.send(newMessageDto, user);

        Assert.assertEquals(message,message1);
    }

    @Test(expected = RecipientException.class)
    public void sendFail2() throws RecipientException, MessageException {
        NewMessageDto newMessageDto =  objectsFactory.createNewMessageDto();

        User user= objectsFactory.createUser();

        Message auxMessage = new Message();
        auxMessage.setBody(newMessageDto.getBody());
        auxMessage.setSubject(newMessageDto.getSubject());
        auxMessage.setUserFrom(user);

        Message message = objectsFactory.createMessage();
        message.setRecipientList(null);

        when(this.messageRepository.save(auxMessage)).thenReturn(message);
        when(this.attachmentRepository.saveAll(message.getAttachmentsList())).thenReturn(message.getAttachmentsList());
        when(this.userRepository.findByEmployeeMailUsernameAndIsEnabledTrue(newMessageDto.getRecipients().get(0).getMailUsername())).thenReturn(null);

        Message message1 = this.messageService.send(newMessageDto, user);

        Assert.assertEquals(message,message1);
    }

    @Test
    public void deleteSentOk() throws MessageException {
        Message message = objectsFactory.createMessage();
        message.setDeletedByUserFrom(true);

        when(this.messageRepository.findById(message.getId())).thenReturn(message);
        when(this.messageRepository.save(message)).thenReturn(message);

        Message message1 = this.messageService.deleteSent(message.getId());

        Assert.assertEquals(message,message1);
    }

    @Test(expected = MessageException.class)
    public void deleteSentFail() throws MessageException {
        Message message = objectsFactory.createMessage();
        message.setDeletedByUserFrom(true);

        when(this.messageRepository.findById(message.getId())).thenReturn(null);

        Message message1 = this.messageService.deleteSent(message.getId());
    }

    @Test
    public void deleteInboxOk() throws MessageException {
        Message message = objectsFactory.createMessage();
        Recipient recipient = message.getRecipientList().get(0);
        User user = objectsFactory.createUser();

        when(this.recipientRepository.findByMessageIdAndUserId(message.getId(), user.getId())).thenReturn(recipient);
        when(this.recipientRepository.save(recipient)).thenReturn(recipient);

        recipient.setDeletedByRecipient(true);

        Recipient recipient1 = this.messageService.deleteInbox(message.getId(),user.getId());

        Assert.assertEquals(recipient,recipient1);
    }

    @Test(expected = MessageException.class)
    public void deleteInboxFail() throws MessageException {
        Message message = objectsFactory.createMessage();
        Recipient recipient = message.getRecipientList().get(0);
        User user = objectsFactory.createUser();

        when(this.recipientRepository.findByMessageIdAndUserId(message.getId(), user.getId())).thenReturn(null);

        Recipient recipient1 = this.messageService.deleteInbox(message.getId(),user.getId());
    }
}
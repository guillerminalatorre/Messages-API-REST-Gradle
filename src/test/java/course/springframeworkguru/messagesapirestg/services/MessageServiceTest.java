package course.springframeworkguru.messagesapirestg.services;

import course.springframeworkguru.messagesapirestg.dto.NewMessageDto;
import course.springframeworkguru.messagesapirestg.dto.RecipientDto;
import course.springframeworkguru.messagesapirestg.exceptions.MessageException;
import course.springframeworkguru.messagesapirestg.exceptions.RecipientException;
import course.springframeworkguru.messagesapirestg.models.*;
import course.springframeworkguru.messagesapirestg.models.employees.Employee;
import course.springframeworkguru.messagesapirestg.repositories.*;
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

    private ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        messageService = new MessageService(this.messageRepository,
                this.userRepository, this.recipientTypeRepository,
                this.recipientRepository, this.attachmentRepository);
    }

    private Employee createEmployee(){
        return Employee.builder()
                .id(1L)
                .mailUsername("pepe")
                .idNumber("1234567")
                .lastName("perez")
                .name("jose")
                .city(null)
                .build();
    }

    private User createUser(){
        return User.builder()
                .id(0)
                .username("jose perez")
                .isAdmin(false)
                .isEnabled(true)
                .password("1234")
                .employee(createEmployee())
                .build();
    }

    private RecipientType createRecipientType(){
        return RecipientType.builder()
                .id(1)
                .name("Primary Receptor")
                .acronym("To").build();
    }

    private Recipient createRecipient(Message message){
        return Recipient.builder().id(1)
                .message(message)
                .recipientType(createRecipientType())
                .isDeletedByRecipient(false)
                .user(createUser())
                .build();
    }

    private Message createMessage(){
        Message message = Message.builder()
                .id(0)
                .body("Body Message")
                .subject("Subject Message")
                .labelXMessageList(null)
                .isDeletedByUserFrom(false)
                .userFrom(createUser())
                .recipientList(null)
                .attachmentsList(null)
                .build();

        List<Recipient> recipientList = new ArrayList<Recipient>();
        recipientList.add(createRecipient(message));

        message.setRecipientList(recipientList);

        return message;
    }

    private MessageView createMessageView(){
        Employee employee = createEmployee();
        EmployeeView employeeView = this.factory.createProjection(EmployeeView.class);
        employeeView.setMailUsername(employee.getMailUsername());

        User user = createUser();
        UserView userView = this.factory.createProjection(UserView.class);
        userView.setUsername(user.getUsername());
        userView.setEmployee(employeeView);

        Message message = createMessage();

        RecipientTypeView recipientTypeView = this.factory.createProjection(RecipientTypeView.class);
        recipientTypeView.setAcronym(message.getRecipientList()
                .get(0).getRecipientType().getAcronym());

        RecipientView recipientView = this.factory.createProjection(RecipientView.class);
        recipientView.setUser(userView);
        recipientView.setRecipientType(recipientTypeView);

        List<RecipientView> recipientViewList =  new ArrayList<RecipientView>();
        recipientViewList.add(recipientView);

        MessageView messageView = this.factory.createProjection(MessageView.class);
        messageView.setId(message.getId());
        messageView.setDatee(message.getDatee().toString());
        messageView.setUserFrom(userView);
        messageView.setAttachmentsList(null);
        messageView.setBody(message.getBody());
        messageView.setSubject(message.getSubject());
        messageView.setRecipientList(recipientViewList);

        return messageView;
    }

    private RecipientDto createRecipientDto(){
        return RecipientDto.builder().acronymRecipientType("To").mailUsername("pepe").build();
    }

    private NewMessageDto createNewMessageDto(){
        List<RecipientDto> recipientDtos = new ArrayList<RecipientDto>();
        recipientDtos.add(createRecipientDto());

        return NewMessageDto.builder()
                .body("Body Message")
                .subject("Subject Message")
                .attachments(null)
                .recipients(recipientDtos)
                .build();
    }

    @Test
    public void findByUserFromId() {
        Message message = createMessage();

        MessageView messageView = createMessageView();

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
        Message message = createMessage();

        MessageView messageView = createMessageView();

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
        Message message = createMessage();

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

        Message message = createMessage();

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
        NewMessageDto newMessageDto =  createNewMessageDto();

        User user= createUser();

        Message auxMessage = new Message();
        auxMessage.setBody(newMessageDto.getBody());
        auxMessage.setSubject(newMessageDto.getSubject());
        auxMessage.setUserFrom(user);

        Message message = createMessage();
        message.setRecipientList(null);

        when(this.messageRepository.save(auxMessage)).thenReturn(message);
        when(this.attachmentRepository.saveAll(message.getAttachmentsList())).thenReturn(message.getAttachmentsList());
        when(this.userRepository.findByEmployeeMailUsernameAndIsEnabledTrue(newMessageDto.getRecipients().get(0).getMailUsername())).thenReturn(user);
        when(this.recipientTypeRepository.findByAcronym(createRecipientType().getAcronym())).thenReturn(createRecipientType());
        when(this.recipientRepository.saveAll(message.getRecipientList())).thenReturn(message.getRecipientList());

        Message message1 = this.messageService.send(newMessageDto, user);

        Assert.assertEquals(message,message1);
    }

    @Test(expected = MessageException.class)
    public void sendFail1() throws RecipientException, MessageException {
        NewMessageDto newMessageDto =  createNewMessageDto();

        User user= createUser();

        Message message = createMessage();

        when(this.messageRepository.save(message)).thenReturn(null);

        Message message1 = this.messageService.send(newMessageDto, user);

        Assert.assertEquals(message,message1);
    }

    @Test(expected = RecipientException.class)
    public void sendFail2() throws RecipientException, MessageException {
        NewMessageDto newMessageDto =  createNewMessageDto();

        User user= createUser();

        Message auxMessage = new Message();
        auxMessage.setBody(newMessageDto.getBody());
        auxMessage.setSubject(newMessageDto.getSubject());
        auxMessage.setUserFrom(user);

        Message message = createMessage();
        message.setRecipientList(null);

        when(this.messageRepository.save(auxMessage)).thenReturn(message);
        when(this.attachmentRepository.saveAll(message.getAttachmentsList())).thenReturn(message.getAttachmentsList());
        when(this.userRepository.findByEmployeeMailUsernameAndIsEnabledTrue(newMessageDto.getRecipients().get(0).getMailUsername())).thenReturn(null);

        Message message1 = this.messageService.send(newMessageDto, user);

        Assert.assertEquals(message,message1);
    }

    @Test
    public void deleteSentOk() throws MessageException {
        Message message = createMessage();
        message.setDeletedByUserFrom(true);

        when(this.messageRepository.findById(message.getId())).thenReturn(message);
        when(this.messageRepository.save(message)).thenReturn(message);

        Message message1 = this.messageService.deleteSent(message.getId());

        Assert.assertEquals(message,message1);
    }

    @Test(expected = MessageException.class)
    public void deleteSentFail() throws MessageException {
        Message message = createMessage();
        message.setDeletedByUserFrom(true);

        when(this.messageRepository.findById(message.getId())).thenReturn(null);

        Message message1 = this.messageService.deleteSent(message.getId());
    }

    @Test
    public void deleteInboxOk() throws MessageException {
        Message message = createMessage();
        Recipient recipient = message.getRecipientList().get(0);
        User user = createUser();

        when(this.recipientRepository.findByMessageIdAndUserId(message.getId(), user.getId())).thenReturn(recipient);
        when(this.recipientRepository.save(recipient)).thenReturn(recipient);

        recipient.setDeletedByRecipient(true);

        Recipient recipient1 = this.messageService.deleteInbox(message.getId(),user.getId());

        Assert.assertEquals(recipient,recipient1);
    }

    @Test(expected = MessageException.class)
    public void deleteInboxFail() throws MessageException {
        Message message = createMessage();
        Recipient recipient = message.getRecipientList().get(0);
        User user = createUser();

        when(this.recipientRepository.findByMessageIdAndUserId(message.getId(), user.getId())).thenReturn(null);

        Recipient recipient1 = this.messageService.deleteInbox(message.getId(),user.getId());
    }
}
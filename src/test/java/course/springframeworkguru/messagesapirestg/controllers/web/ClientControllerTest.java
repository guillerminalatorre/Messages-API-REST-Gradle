package course.springframeworkguru.messagesapirestg.controllers.web;

import course.springframeworkguru.messagesapirestg.controllers.LabelController;
import course.springframeworkguru.messagesapirestg.controllers.MessageController;
import course.springframeworkguru.messagesapirestg.controllers.UserController;
import course.springframeworkguru.messagesapirestg.dto.*;
import course.springframeworkguru.messagesapirestg.exceptions.LabelException;
import course.springframeworkguru.messagesapirestg.exceptions.MessageException;
import course.springframeworkguru.messagesapirestg.exceptions.RecipientException;
import course.springframeworkguru.messagesapirestg.exceptions.UserException;
import course.springframeworkguru.messagesapirestg.models.*;
import course.springframeworkguru.messagesapirestg.models.employees.Employee;
import course.springframeworkguru.messagesapirestg.session.SessionManager;
import course.springframeworkguru.messagesapirestg.views.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ClientControllerTest {

    @Mock
    private UserController userController;
    @Mock
    private MessageController messageController;
    @Mock
    private LabelController labelController;

    @Mock
    private SessionManager sessionManager;

    private ClientController clientController;

    private ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.clientController = new ClientController(userController, messageController, labelController,sessionManager);

        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @After
    public void teardown() {
        RequestContextHolder.resetRequestAttributes();
    }

    private NewUserDto createNewUserDto(){
        return NewUserDto.builder()
                .username("jose perez")
                .mailUsername("pepe")
                .password("1234")
                .build();
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

    private Label createLabel(){
        return Label.builder()
                .id(0)
                .name("Work")
                .isEnabled(true)
                .user(createUser())
                .build();
    }

    private LabelXMessage createLabelXMessage(){
        return LabelXMessage.builder()
                .id(0)
                .user(createUser())
                .label(createLabel())
                .message(createMessage())
                .build();
    }

    private LabelView createLabelView(){
        Label label = createLabel();
        LabelView labelView = this.factory.createProjection(LabelView.class);
        labelView.setName(label.getName());
        labelView.setId(label.getId());

        return labelView;
    }

    private LabelXMessageView createLabelXMessageView(){
        LabelXMessageView labelXMessageView = this.factory.createProjection(LabelXMessageView.class);
        labelXMessageView.setLabel(createLabelView());

        return labelXMessageView;
    }

    private NewLabelDto createNewLabelDto(){
        return NewLabelDto.builder().name("Work").build();
    }

    @Test
    public void getInfoOk() throws UserException {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        ResponseEntity responseEntity = ResponseEntity.ok(user);

        ResponseEntity responseEntity1 = this.clientController.getInfo("1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test(expected = UserException.class)
    public void getInfoFail() throws UserException {
        User user = createUser();
        when(sessionManager.getCurrentUser("123")).thenReturn(null);

        ResponseEntity responseEntity1 = this.clientController.getInfo("123");
    }

    @Test
    public void sentByUserFrom() {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = createMessage();

        MessageView messageView = createMessageView();

        List<MessageView> messageViewList =  new ArrayList<MessageView>();
        messageViewList.add(messageView);

        Page<MessageView> page = new PageImpl<>(messageViewList);
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).
                header("total-count", Long.toString(page.getTotalElements())).
                header("total-pages", Long.toString(page.getTotalPages())).
                body(page.getContent());

        when(this.messageController.findMessagesByUserFrom(message.getUserFrom().getId(), PageRequest.of(0,1))).thenReturn(page);

        ResponseEntity responseEntity1 = this.clientController.sentByUserFrom("1234", PageRequest.of(0,1));

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void sentByUserFromAndLabel() {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = createMessage();

        MessageView messageView = createMessageView();

        List<MessageView> messageViewList =  new ArrayList<MessageView>();
        messageViewList.add(messageView);

        Page<MessageView> page = new PageImpl<>(messageViewList);
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).
                header("total-count", Long.toString(page.getTotalElements())).
                header("total-pages", Long.toString(page.getTotalPages())).
                body(page.getContent());

        when(this.messageController.findMessagesSentByLabel(message.getRecipientList().get(0).getUser().getId(), 1,PageRequest.of(0,1))).thenReturn(page);

        ResponseEntity responseEntity1 = this.clientController.sentByUserFromAndLabel(1, "1234", PageRequest.of(0,1));

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void inboxByRecipientId() {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = createMessage();

        MessageView messageView = createMessageView();

        List<MessageView> messageViewList =  new ArrayList<MessageView>();
        messageViewList.add(messageView);

        Page<MessageView> page = new PageImpl<>(messageViewList);
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).
                header("total-count", Long.toString(page.getTotalElements())).
                header("total-pages", Long.toString(page.getTotalPages())).
                body(page.getContent());

        when(this.messageController.findByRecipientId(message.getRecipientList().get(0).getUser().getId(), PageRequest.of(0,1))).thenReturn(page);

        ResponseEntity responseEntity1 = this.clientController.inboxByRecipientId("1234", PageRequest.of(0,1));

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void inboxByRecipientIdAndLabel() {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = createMessage();

        MessageView messageView = createMessageView();

        List<MessageView> messageViewList =  new ArrayList<MessageView>();
        messageViewList.add(messageView);

        Page<MessageView> page = new PageImpl<>(messageViewList);
        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.OK).
                header("total-count", Long.toString(page.getTotalElements())).
                header("total-pages", Long.toString(page.getTotalPages())).
                body(page.getContent());

        when(this.messageController.findMessagesInboxByLabel(message.getRecipientList().get(0).getUser().getId(), 1,PageRequest.of(0,1))).thenReturn(page);

        ResponseEntity responseEntity1 = this.clientController.inboxByRecipientIdAndLabel(1, "1234", PageRequest.of(0,1));

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void findLabelsByMessageId() {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = createMessage();

        List<LabelXMessageView> labels = new ArrayList<LabelXMessageView>();
        labels.add(createLabelXMessageView());

        ResponseEntity responseEntity = new ResponseEntity(labels, HttpStatus.OK);

        when(this.labelController.findLabelsByMessageId(message.getId(),user.getId())).thenReturn(labels);

        ResponseEntity responseEntity1 = this.clientController.findLabelsByMessageId(message.getId(), "1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void findLabelsByUserId() {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = createMessage();

        List<LabelView> labels = new ArrayList<LabelView>();
        labels.add(createLabelView());

        ResponseEntity responseEntity = new ResponseEntity(labels, HttpStatus.OK);

        when(this.labelController.findLabelsByUserId(user.getId())).thenReturn(labels);

        ResponseEntity responseEntity1 = this.clientController.findLabelsByUserId("1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void findUserByMailUsernameLike() {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Employee employee = createEmployee();
        EmployeeView employeeView = this.factory.createProjection(EmployeeView.class);
        employeeView.setMailUsername(employee.getMailUsername());

        UserView userView = this.factory.createProjection(UserView.class);
        userView.setUsername(user.getUsername());
        userView.setEmployee(employeeView);

        List<UserView> usersView = new ArrayList<UserView>();
        usersView.add(userView);

        ResponseEntity responseEntity = new ResponseEntity(usersView, HttpStatus.OK);

        when(this.userController.findByMailUsernameLike(employee.getMailUsername())).thenReturn(usersView);

        ResponseEntity responseEntity1 = this.clientController.findUserByMailUsernameLike(employee.getMailUsername(),"1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void testFindUsers() {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Employee employee = createEmployee();
        EmployeeView employeeView = this.factory.createProjection(EmployeeView.class);
        employeeView.setMailUsername(employee.getMailUsername());

        UserView userView = this.factory.createProjection(UserView.class);
        userView.setUsername(user.getUsername());
        userView.setEmployee(employeeView);

        List<UserView> usersView = new ArrayList<UserView>();
        usersView.add(userView);

        ResponseEntity responseEntity = new ResponseEntity(usersView, HttpStatus.OK);

        when(this.userController.findAll()).thenReturn(usersView);

        ResponseEntity responseEntity1 = this.clientController.findUsers("1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void send() throws MessageException, RecipientException, URISyntaxException {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        NewMessageDto newMessageDto =  createNewMessageDto();

        Message message = createMessage();
        message.setRecipientList(null);

        ResponseEntity responseEntity = ResponseEntity.created(new URI("http://localhost/0")).build();

        when(this.messageController.send(newMessageDto, user)).thenReturn(message);

        ResponseEntity responseEntity1 = this.clientController.send(newMessageDto,"1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void newLabel() throws LabelException, URISyntaxException {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        NewLabelDto newLabelDto = createNewLabelDto();

        Label auxLabel = createLabel();
        auxLabel.setName(newLabelDto.getName());
        auxLabel.setUser(user);

        Label label = createLabel();

        ResponseEntity responseEntity = ResponseEntity.created(new URI("http://localhost/0")).build();

        when(this.labelController.createNewLabel(newLabelDto, user)).thenReturn(label);

        ResponseEntity responseEntity1 = this.clientController.newLabel(newLabelDto,"1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void assignLabelToMessage() throws LabelException, URISyntaxException {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Label label = createLabel();
        Message message = createMessage();

        LabelXMessage labelXMessage = createLabelXMessage();

        ResponseEntity responseEntity = ResponseEntity.created(new URI("http://localhost/0")).build();


        when(this.labelController.assignLabelToMessage(message.getId(), label.getId(), user)).thenReturn(labelXMessage);

        ResponseEntity responseEntity1 =  this.clientController.assignLabelToMessage(message.getId(), label.getId(), "1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void update() throws UserException, URISyntaxException {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        NewUserDto newUserDto = createNewUserDto();

        ResponseEntity responseEntity = ResponseEntity.created(new URI("http://localhost/0")).build();

        when(this.userController.update(newUserDto)).thenReturn(user);

        ResponseEntity responseEntity1 = this.clientController.update(newUserDto, "1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void deleteLabel() throws LabelException {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Label label = createLabel();
        label.setEnabled(false);

        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);

        when(this.labelController.delete(label.getId())).thenReturn(label);

        ResponseEntity responseEntity1 = this.clientController.deleteLabel(label.getId(), "1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void deleteSent() throws MessageException {

        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = createMessage();
        message.setDeletedByUserFrom(true);

        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);

        when(this.messageController.deleteSent(message.getId())).thenReturn(message);

        ResponseEntity responseEntity1 = this.clientController.deleteSent(message.getId(), "1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void deleteInbox() throws MessageException {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = createMessage();
        Recipient recipient = message.getRecipientList().get(0);

        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);

        when(this.messageController.deleteInbox(message.getId(),user.getId())).thenReturn(recipient);

        ResponseEntity responseEntity1 = this.clientController.deleteInbox(message.getId(), "1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

}
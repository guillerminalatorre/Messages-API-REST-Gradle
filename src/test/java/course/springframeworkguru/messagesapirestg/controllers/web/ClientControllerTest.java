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
import course.springframeworkguru.messagesapirestg.utils.ObjectsFactory;
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
    
    private ObjectsFactory objectsFactory = new ObjectsFactory();

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

    @Test
    public void getInfoOk() throws UserException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        ResponseEntity responseEntity = ResponseEntity.ok(user);

        ResponseEntity responseEntity1 = this.clientController.getInfo("1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test(expected = UserException.class)
    public void getInfoFail() throws UserException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("123")).thenReturn(null);

        ResponseEntity responseEntity1 = this.clientController.getInfo("123");
    }

    @Test
    public void sentByUserFromOk() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = objectsFactory.createMessage();

        MessageView messageView = objectsFactory.createMessageView();

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
    public void sentByUserFromFail() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.clientController.sentByUserFrom("1234", PageRequest.of(0,1));

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void sentByUserFromAndLabelOk() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = objectsFactory.createMessage();

        MessageView messageView = objectsFactory.createMessageView();

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
    public void sentByUserFromAndLabelFail() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.clientController.sentByUserFromAndLabel(1,"1234", PageRequest.of(0,1));

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void inboxByRecipientIdOk() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = objectsFactory.createMessage();

        MessageView messageView = objectsFactory.createMessageView();

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
    public void inboxByRecipientIdFail() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.clientController.inboxByRecipientId("1234", PageRequest.of(0,1));

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void inboxByRecipientIdAndLabelOk() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = objectsFactory.createMessage();

        MessageView messageView = objectsFactory.createMessageView();

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
    public void inboxByRecipientIdAndLabelFail() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.clientController.inboxByRecipientIdAndLabel(1,"1234", PageRequest.of(0,1));

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void findLabelsByMessageIdOk() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = objectsFactory.createMessage();

        List<LabelXMessageView> labels = new ArrayList<LabelXMessageView>();
        labels.add(objectsFactory.createLabelXMessageView());

        ResponseEntity responseEntity = new ResponseEntity(labels, HttpStatus.OK);

        when(this.labelController.findLabelsByMessageId(message.getId(),user.getId())).thenReturn(labels);

        ResponseEntity responseEntity1 = this.clientController.findLabelsByMessageId(message.getId(), "1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void findLabelsByMessageIdFail() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.clientController.findLabelsByMessageId(1,"1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }


    @Test
    public void findLabelsByUserIdOk() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = objectsFactory.createMessage();

        List<LabelView> labels = new ArrayList<LabelView>();
        labels.add(objectsFactory.createLabelView());

        ResponseEntity responseEntity = new ResponseEntity(labels, HttpStatus.OK);

        when(this.labelController.findLabelsByUserId(user.getId())).thenReturn(labels);

        ResponseEntity responseEntity1 = this.clientController.findLabelsByUserId("1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void findLabelsByUserIdFail() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.clientController.findLabelsByUserId("1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void findUserByMailUsernameLikeOk() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Employee employee = objectsFactory.createEmployee();
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
    public void findUserByMailUsernameLikeFail() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.clientController.findUserByMailUsernameLike("pe", "1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void testFindUsersOk() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Employee employee = objectsFactory.createEmployee();
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
    public void testFindUsersFail() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.clientController.findUsers("1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void sendOk() throws MessageException, RecipientException, URISyntaxException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        NewMessageDto newMessageDto =  objectsFactory.createNewMessageDto();

        Message message = objectsFactory.createMessage();
        message.setRecipientList(null);

        ResponseEntity responseEntity = ResponseEntity.created(new URI("http://localhost/0")).build();

        when(this.messageController.send(newMessageDto, user)).thenReturn(message);

        ResponseEntity responseEntity1 = this.clientController.send(newMessageDto,"1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void sendFail1() throws MessageException {
        User user = objectsFactory.createUser();

        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.clientController.send(objectsFactory.createNewMessageDto(),"1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void sendFail2() throws MessageException, RecipientException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        NewMessageDto newMessageDto =  objectsFactory.createNewMessageDto();

        ResponseEntity responseEntity = new ResponseEntity(new HttpMessageDto(null,null), HttpStatus.CREATED);

        when(this.messageController.send(newMessageDto, user)).thenThrow(RecipientException.class);

        ResponseEntity responseEntity1 = this.clientController.send(newMessageDto,"1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void newLabelOk() throws LabelException, URISyntaxException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        NewLabelDto newLabelDto = objectsFactory.createNewLabelDto();

        Label auxLabel = objectsFactory.createLabel();
        auxLabel.setName(newLabelDto.getName());
        auxLabel.setUser(user);

        Label label = objectsFactory.createLabel();

        ResponseEntity responseEntity = ResponseEntity.created(new URI("http://localhost/0")).build();

        when(this.labelController.createNewLabel(newLabelDto, user)).thenReturn(label);

        ResponseEntity responseEntity1 = this.clientController.newLabel(newLabelDto,"1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void newLabelFail1() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.clientController.newLabel(objectsFactory.createNewLabelDto(),"1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void newLabelFail2() throws LabelException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        NewLabelDto newLabelDto = objectsFactory.createNewLabelDto();

        ResponseEntity responseEntity = new ResponseEntity(new HttpMessageDto(null,null), HttpStatus.CREATED);

        when(this.labelController.createNewLabel(newLabelDto, user)).thenThrow(LabelException.class);

        ResponseEntity responseEntity1 = this.clientController.newLabel(newLabelDto,"1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void assignLabelToMessageOk() throws LabelException, URISyntaxException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Label label = objectsFactory.createLabel();
        Message message = objectsFactory.createMessage();

        LabelXMessage labelXMessage = objectsFactory.createLabelXMessage();

        ResponseEntity responseEntity = ResponseEntity.created(new URI("http://localhost/0")).build();


        when(this.labelController.assignLabelToMessage(message.getId(), label.getId(), user)).thenReturn(labelXMessage);

        ResponseEntity responseEntity1 =  this.clientController.assignLabelToMessage(message.getId(), label.getId(), "1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void assignLabelToMessageFail1() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.clientController.assignLabelToMessage(1, 1, "1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void assignLabelToMessageFail2() throws LabelException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        NewLabelDto newLabelDto = objectsFactory.createNewLabelDto();

        ResponseEntity responseEntity = new ResponseEntity(new HttpMessageDto(null,null), HttpStatus.CREATED);

        when(this.labelController.assignLabelToMessage(1, 1, user)).thenThrow(LabelException.class);

        ResponseEntity responseEntity1 = this.clientController.assignLabelToMessage(1, 1, "1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void updateOk() throws UserException, URISyntaxException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        NewUserDto newUserDto = objectsFactory.createNewUserDto();

        ResponseEntity responseEntity = ResponseEntity.created(new URI("http://localhost/0")).build();

        when(this.userController.update(newUserDto)).thenReturn(user);

        ResponseEntity responseEntity1 = this.clientController.update(newUserDto, "1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void updateFail1() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.clientController.update(objectsFactory.createNewUserDto(), "1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void updateFail2() throws UserException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        NewUserDto newUserDto = objectsFactory.createNewUserDto();

        ResponseEntity responseEntity = new ResponseEntity(new HttpMessageDto(null,null), HttpStatus.BAD_REQUEST);

        when(this.userController.update(newUserDto)).thenThrow(UserException.class);

        ResponseEntity responseEntity1 = this.clientController.update(newUserDto, "1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void deleteLabelOk() throws LabelException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Label label = objectsFactory.createLabel();
        label.setEnabled(false);

        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);

        when(this.labelController.delete(label.getId())).thenReturn(label);

        ResponseEntity responseEntity1 = this.clientController.deleteLabel(label.getId(), "1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void deleteLabelFail1() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.clientController.deleteLabel(1, "1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void deleteLabelFail2() throws LabelException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Label label = objectsFactory.createLabel();
        label.setEnabled(false);

        ResponseEntity responseEntity = new ResponseEntity(new HttpMessageDto(null,null), HttpStatus.BAD_REQUEST);

        when(this.labelController.delete(label.getId())).thenThrow(LabelException.class);

        ResponseEntity responseEntity1 = this.clientController.deleteLabel(label.getId(), "1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void deleteSentOk() throws MessageException {

        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = objectsFactory.createMessage();
        message.setDeletedByUserFrom(true);

        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);

        when(this.messageController.deleteSent(message.getId())).thenReturn(message);

        ResponseEntity responseEntity1 = this.clientController.deleteSent(message.getId(), "1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void deleteSentFail1() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.clientController.deleteSent(1, "1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void deleteSentFail2() throws MessageException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = objectsFactory.createMessage();
        message.setDeletedByUserFrom(true);

        ResponseEntity responseEntity = new ResponseEntity(new HttpMessageDto(null,null), HttpStatus.BAD_REQUEST);

        when(this.messageController.deleteSent(message.getId())).thenThrow(MessageException.class);

        ResponseEntity responseEntity1 = this.clientController.deleteSent(message.getId(), "1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void deleteInboxOk() throws MessageException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = objectsFactory.createMessage();
        Recipient recipient = message.getRecipientList().get(0);

        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);

        when(this.messageController.deleteInbox(message.getId(),user.getId())).thenReturn(recipient);

        ResponseEntity responseEntity1 = this.clientController.deleteInbox(message.getId(), "1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void deleteInboxFail1() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.clientController.deleteInbox(1, "1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void deleteInboxFail2() throws MessageException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        Message message = objectsFactory.createMessage();
        Recipient recipient = message.getRecipientList().get(0);

        ResponseEntity responseEntity = new ResponseEntity(new HttpMessageDto(null,null), HttpStatus.BAD_REQUEST);

        when(this.messageController.deleteInbox(message.getId(),user.getId())).thenThrow(MessageException.class);

        ResponseEntity responseEntity1 = this.clientController.deleteInbox(message.getId(), "1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

}
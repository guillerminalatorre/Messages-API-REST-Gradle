package course.springframeworkguru.messagesapirestg.controllers.web;

import course.springframeworkguru.messagesapirestg.controllers.MessageController;
import course.springframeworkguru.messagesapirestg.controllers.UserController;
import course.springframeworkguru.messagesapirestg.dto.RecipientDto;
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

public class AdminControllerTest {

    @Mock
    private UserController userController;
    @Mock
    private MessageController messageController;
    @Mock
    private SessionManager sessionManager;

    private AdminController adminController;

    private ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Before
    public void setUp(){
        initMocks(this);
        this.adminController =  new AdminController(userController,messageController,sessionManager);

        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @After
    public void teardown() {
        RequestContextHolder.resetRequestAttributes();
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

    private Label createLabel(){
        return Label.builder()
                .id(0)
                .name("Work")
                .isEnabled(true)
                .user(createUser())
                .build();
    }

    private LabelView createLabelView(){
        Label label = createLabel();
        LabelView labelView = this.factory.createProjection(LabelView.class);
        labelView.setName(label.getName());
        labelView.setId(label.getId());

        return labelView;
    }

    @Test
    public void getInfo() throws UserException {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        ResponseEntity responseEntity = ResponseEntity.ok(user);

        ResponseEntity responseEntity1 = this.adminController.getInfo("1234");

        Assert.assertEquals(responseEntity, responseEntity1);
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

        ResponseEntity responseEntity1 = this.adminController.sentByUserFrom("1234", user.getId(), PageRequest.of(0,1));

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

        ResponseEntity responseEntity1 = this.adminController.inboxByRecipientId("1234", user.getId(),PageRequest.of(0,1));

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void makeAdmin() throws UserException, URISyntaxException {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        user.setAdmin(true);

        ResponseEntity responseEntity = ResponseEntity.created(new URI("http://localhost/0")).build();

        when(this.userController.makeAdmin(user.getId(),true)).thenReturn(user);

        ResponseEntity responseEntity1 = this.adminController.makeAdmin(user.getId(),true,"1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void deleteUser() throws UserException {
        User user = createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);

        when(this.userController.delete(user.getId())).thenReturn(user);

        ResponseEntity responseEntity1 = this.adminController.deleteUser(user.getId(), "1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }
}
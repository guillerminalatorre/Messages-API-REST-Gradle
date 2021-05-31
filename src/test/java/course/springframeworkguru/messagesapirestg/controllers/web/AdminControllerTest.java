package course.springframeworkguru.messagesapirestg.controllers.web;

import course.springframeworkguru.messagesapirestg.controllers.MessageController;
import course.springframeworkguru.messagesapirestg.controllers.UserController;
import course.springframeworkguru.messagesapirestg.dto.HttpMessageDto;
import course.springframeworkguru.messagesapirestg.exceptions.UserException;
import course.springframeworkguru.messagesapirestg.models.*;
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

public class AdminControllerTest {

    @Mock
    private UserController userController;
    @Mock
    private MessageController messageController;
    @Mock
    private SessionManager sessionManager;

    private AdminController adminController;

    private ObjectsFactory objectsFactory = new ObjectsFactory();

    private ObjectsFactory objectFactory = new ObjectsFactory();

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

    @Test
    public void getInfoOk() throws UserException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        ResponseEntity responseEntity = ResponseEntity.ok(user);

        ResponseEntity responseEntity1 = this.adminController.getInfo("1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test(expected = UserException.class)
    public void getInfoFail() throws UserException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity1 = this.adminController.getInfo("1234");
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

        ResponseEntity responseEntity1 = this.adminController.sentByUserFrom("1234", user.getId(), PageRequest.of(0,1));

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void sentByUserFromFail() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.adminController.sentByUserFrom("1234", user.getId(), PageRequest.of(0,1));

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

        ResponseEntity responseEntity1 = this.adminController.inboxByRecipientId("1234", user.getId(),PageRequest.of(0,1));

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void inboxByRecipientIdFail() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.adminController.inboxByRecipientId("1234", user.getId(),PageRequest.of(0,1));

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void makeAdminOk() throws UserException, URISyntaxException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        user.setAdmin(true);

        ResponseEntity responseEntity = ResponseEntity.created(new URI("http://localhost/0")).build();

        when(this.userController.makeAdmin(user.getId(),true)).thenReturn(user);

        ResponseEntity responseEntity1 = this.adminController.makeAdmin(user.getId(),true,"1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void makeAdminFail1() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.adminController.makeAdmin(user.getId(),true,"1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void makeAdminFail2() throws UserException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        user.setAdmin(true);

        ResponseEntity responseEntity = new ResponseEntity(new HttpMessageDto(null,null), HttpStatus.BAD_REQUEST);

        when(this.userController.makeAdmin(user.getId(),true)).thenThrow(UserException.class);

        ResponseEntity responseEntity1 = this.adminController.makeAdmin(user.getId(),true,"1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void deleteUserOk() throws UserException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);

        when(this.userController.delete(user.getId())).thenReturn(user);

        ResponseEntity responseEntity1 = this.adminController.deleteUser(user.getId(), "1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void deleteUserFail1() {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(null);

        ResponseEntity responseEntity = ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        ResponseEntity responseEntity1 = this.adminController.deleteUser(user.getId(), "1234");

        Assert.assertEquals(responseEntity, responseEntity1);
    }

    @Test
    public void deleteUserFail2() throws UserException {
        User user = objectsFactory.createUser();
        when(sessionManager.getCurrentUser("1234")).thenReturn(user);

        ResponseEntity responseEntity = new ResponseEntity(new HttpMessageDto(null,null), HttpStatus.BAD_REQUEST);

        when(this.userController.delete(user.getId())).thenThrow(UserException.class);

        ResponseEntity responseEntity1 = this.adminController.deleteUser(user.getId(), "1234");

        Assert.assertEquals(responseEntity,responseEntity1);
    }
}
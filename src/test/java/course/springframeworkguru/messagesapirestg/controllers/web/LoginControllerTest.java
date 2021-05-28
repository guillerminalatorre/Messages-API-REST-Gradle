package course.springframeworkguru.messagesapirestg.controllers.web;

import course.springframeworkguru.messagesapirestg.controllers.UserController;
import course.springframeworkguru.messagesapirestg.dto.LoginDto;
import course.springframeworkguru.messagesapirestg.dto.NewUserDto;
import course.springframeworkguru.messagesapirestg.exceptions.LoginException;
import course.springframeworkguru.messagesapirestg.exceptions.UserException;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.models.employees.City;
import course.springframeworkguru.messagesapirestg.models.employees.Employee;
import course.springframeworkguru.messagesapirestg.session.SessionManager;
import course.springframeworkguru.messagesapirestg.utils.Hash;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LoginControllerTest {

    @Mock
    private UserController userController;
    @Mock
    private SessionManager sessionManager;

    private LoginController loginController;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.loginController= new LoginController(userController, sessionManager);

        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @After
    public void teardown() {
        RequestContextHolder.resetRequestAttributes();
    }

    private LoginDto createLoginDto(){
        return new LoginDto("pepe", "1234");
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
                .city(new City())
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

    @Test
    public void loginOk() throws LoginException, UserException {
        ResponseEntity response;
        LoginDto loginDto  =createLoginDto();
        User user = createUser();
        loginDto.setPassword(Hash.getHash(loginDto.getPassword()));

        when(this.userController.login(loginDto,sessionManager)).thenReturn(user);
        when(this.sessionManager.createSession(user)).thenReturn("123456");

        ResponseEntity responseEntity = ResponseEntity.ok().headers(createHeaders("123456")).build();

        ResponseEntity responseEntity1 = this.loginController.login(loginDto);

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void singin() throws UserException, URISyntaxException {
        User user = createUser();
        NewUserDto newUserDto = createNewUserDto();

        ResponseEntity responseEntity = ResponseEntity.created(new URI("http://localhost/0")).build();

        when(this.userController.singIn(newUserDto)).thenReturn(user);

        ResponseEntity responseEntity1 = this.loginController.singin(newUserDto);

        Assert.assertEquals(responseEntity,responseEntity1);
    }

    @Test
    public void logout() {
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
        ResponseEntity responseEntity1 = this.loginController.logout("1234");
        Assert.assertEquals(responseEntity, responseEntity1);
    }

    private HttpHeaders createHeaders(String token) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token);
        return responseHeaders;
    }
}
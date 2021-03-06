package course.springframeworkguru.messagesapirestg.controllers;

import course.springframeworkguru.messagesapirestg.dto.LoginDto;
import course.springframeworkguru.messagesapirestg.dto.NewUserDto;
import course.springframeworkguru.messagesapirestg.exceptions.LoginException;
import course.springframeworkguru.messagesapirestg.exceptions.UserException;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.models.employees.Employee;
import course.springframeworkguru.messagesapirestg.services.UserService;
import course.springframeworkguru.messagesapirestg.session.SessionManager;
import course.springframeworkguru.messagesapirestg.utils.ObjectsFactory;
import course.springframeworkguru.messagesapirestg.views.EmployeeView;
import course.springframeworkguru.messagesapirestg.views.UserView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserControllerTest {
    @Mock
    private UserService userService;

    private UserController userController;

    private ObjectsFactory objectsFactory = new ObjectsFactory();

    private ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.userController = new UserController(userService);
    }
    
    @Test
    public void loginOk() throws UserException, LoginException {
        User user = objectsFactory.createUser();
        LoginDto loginDto = objectsFactory.createLoginDto();

        SessionManager sessionManager = new SessionManager();

        when(this.userService.login(loginDto)).thenReturn(user);

        User user1 = this.userController.login(loginDto, sessionManager);

        Assert.assertEquals(user, user1);
    }

    @Test(expected = LoginException.class)
    public void loginFail1() throws UserException, LoginException {
        User user = objectsFactory.createUser();
        LoginDto loginDto = objectsFactory.createLoginDto();

        SessionManager sessionManager = new SessionManager();
        sessionManager.createSession(user);

        when(this.userService.login(loginDto)).thenReturn(user);

        User user1 = this.userController.login(loginDto, sessionManager);
    }

    @Test(expected = UserException.class)
    public void loginFail() throws UserException, LoginException {
        User user = objectsFactory.createUser();
        LoginDto loginDto = objectsFactory.createLoginDto();

        SessionManager sessionManager = new SessionManager();
        sessionManager.createSession(user);

        when(this.userService.login(loginDto)).thenThrow(UserException.class);

        User user1 = this.userController.login(loginDto, sessionManager);
    }

    @Test
    public void singInOk() throws UserException {
        User user = objectsFactory.createUser();
        NewUserDto newUserDto = objectsFactory.createNewUserDto();

        when(this.userService.save(newUserDto)).thenReturn(user);

        User user1 = this.userController.singIn(newUserDto);

        Assert.assertEquals(user, user1);
    }

    @Test(expected = UserException.class)
    public void singInOFail() throws UserException {
        User user = objectsFactory.createUser();
        NewUserDto newUserDto = objectsFactory.createNewUserDto();

        when(this.userService.save(newUserDto)).thenThrow(UserException.class);

        User user1 = this.userController.singIn(newUserDto);
    }

    @Test
    public void updateOk() throws UserException {
        User user = objectsFactory.createUser();
        NewUserDto newUserDto = objectsFactory.createNewUserDto();

        when(this.userService.update(newUserDto)).thenReturn(user);

        User user1 = this.userController.update(newUserDto);

        Assert.assertEquals(user, user1);
    }

    @Test(expected = UserException.class)
    public void updateFail() throws UserException {
        User user = objectsFactory.createUser();
        NewUserDto newUserDto = objectsFactory.createNewUserDto();

        when(this.userService.update(newUserDto)).thenThrow(UserException.class);

        User user1 = this.userController.update(newUserDto);
    }

    @Test
    public void makeAdminOk() throws UserException {
        User user = objectsFactory.createUser();

        when(this.userService.changeAdminStatus(0,false)).thenReturn(user);

        User user1 = this.userController.makeAdmin(0,false);

        Assert.assertEquals(user, user1);
    }

    @Test(expected = UserException.class)
    public void makeAdminFail() throws UserException {
        User user = objectsFactory.createUser();

        when(this.userService.changeAdminStatus(0,false)).thenThrow(UserException.class);

        User user1 = this.userController.makeAdmin(0,false);
    }

    @Test
    public void findByMailUsernameLike() {
        Employee employee = objectsFactory.createEmployee();
        EmployeeView employeeView = this.factory.createProjection(EmployeeView.class);
        employeeView.setMailUsername(employee.getMailUsername());

        User user = objectsFactory.createUser();
        UserView userView = this.factory.createProjection(UserView.class);
        userView.setUsername(user.getUsername());
        userView.setEmployee(employeeView);

        List<UserView> usersView = new ArrayList<UserView>();

        usersView.add(userView);

        when(this.userService.findByMailUsernameLike(employee.getMailUsername())).thenReturn(usersView);

        List<UserView> usersView1 = this.userController.findByMailUsernameLike(employee.getMailUsername());

        Assert.assertEquals(usersView, usersView1);
    }

    @Test
    public void findAll() {
        Employee employee = objectsFactory.createEmployee();
        EmployeeView employeeView = this.factory.createProjection(EmployeeView.class);
        employeeView.setMailUsername(employee.getMailUsername());

        User user = objectsFactory.createUser();
        UserView userView = this.factory.createProjection(UserView.class);
        userView.setUsername(user.getUsername());
        userView.setEmployee(employeeView);

        List<UserView> usersView = new ArrayList<UserView>();

        usersView.add(userView);

        when(userService.findAll()).thenReturn(usersView);

        List<UserView> usersView1 = this.userController.findAll();

        Assert.assertEquals(usersView, usersView1);
    }

    @Test
    public void deleteOk() throws UserException {
        User user = objectsFactory.createUser();
        user.setEnabled(false);

        when(this.userService.delete(user.getId())).thenReturn(user);

        User user1 = this.userController.delete(user.getId());

        Assert.assertEquals(user, user1);
    }

    @Test(expected = UserException.class)
    public void deleteFail() throws UserException {
        User user = objectsFactory.createUser();
        user.setEnabled(false);

        when(this.userService.delete(user.getId())).thenThrow(UserException.class);

        User user1 = this.userController.delete(user.getId());
    }
}
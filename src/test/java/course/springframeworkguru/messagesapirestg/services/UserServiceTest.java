package course.springframeworkguru.messagesapirestg.services;

import course.springframeworkguru.messagesapirestg.dto.LoginDto;
import course.springframeworkguru.messagesapirestg.dto.NewUserDto;
import course.springframeworkguru.messagesapirestg.exceptions.UserException;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.models.employees.City;
import course.springframeworkguru.messagesapirestg.models.employees.Employee;
import course.springframeworkguru.messagesapirestg.repositories.EmployeeRepository;
import course.springframeworkguru.messagesapirestg.repositories.UserRepository;
import course.springframeworkguru.messagesapirestg.utils.Hash;
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

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmployeeRepository employeeRepository;

    private UserService userService;

    private ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.userService = new UserService(this.userRepository, this.employeeRepository);
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
    public void loginOk() throws UserException {
        User user = this.createUser();

        when(this.userRepository.findByEmployeeMailUsernameAndIsEnabledTrue(user.getEmployee().getMailUsername())).thenReturn(user);

        User user1 = this.userService.login(createLoginDto());

        Assert.assertEquals(user, user1);
    }

    @Test(expected = UserException.class)
    public void loginFail() throws UserException {
        User user = this.createUser();

        when(this.userRepository.findByEmployeeMailUsernameAndIsEnabledTrue(user.getEmployee().getMailUsername())).thenReturn(user);

        User user1 = this.userService.login(new LoginDto("some", "some"));

        Assert.assertEquals(user, user1);
    }

    @Test
    public void saveOk() throws UserException {
        NewUserDto newUserDto =  this.createNewUserDto();

        Employee employee = this.createEmployee();

        User user = new User();
        user.setAdmin(false);
        user.setEmployee(employee);
        user.setUsername(newUserDto.getUsername());
        user.setPassword(Hash.getHash(newUserDto.getPassword()));
        user.setEnabled(true);


        when(this.employeeRepository.findByMailUsername(newUserDto.getMailUsername())).thenReturn(employee);
        when(this.userRepository.findByEmployeeMailUsernameAndIsEnabledTrue(newUserDto.getMailUsername())).thenReturn(null);
        when(this.userRepository.save(user)).thenReturn(user);


        User user1 = this.userService.save(newUserDto);

        Assert.assertEquals(user,user1);
    }

    @Test(expected = UserException.class)
    public void saveFail() throws UserException {
        NewUserDto newUserDto =  this.createNewUserDto();

        Employee employee = this.createEmployee();

        User user = new User();
        user.setAdmin(false);
        user.setEmployee(employee);
        user.setUsername(newUserDto.getUsername());
        user.setPassword(Hash.getHash(newUserDto.getPassword()));
        user.setEnabled(true);

        when(this.employeeRepository.findByMailUsername(newUserDto.getMailUsername())).thenReturn(null);
        when(this.userRepository.findByEmployeeMailUsernameAndIsEnabledTrue(newUserDto.getMailUsername())).thenReturn(null);
        when(this.userRepository.save(user)).thenReturn(null);

        User user1 = this.userService.save(newUserDto);
    }

    @Test
    public void updateOk() throws UserException {
        NewUserDto newUserDto =  this.createNewUserDto();

        Employee employee = this.createEmployee();

        User user = new User();
        user.setAdmin(false);
        user.setEmployee(employee);
        user.setUsername(newUserDto.getUsername());
        user.setPassword(Hash.getHash(newUserDto.getPassword()));
        user.setEnabled(true);

        when(this.employeeRepository.findByMailUsername(newUserDto.getMailUsername())).thenReturn(employee);
        when(this.userRepository.findByEmployeeMailUsernameAndIsEnabledTrue(newUserDto.getMailUsername())).thenReturn(user);
        when(this.userRepository.save(user)).thenReturn(user);

        User user1 = this.userService.update(newUserDto);

        Assert.assertEquals(user, user1);
    }

    @Test(expected = UserException.class)
    public void updateFail() throws UserException {
        NewUserDto newUserDto =  this.createNewUserDto();

        Employee employee = this.createEmployee();

        User user = new User();
        user.setAdmin(false);
        user.setEmployee(employee);
        user.setUsername(newUserDto.getUsername());
        user.setPassword(Hash.getHash(newUserDto.getPassword()));
        user.setEnabled(true);

        when(this.employeeRepository.findByMailUsername(newUserDto.getMailUsername())).thenReturn(null);
        when(this.userRepository.findByEmployeeMailUsernameAndIsEnabledTrue(newUserDto.getMailUsername())).thenReturn(null);
        when(this.userRepository.save(user)).thenReturn(user);

        User user1 = this.userService.update(newUserDto);
    }

    @Test
    public void changeAdminStatusOk() throws UserException {

        User user = this.createUser();

        when(this.userRepository.findByIdAndIsEnabledTrue(user.getId())).thenReturn(user);
        when(this.userRepository.save(user)).thenReturn(user);


        User user1 = this.userService.changeAdminStatus(user.getId(), true);

        user.setAdmin(true);

        Assert.assertEquals(user, user1);
    }

    @Test(expected = UserException.class)
    public void changeAdminStatusFail() throws UserException {

        User user = this.createUser();

        when(this.userRepository.findByIdAndIsEnabledTrue(user.getId())).thenReturn(null);
        when(this.userRepository.save(user)).thenReturn(user);

        User user1 = this.userService.changeAdminStatus(user.getId(), true);
    }

    @Test
    public void findByMailUsernameLike() {
        Employee employee = createEmployee();
        EmployeeView employeeView = this.factory.createProjection(EmployeeView.class);
        employeeView.setMailUsername(employee.getMailUsername());

        User user = createUser();
        UserView userView = this.factory.createProjection(UserView.class);
        userView.setUsername(user.getUsername());
        userView.setEmployee(employeeView);

        List<UserView> usersView = new ArrayList<UserView>();

        usersView.add(userView);

        when(userRepository.findByEmployeeMailUsernameLikeAndIsEnabledTrue("%"+employeeView.getMailUsername()+"%"))
                .thenReturn(usersView);

        List<UserView> usersView1 = this.userService.findByMailUsernameLike(employeeView.getMailUsername());

        Assert.assertEquals(usersView, usersView1);
    }

    @Test
    public void findAll() {
        Employee employee = createEmployee();
        EmployeeView employeeView = this.factory.createProjection(EmployeeView.class);
        employeeView.setMailUsername(employee.getMailUsername());

        User user = createUser();
        UserView userView = this.factory.createProjection(UserView.class);
        userView.setUsername(user.getUsername());
        userView.setEmployee(employeeView);

        List<UserView> usersView = new ArrayList<UserView>();

        usersView.add(userView);

        when(userRepository.findByIsEnabledTrue()).thenReturn(usersView);

        List<UserView> usersView1 = this.userService.findAll();

        Assert.assertEquals(usersView, usersView1);
    }

    @Test
    public void deleteOk() throws UserException {
        User user = this.createUser();

        when(this.userRepository.findByIdAndIsEnabledTrue(user.getId())).thenReturn(user);

        user.setEnabled(false);
        when(this.userRepository.save(user)).thenReturn(user);


        User user1 = this.userService.delete(user.getId());

        Assert.assertEquals(user, user1);
    }

    @Test(expected = UserException.class)
    public void deleteFail() throws UserException {
        User user = this.createUser();

        when(this.userRepository.findByIdAndIsEnabledTrue(user.getId())).thenReturn(null);

        User user1 = this.userService.delete(user.getId());
    }
}
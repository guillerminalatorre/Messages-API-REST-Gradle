package course.springframeworkguru.messagesapirestg.controllers;

import course.springframeworkguru.messagesapirestg.dto.LoginDto;
import course.springframeworkguru.messagesapirestg.dto.NewUserDto;
import course.springframeworkguru.messagesapirestg.exceptions.LoginException;
import course.springframeworkguru.messagesapirestg.exceptions.UserException;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.services.UserService;
import course.springframeworkguru.messagesapirestg.session.SessionManager;
import course.springframeworkguru.messagesapirestg.views.UserView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    public User login(LoginDto loginDto, SessionManager sessionManager) throws LoginException, UserException {

            User user = this.userService.login(loginDto);

            if(sessionManager.userIsLogged(user)){
                return (User) Optional.ofNullable(null).orElseThrow(() -> new LoginException("Invalid Login","This user is already logged"));
            }
            else {
                return user;
            }

    }

    public User singIn(NewUserDto newUserDto) throws UserException {

        return this.userService.save(newUserDto);
    }

    public User update(NewUserDto newUserDto) throws UserException {

        return this.userService.update(newUserDto);
    }

    public User makeAdmin(int id, boolean status) throws UserException {

        return this.userService.changeAdminStatus(id, status);
    }


    public List<UserView> findByMailUsernameLike(String mailUsername){

        return this.userService.findByMailUsernameLike(mailUsername);
    }

    public List<UserView> findAll(){

        return this.userService.findAll();
    }

    public User delete(int idUser) throws UserException {

        return this.userService.delete(idUser);
    }
}

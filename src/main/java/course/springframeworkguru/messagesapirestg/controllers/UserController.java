package course.springframeworkguru.messagesapirestg.controllers;

import course.springframeworkguru.messagesapirestg.dto.HttpMessageDto;
import course.springframeworkguru.messagesapirestg.dto.input.LoginDto;
import course.springframeworkguru.messagesapirestg.dto.input.NewUserDto;
import course.springframeworkguru.messagesapirestg.exceptions.LoginException;
import course.springframeworkguru.messagesapirestg.exceptions.UserException;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.services.UserService;
import course.springframeworkguru.messagesapirestg.session.SessionManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
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

    public User singIn(@RequestBody NewUserDto newUserDto) throws UserException {

        return this.userService.save(newUserDto);
    }

    public User update(NewUserDto newUserDto) throws UserException {

        return this.userService.update(newUserDto);
    }

    public User makeAdmin(int id, boolean status) throws UserException {

        return this.userService.changeAdminStatus(id, status);
    }


    public List<User> findByMailUsernameLike(String mailUsername){

        return this.userService.findByMailUsernameLike(mailUsername);
    }

    public List<User> findAll(){

        return this.userService.findAll();
    }

    public User delete(int idUser) throws UserException {

        return this.userService.delete(idUser);
    }

    /*@PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) {

        try {
            User user = this.userService.login(loginDto);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(user.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        }
        catch(UserException userException) {
            return new ResponseEntity(new HttpMessageDto(userException.getMessage(), userException.getDetails()), HttpStatus.UNAUTHORIZED);
        }
    }*/
}

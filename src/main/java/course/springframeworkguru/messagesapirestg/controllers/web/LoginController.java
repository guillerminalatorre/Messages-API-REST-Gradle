package course.springframeworkguru.messagesapirestg.controllers.web;

import course.springframeworkguru.messagesapirestg.controllers.UserController;
import course.springframeworkguru.messagesapirestg.dto.HttpMessageDto;
import course.springframeworkguru.messagesapirestg.dto.input.LoginDto;
import course.springframeworkguru.messagesapirestg.dto.input.NewUserDto;
import course.springframeworkguru.messagesapirestg.exceptions.LoginException;
import course.springframeworkguru.messagesapirestg.exceptions.UserException;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.session.SessionManager;
import course.springframeworkguru.messagesapirestg.utils.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/")
public class LoginController {

    UserController userController;
    SessionManager sessionManager;

    @Autowired
    public LoginController(UserController userController, SessionManager sessionManager) {
        this.userController = userController;
        this.sessionManager = sessionManager;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto) throws UserException, LoginException {

        ResponseEntity response;

        Hash hash = new Hash();

        String password = hash.getHash(loginDto.getPassword());

        loginDto.setPassword(password);

        try{

            User u = userController.login(loginDto, sessionManager);

            String token = sessionManager.createSession(u);

            return ResponseEntity.ok().headers(createHeaders(token)).build();

        }catch (LoginException loginException){
            return new ResponseEntity(new HttpMessageDto(loginException.getMessage(), loginException.getDetails()), HttpStatus.UNAUTHORIZED);

        }catch(UserException userException){
            return new ResponseEntity(new HttpMessageDto(userException.getMessage(), userException.getDetails()), HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/singin")
    public ResponseEntity singin(@RequestBody NewUserDto newUserDto){
        try {
            User user = this.userController.singIn(newUserDto);

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
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@RequestHeader("Authorization") String token) {
        sessionManager.removeSession(token);
        return ResponseEntity.ok().build();
    }

    private HttpHeaders createHeaders(String token) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token);
        return responseHeaders;
    }

}

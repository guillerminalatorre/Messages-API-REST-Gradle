package course.springframeworkguru.messagesapirestg.controllers.web;

import course.springframeworkguru.messagesapirestg.controllers.MessageController;
import course.springframeworkguru.messagesapirestg.controllers.UserController;
import course.springframeworkguru.messagesapirestg.dto.HttpMessageDto;
import course.springframeworkguru.messagesapirestg.exceptions.UserException;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserController userController;
    private final MessageController messageController;
    private final SessionManager sessionManager;

    @Autowired
    public AdminController(UserController userController, MessageController messageController, SessionManager sessionManager) {
        this.userController = userController;
        this.messageController = messageController;
        this.sessionManager = sessionManager;
    }

    @GetMapping("/")
    public ResponseEntity<User> getInfo(@RequestHeader("Authorization") String sessionToken) throws UserException {
        User currentUser = getCurrentUser(sessionToken);

        return ResponseEntity.ok(currentUser);

    }

    @PutMapping("/users/make-admin")
    public ResponseEntity makeAdmin(@RequestParam("user") int idUser,
                                    @RequestParam("status") boolean status,
                                    @RequestHeader("Authorization") String sessionToken){

        User currentUser = sessionManager.getCurrentUser(sessionToken);

        if (currentUser == null) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            User user = this.userController.makeAdmin(idUser, status);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(user.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        }
        catch(UserException userException) {
            return new ResponseEntity(new HttpMessageDto(userException.getMessage(), userException.getDetails()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/users")
    public ResponseEntity deleteUser(@RequestParam("user") int idUser,
                                     @RequestHeader("Authorization") String sessionToken) {

        User currentUser = sessionManager.getCurrentUser(sessionToken);

        if (currentUser == null) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try{
            User user = this.userController.delete(idUser);

            return new ResponseEntity(HttpStatus.OK);

        }catch (UserException userException){
            return new ResponseEntity(new HttpMessageDto(userException.getMessage(), userException.getDetails()), HttpStatus.BAD_REQUEST);
        }
    }

    private User getCurrentUser(String sessionToken) throws UserException {
        return Optional.ofNullable(sessionManager.getCurrentUser(sessionToken)).orElseThrow(() ->
                new UserException("User error","User not logged"));
    }
}

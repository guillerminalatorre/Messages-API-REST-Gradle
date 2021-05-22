package course.springframeworkguru.messagesapirestg.controllers.web;

import course.springframeworkguru.messagesapirestg.controllers.LabelController;
import course.springframeworkguru.messagesapirestg.controllers.MessageController;
import course.springframeworkguru.messagesapirestg.controllers.UserController;
import course.springframeworkguru.messagesapirestg.dto.HttpMessageDto;
import course.springframeworkguru.messagesapirestg.dto.input.MessageDto;
import course.springframeworkguru.messagesapirestg.dto.input.NewLabelDto;
import course.springframeworkguru.messagesapirestg.dto.input.NewUserDto;
import course.springframeworkguru.messagesapirestg.dto.output.LabelDto;
import course.springframeworkguru.messagesapirestg.exceptions.LabelException;
import course.springframeworkguru.messagesapirestg.exceptions.MessageException;
import course.springframeworkguru.messagesapirestg.exceptions.RecipientException;
import course.springframeworkguru.messagesapirestg.exceptions.UserException;
import course.springframeworkguru.messagesapirestg.models.Label;
import course.springframeworkguru.messagesapirestg.models.LabelXMessage;
import course.springframeworkguru.messagesapirestg.models.Message;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ClientController {

    private final UserController userController;
    private final MessageController messageController;
    private final LabelController labelController;
    private final SessionManager sessionManager;

    @Autowired
    public ClientController(UserController userController, MessageController messageController, LabelController labelController, SessionManager sessionManager) {
        this.userController = userController;
        this.messageController = messageController;
        this.labelController = labelController;
        this.sessionManager = sessionManager;
    }

    @GetMapping("/")
    public ResponseEntity<User> getInfo(@RequestHeader("Authorization") String sessionToken) throws UserException {
        User currentUser = getCurrentUser(sessionToken);

        return ResponseEntity.ok(currentUser);

    }

    @PostMapping("/send")
    public ResponseEntity<Message> send(@RequestBody MessageDto messageDto,
                                        @RequestHeader("Authorization") String sessionToken) throws MessageException {

        User currentUser = sessionManager.getCurrentUser(sessionToken);

        if (currentUser == null) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Message newMessage = this.messageController.send(messageDto, currentUser);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newMessage.getId())
                    .toUri();

            return ResponseEntity.created(location).build();

        }catch(RecipientException recipientException) {

            return new ResponseEntity(new HttpMessageDto(recipientException.getMessage(), recipientException.getDetails()), HttpStatus.CREATED);
        }

    }

    @GetMapping(value = "/sent")
    public ResponseEntity sentByUserFrom(@RequestHeader("Authorization") String sessionToken,
                                                          Pageable pageable){

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<Message> page = this.messageController.findMessagesByUserFrom(currentUser.getId(), pageable);

        if(page.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        else return ResponseEntity.status(HttpStatus.OK).
                header("total-count", Long.toString(page.getTotalElements())).
                header("total-pages", Long.toString(page.getTotalPages())).
                body(page.getContent());
    }

    @GetMapping("/sent/label")
    public ResponseEntity sentByUserFromAndLabel(@RequestParam("label") int idLabel,
                                         @RequestHeader("Authorization") String sessionToken,
                                         Pageable pageable){

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<Message> page = this.messageController.findMessagesSentByLabel(currentUser.getId(), idLabel, pageable);

        if(page.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        else return ResponseEntity.status(HttpStatus.OK).
                header("total-count", Long.toString(page.getTotalElements())).
                header("total-pages", Long.toString(page.getTotalPages())).
                body(page.getContent());
    }

    @GetMapping(value = "/inbox")
    public ResponseEntity inboxByRecipientId(@RequestHeader("Authorization") String sessionToken,
                                         Pageable pageable){

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<Message> page = this.messageController.findByRecipientId(currentUser.getId(), pageable);

        if(page.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        else return ResponseEntity.status(HttpStatus.OK).
                header("total-count", Long.toString(page.getTotalElements())).
                header("total-pages", Long.toString(page.getTotalPages())).
                body(page.getContent());
    }

    @GetMapping("/inbox/label")
    public ResponseEntity inboxByRecipientIdAndLabel(@RequestParam("label") int idLabel,
                                                 @RequestHeader("Authorization") String sessionToken,
                                                 Pageable pageable){

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Page<Message> page = this.messageController.findMessagesInboxByLabel(currentUser.getId(), idLabel, pageable);

        if(page.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        else return ResponseEntity.status(HttpStatus.OK).
                header("total-count", Long.toString(page.getTotalElements())).
                header("total-pages", Long.toString(page.getTotalPages())).
                body(page.getContent());
    }

    @GetMapping("/messages/labels")
    public ResponseEntity findLabelsByMessageId(@RequestParam("message") int idMessage,
                                                @RequestHeader("Authorization") String sessionToken){

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<LabelDto> labels = this.labelController.findLabelsByMessageId(idMessage, currentUser.getId());

        if(labels == null ) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        else return new ResponseEntity(labels, HttpStatus.OK);
    }
    
    @GetMapping("/labels")
    public ResponseEntity findLabelsByUserId(@RequestHeader("Authorization") String sessionToken){

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<LabelDto> labels = this.labelController.findLabelsByUserId(currentUser.getId());

        if(labels == null ) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        else return new ResponseEntity(labels, HttpStatus.OK);
    }

    @PostMapping("/label")
    public ResponseEntity<Label> newLabel(@RequestBody NewLabelDto newLabelDto,
                                          @RequestHeader("Authorization") String sessionToken){

        User currentUser = sessionManager.getCurrentUser(sessionToken);

        if (currentUser == null) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            Label label = this.labelController.createNewLabel(newLabelDto, currentUser);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(label.getId())
                    .toUri();

            return ResponseEntity.created(location).build();

        }catch(LabelException labelException) {

            return new ResponseEntity(new HttpMessageDto(labelException.getMessage(), labelException.getDetails()), HttpStatus.CREATED);
        }

    }

    @PostMapping("/messages/{idMessage}/assign-label")
    public ResponseEntity<Label> assignLabelToMessage (@PathVariable int idMessage,
                                                       @RequestParam("label") int idLabel,
                                                       @RequestHeader("Authorization") String sessionToken){

        User currentUser = sessionManager.getCurrentUser(sessionToken);

        if (currentUser == null) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            LabelXMessage label = this.labelController.assignLabelToMessage(idMessage, idLabel, currentUser);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(label.getId())
                    .toUri();

            return ResponseEntity.created(location).build();

        }catch(LabelException labelException) {

            return new ResponseEntity(new HttpMessageDto(labelException.getMessage(), labelException.getDetails()), HttpStatus.CREATED);
        }

    }

    @DeleteMapping("/label")
    public ResponseEntity deleteLabel(@RequestParam("label") int idLabel,@RequestHeader("Authorization") String sessionToken){

        User currentUser = sessionManager.getCurrentUser(sessionToken);

        if (currentUser == null) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try{
            Label label = this.labelController.delete(idLabel);

            return new ResponseEntity(HttpStatus.OK);

        }catch (LabelException labelException){
            return new ResponseEntity(new HttpMessageDto(labelException.getMessage(), labelException.getDetails()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/users/update")
    private ResponseEntity update(@RequestBody NewUserDto newUserDto,@RequestHeader("Authorization") String sessionToken){


        User currentUser = sessionManager.getCurrentUser(sessionToken);

        if (currentUser == null) {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!currentUser.getEmployee().getMailUsername().equals(newUserDto.getMailUsername())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            User user = this.userController.update(newUserDto);

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

    @GetMapping("/users/mailUsername")
    public ResponseEntity findUserByMailUsernameLike(@RequestParam("mailUsername") String mailUsername,
                                                     @RequestHeader("Authorization") String sessionToken){

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<User> users = this.userController.findByMailUsernameLike(mailUsername);

        if(users == null ) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        else return new ResponseEntity(users, HttpStatus.OK);
    }

    @GetMapping("/users/all")
    public ResponseEntity findUserByMailUsernameLike(@RequestHeader("Authorization") String sessionToken){

        User currentUser = sessionManager.getCurrentUser(sessionToken);
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<User> users = this.userController.findAll();

        if(users == null ) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        else return new ResponseEntity(users, HttpStatus.OK);
    }

    private User getCurrentUser(String sessionToken) throws UserException {
    return Optional.ofNullable(sessionManager.getCurrentUser(sessionToken)).orElseThrow(() ->
            new UserException("User error","User not logged"));
    }
}

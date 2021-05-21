package course.springframeworkguru.messagesapirestg.controllers;

import course.springframeworkguru.messagesapirestg.dto.HttpMessageDto;
import course.springframeworkguru.messagesapirestg.dto.output.LoginDto;
import course.springframeworkguru.messagesapirestg.dto.input.NewUserDto;
import course.springframeworkguru.messagesapirestg.exceptions.UserException;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
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
    }

    @PostMapping("/")
    public ResponseEntity singIn(@RequestBody NewUserDto newUserDto){

        try {
            User user = this.userService.save(newUserDto);

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

    @PutMapping("/update")
    public ResponseEntity update(@RequestBody NewUserDto newUserDto){

        try {
            User user = this.userService.update(newUserDto);

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

    @PutMapping("/{id}/admin")
    public ResponseEntity makeAdmin(@PathVariable int id, @RequestParam("status") boolean status){
        try {
            User user = this.userService.changeAdminStatus(id, status);

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

    @GetMapping("/mailUsername/{mailUsername}")
    public ResponseEntity findByMailUsernameLike(@PathVariable String mailUsername){

        List<User> users = this.userService.findByMailUsernameLike(mailUsername);

        if (users != null) return new ResponseEntity(users, HttpStatus.OK);

        else return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/")
    public ResponseEntity findAll(){

        List<User> users = this.userService.findAll();

        if (users != null) return new ResponseEntity(users, HttpStatus.OK);

        else return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/")
    public ResponseEntity deleteUser(@RequestParam("user") int idUser){

        try{
            User user = this.userService.delete(idUser);

            return new ResponseEntity(HttpStatus.OK);

        }catch (UserException userException){
            return new ResponseEntity(new HttpMessageDto(userException.getMessage(), userException.getDetails()), HttpStatus.BAD_REQUEST);
        }
    }
}

package course.springframeworkguru.messagesapirestg.controllers;

import course.springframeworkguru.messagesapirestg.dto.LoginDto;
import course.springframeworkguru.messagesapirestg.dto.NewUserDto;
import course.springframeworkguru.messagesapirestg.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDto loginDto){

        return this.userService.login(loginDto);
    }

    @PostMapping("/")
    public ResponseEntity save(@RequestBody NewUserDto newUserDto){

        return this.userService.save(newUserDto);
    }

    @PostMapping("/update")
    public ResponseEntity update(@RequestBody NewUserDto newUserDto){

        return this.userService.update(newUserDto);
    }

    @PostMapping("/{id}/admin={status}")
    public ResponseEntity makeAdmin(@PathVariable int id, @PathVariable boolean status){

        return this.userService.changeAdminStatus(id, status);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity findById(@PathVariable int id){

        return this.userService.findById(id);
    }

    @GetMapping("/mailUsername/{mailUsername}")
    public ResponseEntity findByMailUsername(@PathVariable String mailUsername){

        return this.userService.findByMailUsername(mailUsername);
    }

    @GetMapping("/")
    public ResponseEntity findAll(){

        return this.userService.findAll();
    }

}

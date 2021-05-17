package course.springframeworkguru.messagesapirestg.controllers;

import course.springframeworkguru.messagesapirestg.services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/userFrom={id}")
    public ResponseEntity findByUserFromId(@PathVariable int id){
        return this.messageService.findByUserFromId(id);
    }

}

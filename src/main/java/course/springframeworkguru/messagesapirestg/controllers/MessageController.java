package course.springframeworkguru.messagesapirestg.controllers;

import course.springframeworkguru.messagesapirestg.dto.MessageDto;
import course.springframeworkguru.messagesapirestg.dto.NewLabelDto;
import course.springframeworkguru.messagesapirestg.services.LabelXMessageService;
import course.springframeworkguru.messagesapirestg.services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private MessageService messageService;
    private LabelXMessageService labelXMessageService;

    public MessageController(MessageService messageService, LabelXMessageService labelXMessageService) {
        this.messageService = messageService;
        this.labelXMessageService = labelXMessageService;
    }

    @GetMapping("/sent={id}")
    public ResponseEntity findByUserFromId(@PathVariable int id){
        return this.messageService.findByUserFromId(id);
    }

    @GetMapping("/inbox={id}")
    public ResponseEntity findByRecipientId(@PathVariable int id){
        return this.messageService.findByRecipientId(id);
    }

    @GetMapping("/{idMessage}/labels/user={idUser}")
    public ResponseEntity findLabelsByMessageId(@PathVariable int idMessage, @PathVariable int idUser){
        return this.labelXMessageService.findLabelsByMessageId(idMessage, idUser);
    }

    @GetMapping("/labels/user={idUser}")
    public ResponseEntity findLabelsUserByUserId(@PathVariable int idUser){
        return this.labelXMessageService.findLabelsUserByUserId(idUser);
    }
    @GetMapping("/labels/default")
    public ResponseEntity findLabelsDefault(){
        return this.labelXMessageService.findLabelsDefault();
    }

    @PostMapping("/send")
    public ResponseEntity send(@RequestBody MessageDto messageDto){
        return this.messageService.send(messageDto);
    }

    @PostMapping("/label/user={id}")
    public ResponseEntity send(@RequestBody NewLabelDto label, @PathVariable int id){
        return this.labelXMessageService.saveLabel(label,id);
    }

}

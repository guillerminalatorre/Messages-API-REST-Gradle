package course.springframeworkguru.messagesapirestg.controllers;

import course.springframeworkguru.messagesapirestg.dto.HttpMessageDto;
import course.springframeworkguru.messagesapirestg.dto.output.LabelDto;
import course.springframeworkguru.messagesapirestg.dto.output.MessageDto;
import course.springframeworkguru.messagesapirestg.dto.input.NewLabelDto;
import course.springframeworkguru.messagesapirestg.exceptions.LabelException;
import course.springframeworkguru.messagesapirestg.exceptions.MessageException;
import course.springframeworkguru.messagesapirestg.exceptions.RecipientException;
import course.springframeworkguru.messagesapirestg.models.Label;
import course.springframeworkguru.messagesapirestg.models.LabelXMessage;
import course.springframeworkguru.messagesapirestg.models.Message;
import course.springframeworkguru.messagesapirestg.services.LabelXMessageService;
import course.springframeworkguru.messagesapirestg.services.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final LabelXMessageService labelXMessageService;

    public MessageController(MessageService messageService, LabelXMessageService labelXMessageService) {
        this.messageService = messageService;
        this.labelXMessageService = labelXMessageService;
    }

    //MESSAGES

    @GetMapping(value = "/sent")
    public ResponseEntity<List<Message>> findByUserFromId(@RequestParam("user") int idUser, Pageable pageable){

        Page page = this.messageService.findByUserFromId(idUser, pageable);

        return ResponseEntity.status(HttpStatus.OK).
                header("total-count", Long.toString(page.getTotalElements())).
                header("total-pages", Long.toString(page.getTotalPages())).
                body(page.getContent());
    }

    @GetMapping("/sent/label")
    public ResponseEntity<List<Message>> findMessagesSentByLabel(@RequestParam("user") int idUser,
                                                             @RequestParam("label") int idLabel,
                                                             Pageable pageable){

        Page page = this.messageService.findByUserFromIdAndLabel(idUser, idLabel, pageable);

        return ResponseEntity.status(HttpStatus.OK).
                header("total-count", Long.toString(page.getTotalElements())).
                header("total-pages", Long.toString(page.getTotalPages())).
                body(page.getContent());
    }

    @GetMapping(value = "/inbox")
    public ResponseEntity<List<Message>> findByRecipientId(@RequestParam("user") int idUser, Pageable pageable){

        Page page = this.messageService.findByRecipientId(idUser, pageable);

        return ResponseEntity.status(HttpStatus.OK).
                header("total-count", Long.toString(page.getTotalElements())).
                header("total-pages", Long.toString(page.getTotalPages())).
                body(page.getContent());
    }

    @GetMapping("/inbox/label")
    public ResponseEntity<List<Message>> findMessagesInboxByLabel(@RequestParam("user") int idUser,
                                              @RequestParam("label") int idLabel,
                                              Pageable pageable){

        Page page = this.messageService.findByRecipientIdAndLabel(idUser, idLabel, pageable);

        return ResponseEntity.status(HttpStatus.OK).
                header("total-count", Long.toString(page.getTotalElements())).
                header("total-pages", Long.toString(page.getTotalPages())).
                body(page.getContent());
    }

    @PostMapping("/send")
    public ResponseEntity<Message> send(@RequestBody MessageDto messageDto) throws MessageException {

        try {
            Message newMessage = this.messageService.send(messageDto);

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

    //LABELS

    @GetMapping("/{idMessage}/labels")
    public ResponseEntity<LabelDto> findLabelsByMessageId(@PathVariable int idMessage, @RequestParam("user") int idUser){

        List<LabelDto> labels = this.labelXMessageService.findLabelsByMessageIdAndUserId(idMessage, idUser);

        if(labels == null ) return ResponseEntity.noContent().build();

        else return new ResponseEntity(labels, HttpStatus.OK);
    }

    @GetMapping("/labels")
    public ResponseEntity<LabelDto> findLabelsByUser(@RequestParam("user") int idUser){

        List<LabelDto> labels = this.labelXMessageService.findLabelsByUser(idUser);

        if(labels == null ) return ResponseEntity.noContent().build();

        else return new ResponseEntity(labels, HttpStatus.OK);
    }

    @PostMapping("/label")
    public ResponseEntity createNewLabel(@RequestBody NewLabelDto label, @RequestParam("user") int idUser){

        Label newLabel = this.labelXMessageService.saveNewLabel(label, idUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newLabel.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{idMessage}/assign-label")
    public ResponseEntity assignLabelToMessage(@PathVariable int idMessage, @RequestParam("label") int idLabel,
                                               @RequestParam("user") int idUser){
        LabelXMessage labelXMessage = this.labelXMessageService.assignLabelToMessage(idMessage, idLabel, idUser);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(labelXMessage.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/label")
    public ResponseEntity deleteLabel(@RequestParam("label") int idLabel){

        try{
            Label label = this.labelXMessageService.delete(idLabel);

            return new ResponseEntity(HttpStatus.OK);

        }catch (LabelException labelException){
            return new ResponseEntity(new HttpMessageDto(labelException.getMessage(), labelException.getDetails()), HttpStatus.BAD_REQUEST);
        }
    }


}

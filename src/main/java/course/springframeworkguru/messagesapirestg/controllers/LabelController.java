package course.springframeworkguru.messagesapirestg.controllers;

import course.springframeworkguru.messagesapirestg.dto.HttpMessageDto;
import course.springframeworkguru.messagesapirestg.dto.input.NewLabelDto;
import course.springframeworkguru.messagesapirestg.dto.output.LabelDto;
import course.springframeworkguru.messagesapirestg.exceptions.LabelException;
import course.springframeworkguru.messagesapirestg.models.Label;
import course.springframeworkguru.messagesapirestg.models.LabelXMessage;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.services.LabelXMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Controller
public class LabelController {

    private final LabelXMessageService labelXMessageService;

    @Autowired
    public LabelController(LabelXMessageService labelXMessageService) {
        this.labelXMessageService = labelXMessageService;
    }

    public List<LabelDto> findLabelsByMessageId(int idMessage, int idUser) {

        return this.labelXMessageService.findLabelsByMessageIdAndUserId(idMessage, idUser);
    }

    public List<LabelDto>findLabelsByUserId(int idUser){

        return this.labelXMessageService.findLabelsByUser(idUser);
    }

    public Label createNewLabel(NewLabelDto label, User user) throws LabelException {

        return this.labelXMessageService.saveNewLabel(label, user);
    }

    public LabelXMessage assignLabelToMessage(int idMessage, int idLabel, User user) throws LabelException {

        return this.labelXMessageService.assignLabelToMessage(idMessage, idLabel, user);
    }

    public Label delete(int idLabel) throws LabelException {

        return this.labelXMessageService.delete(idLabel);
    }
}

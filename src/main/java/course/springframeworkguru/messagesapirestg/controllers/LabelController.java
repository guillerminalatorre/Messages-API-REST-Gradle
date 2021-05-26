package course.springframeworkguru.messagesapirestg.controllers;

import course.springframeworkguru.messagesapirestg.dto.NewLabelDto;
import course.springframeworkguru.messagesapirestg.dto.LabelDto;
import course.springframeworkguru.messagesapirestg.exceptions.LabelException;
import course.springframeworkguru.messagesapirestg.models.Label;
import course.springframeworkguru.messagesapirestg.models.LabelXMessage;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.services.LabelXMessageService;
import course.springframeworkguru.messagesapirestg.views.LabelView;
import course.springframeworkguru.messagesapirestg.views.LabelXMessageView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class LabelController {

    private final LabelXMessageService labelXMessageService;

    @Autowired
    public LabelController(LabelXMessageService labelXMessageService) {
        this.labelXMessageService = labelXMessageService;
    }

    public List<LabelXMessageView> findLabelsByMessageId(int idMessage, int idUser) {

        return this.labelXMessageService.findLabelsByMessageIdAndUserId(idMessage, idUser);
    }

    public List<LabelView>findLabelsByUserId(int idUser){

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

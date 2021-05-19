package course.springframeworkguru.messagesapirestg.services;

import course.springframeworkguru.messagesapirestg.dto.HttpMessageDto;
import course.springframeworkguru.messagesapirestg.dto.LabelDto;
import course.springframeworkguru.messagesapirestg.dto.NewLabelDto;
import course.springframeworkguru.messagesapirestg.models.*;
import course.springframeworkguru.messagesapirestg.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LabelXMessageService {

    private final LabelDefaultXMessageRepository labelDefaultXMessageRepository;
    private final LabelUserXMessageRepository labelUserXMessageRepository;
    private final LabelRepository labelRepository;
    private final LabelUserRepository labelUserRepository;
    private final UserRepository userRepository;

    @Autowired
    public LabelXMessageService(LabelDefaultXMessageRepository labelDefaultXMessageRepository,
                                LabelUserXMessageRepository labelUserXMessageRepository,
                                LabelRepository labelRepository, LabelUserRepository labelUserRepository, UserRepository userRepository) {
        this.labelDefaultXMessageRepository = labelDefaultXMessageRepository;
        this.labelUserXMessageRepository = labelUserXMessageRepository;
        this.labelRepository = labelRepository;
        this.labelUserRepository = labelUserRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity findLabelsByMessageId(int idMessage, int idUser) {

        List<LabelDefaultXMessage> labelsDefault = this.labelDefaultXMessageRepository.findByMessageIdAndUserId(idMessage, idUser);

        List<LabelUserXMessage> labelsUsers = this.labelUserXMessageRepository.findByMessageIdAndLabelUserUserId(idMessage, idUser);

        if (!labelsUsers.isEmpty() && !labelsDefault.isEmpty()) {

            List<LabelDto> labels = new ArrayList<LabelDto>();

            if (!labelsUsers.isEmpty()) {

                for (LabelUserXMessage labelUserXMessage : labelsUsers) {

                    LabelDto label = new LabelDto();

                    label.setIdMessage(labelUserXMessage.getMessage().getId());
                    label.setName(labelUserXMessage.getLabelUser().getLabel().getName());

                    labels.add(label);
                }
            }

            if (!labelsDefault.isEmpty()) {

                for (LabelDefaultXMessage labelDefaultXMessage : labelsDefault) {

                    LabelDto label = new LabelDto();

                    label.setIdMessage(labelDefaultXMessage.getMessage().getId());
                    label.setName(labelDefaultXMessage.getLabel().getName());

                    labels.add(label);
                }
            }

            return new ResponseEntity(labels, HttpStatus.OK);

        } else return new ResponseEntity(HttpStatus.NOT_FOUND);

    }

    public ResponseEntity findLabelsDefault(){

        List<Label> labels =  this.labelRepository.findByIsDefaultTrue();

        if (!labels.isEmpty()) {

            return new ResponseEntity(labels, HttpStatus.OK);

        } else return new ResponseEntity(HttpStatus.NOT_FOUND);

    }

    public ResponseEntity findLabelsUserByUserId(int idUser){

        List<LabelUser> labelUserXMessages =  this.labelUserRepository.findByUserId(idUser);

        if (!labelUserXMessages.isEmpty()) {

            List<NewLabelDto> labels = new ArrayList<NewLabelDto>();

            for (LabelUser labelUser : labelUserXMessages) {

                NewLabelDto label = new NewLabelDto();

                label.setName(labelUser.getLabel().getName());

                labels.add(label);
            }

            return new ResponseEntity(labels, HttpStatus.OK);

        } else return new ResponseEntity(HttpStatus.NOT_FOUND);

    }

    public ResponseEntity saveLabel(NewLabelDto nameLabel, int id) {

        Label label = new Label();

        label.setName(nameLabel.getName());
        label.setDefault(false);

        if(this.labelRepository.save(label) != null) return this.saveLabelUser(label, id);

        else return new ResponseEntity(new HttpMessageDto("Label not saved",HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()),HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private ResponseEntity saveLabelUser(Label label, int idUser){

        User user = this.userRepository.findById(idUser);

        LabelUser labelUser = new LabelUser();

        labelUser.setLabel(label);
        labelUser.setUser(user);

        if(this.labelUserRepository.save(labelUser) != null) return new ResponseEntity(HttpStatus.OK);

        else return new ResponseEntity(new HttpMessageDto("Label not saved",HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()),HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

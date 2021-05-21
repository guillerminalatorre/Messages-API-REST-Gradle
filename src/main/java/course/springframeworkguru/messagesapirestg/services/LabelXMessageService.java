package course.springframeworkguru.messagesapirestg.services;

import course.springframeworkguru.messagesapirestg.dto.output.LabelDto;
import course.springframeworkguru.messagesapirestg.dto.input.NewLabelDto;
import course.springframeworkguru.messagesapirestg.exceptions.LabelException;
import course.springframeworkguru.messagesapirestg.models.*;
import course.springframeworkguru.messagesapirestg.repositories.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LabelXMessageService {


    private final LabelXMessageRepository labelXMessageRepository;
    private final LabelRepository labelRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public LabelXMessageService(LabelXMessageRepository labelXMessageRepository,
                                LabelRepository labelRepository,
                                UserRepository userRepository,
                                MessageRepository messageRepository) {
        this.labelXMessageRepository = labelXMessageRepository;
        this.labelRepository = labelRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }


    public List<LabelDto> findLabelsByMessageIdAndUserId(int idMessage, int idUser) {

        List<LabelXMessage> labelsMessage= this.labelXMessageRepository.findByMessageIdAndUserIdAndLabelIsEnabledTrue(idMessage, idUser);

        if (!labelsMessage.isEmpty()) {

            List<LabelDto> labels = new ArrayList<LabelDto>();

            if (!labelsMessage.isEmpty()) {

                for (LabelXMessage labelXMessage : labelsMessage) {

                    LabelDto label = new LabelDto();

                    label.setIdLabel(labelXMessage .getLabel().getId());
                    label.setName(labelXMessage .getLabel().getName());

                    labels.add(label);
                }
            }
            return labels;

        } else return null;

    }
    public List<LabelDto> findLabelsByUser(int id){

        List<Label> labels =  this.labelRepository.findByIsEnabledTrueAndUserIdOrUserId(id, null);

        if (!labels.isEmpty()) {

            List<LabelDto> labelsDefault = new ArrayList<LabelDto>();

            for (Label label : labels) {

                LabelDto aux = new LabelDto();

                aux.setIdLabel(label.getId());
                aux.setName(label.getName());

                labelsDefault.add(aux);
            }

            return labelsDefault;

        } else return null;
    }

    public Label saveNewLabel(NewLabelDto nameLabel, int idUser) {

        User user = this.userRepository.findByIdAndIsEnabledTrue(idUser);

        Label label = new Label();

        label.setName(nameLabel.getName());
        label.setUser(user);

        return this.labelRepository.save(label);

    }

    public LabelXMessage assignLabelToMessage(int idMessage, int idLabel, int idUser){

        Label label = this.labelRepository.findByIdAndIsEnabledTrue(idLabel);

        User user = this.userRepository.findByIdAndIsEnabledTrue(idUser);

        Message message = this.messageRepository.findById(idMessage);

        LabelXMessage labelXMessage = new LabelXMessage();

        labelXMessage.setMessage(message);
        labelXMessage.setLabel(label);
        labelXMessage.setUser(user);

        return this.labelXMessageRepository.save(labelXMessage);
    }

    public Label delete (int idLabel) throws LabelException {

        Label label = this.labelRepository.findByIdAndIsEnabledTrue(idLabel);

        if( label != null ){

            label.setEnabled(false);

            this.labelRepository.save(label);

            return label;
        }
        else throw new LabelException("Delete Label Error", "Invalid Label Id");
    }

}

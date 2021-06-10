package course.springframeworkguru.messagesapirestg.services;

import course.springframeworkguru.messagesapirestg.dto.LabelDto;
import course.springframeworkguru.messagesapirestg.dto.NewLabelDto;
import course.springframeworkguru.messagesapirestg.exceptions.LabelException;
import course.springframeworkguru.messagesapirestg.models.*;
import course.springframeworkguru.messagesapirestg.repositories.*;
import course.springframeworkguru.messagesapirestg.views.LabelView;
import course.springframeworkguru.messagesapirestg.views.LabelXMessageView;
import org.springframework.stereotype.Service;

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


    public List<LabelXMessageView> findLabelsByMessageIdAndUserId(int idMessage, int idUser) {

        List<LabelXMessageView> labels= this.labelXMessageRepository.findByMessageIdAndUserIdAndLabelIsEnabledTrue(idMessage, idUser);

        return labels;
    }

    public List<LabelView> findLabelsByUser(int idUser){

        List<LabelView> labels =  this.labelRepository.findByIsEnabledTrueAndUserIdOrUserId(idUser, null);

        return labels;
    }

    public Label saveNewLabel(NewLabelDto nameLabel, User user) throws LabelException {

        Label label = new Label();

        label.setName(nameLabel.getName());
        label.setUser(user);
        label.setEnabled(true);

        Label newLabel = this.labelRepository.save(label);

        if(newLabel != null) {

            return newLabel;
        }

        else throw new LabelException("Label Error", "New Label wasn't saved");

    }

    public LabelXMessage assignLabelToMessage(int idMessage, int idLabel, User user) throws LabelException {

        Label label = this.labelRepository.findByIdAndIsEnabledTrue(idLabel);

        Message message = this.messageRepository.findById(idMessage);

        LabelXMessage labelXMessage = new LabelXMessage();

        labelXMessage.setMessage(message);
        labelXMessage.setLabel(label);
        labelXMessage.setUser(user);

        LabelXMessage newLabelXMessage = this.labelXMessageRepository.save(labelXMessage);

        if(newLabelXMessage != null) {

            return newLabelXMessage;
        }

        else throw new LabelException("Label Error", "Label wasn't assigned to Message");
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

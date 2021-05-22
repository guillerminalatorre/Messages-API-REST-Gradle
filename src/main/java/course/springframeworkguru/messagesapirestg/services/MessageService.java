package course.springframeworkguru.messagesapirestg.services;

import course.springframeworkguru.messagesapirestg.dto.input.MessageDto;
import course.springframeworkguru.messagesapirestg.dto.output.RecipientDto;
import course.springframeworkguru.messagesapirestg.exceptions.MessageException;
import course.springframeworkguru.messagesapirestg.exceptions.RecipientException;
import course.springframeworkguru.messagesapirestg.models.Attachment;
import course.springframeworkguru.messagesapirestg.models.Message;
import course.springframeworkguru.messagesapirestg.models.Recipient;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RecipientTypeRepository recipientTypeRepository;
    private final RecipientRepository recipientRepository;
    private final AttachmentRepository attachmentRepository;
    private final LabelRepository labelRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          UserRepository userRepository,
                          RecipientTypeRepository recipientTypeRepository,
                          RecipientRepository recipientRepository,
                          AttachmentRepository attachmentRepository, LabelRepository labelRepository){

        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.recipientTypeRepository = recipientTypeRepository;
        this.recipientRepository = recipientRepository;
        this.attachmentRepository = attachmentRepository;
        this.labelRepository = labelRepository;
    }

    public Page<Message> findByUserFromId (int id, Pageable pageable){

        return this.messageRepository.findByUserFromId(id, pageable);
    }

    public Page<Message> findByRecipientId (int id, Pageable pageable){

        return this.messageRepository.findByRecipientListUserId(id, pageable);
    }
    public Page<Message> findByRecipientIdAndLabel (int idUser, int idLabel, Pageable pageable){

        return this.messageRepository.findByRecipientListUserIdAndLabelXMessageListUserIdAndLabelXMessageListLabelIdAndLabelXMessageListLabelIsEnabledTrue(idUser, idUser, idLabel, pageable);
    }

    public Page<Message> findByUserFromIdAndLabel (int idUser, int idLabel, Pageable pageable){

        return this.messageRepository.findByUserFromIdAndLabelXMessageListUserIdAndLabelXMessageListLabelIdAndLabelXMessageListLabelIsEnabledTrue(idUser, idUser, idLabel, pageable);
    }

    public Message send(MessageDto messageDto, User userFrom) throws MessageException, RecipientException {

        Message message = new Message();

        message.setBody(messageDto.getBody());
        message.setSubject(messageDto.getSubject());

        message.setUserFrom(userFrom);

        if((this.messageRepository.save(message)) != null) {

            this.saveAttachments(messageDto.getAttachments(), message);

            return this.saveRecipients(messageDto.getRecipients(), message);
        }

        else throw new MessageException("Message Error", "New Message wasn't saved");

    }

    private Message saveRecipients(List<RecipientDto> recipientsDto, Message message) throws RecipientException {

        List<Recipient> recipients = new ArrayList<Recipient>();

        StringBuilder statusRecipients = new StringBuilder("Couldn't sent mail to: ");

        for (RecipientDto recipientDto: recipientsDto) {

            User user = this.userRepository.findByEmployeeMailUsernameAndIsEnabledTrue(recipientDto.getMailUsername());

            if(user != null) {

                Recipient recipient = new Recipient();

                recipient.setMessage(message);

                recipient.setUser(user);

                recipient.setRecipientType(this.recipientTypeRepository.findByAcronym(recipientDto.getAcronymRecipientType()));

                recipients.add(recipient);
            }
            else if (statusRecipients.toString().equals("Couldn't sent mail to: ") ){

                statusRecipients.append(recipientDto.getMailUsername());

            }else statusRecipients.append(", "+recipientDto.getMailUsername());
        }

        if(!recipients.isEmpty()) this.recipientRepository.saveAll(recipients);



        if ( statusRecipients.toString().equals("Couldn't sent mail to: ") ){

            return message;
        }
        else {
            statusRecipients.append(". Message sent to valid recipients.");

            throw new RecipientException("Invalid Recipient", statusRecipients.toString());
        }
    }

    private void saveAttachments(String[] attachments, Message message) throws RecipientException {

        if(attachments.length > 0){

            List<Attachment> attachmentsList = new ArrayList<Attachment>();

            for (String attachment: attachments) {

                Attachment attach = new Attachment();

                attach.setAttachment(attachment);
                attach.setMessage(message);

                attachmentsList.add(attach);
            }

            this.attachmentRepository.saveAll(attachmentsList);
        }
    }
}

package course.springframeworkguru.messagesapirestg.services;

import course.springframeworkguru.messagesapirestg.views.MessageInboxView;
import course.springframeworkguru.messagesapirestg.dto.NewMessageDto;
import course.springframeworkguru.messagesapirestg.dto.RecipientDto;
import course.springframeworkguru.messagesapirestg.exceptions.MessageException;
import course.springframeworkguru.messagesapirestg.exceptions.RecipientException;
import course.springframeworkguru.messagesapirestg.models.Attachment;
import course.springframeworkguru.messagesapirestg.models.Message;
import course.springframeworkguru.messagesapirestg.models.Recipient;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.repositories.*;
import course.springframeworkguru.messagesapirestg.views.MessageSentView;
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

    @Autowired
    public MessageService(MessageRepository messageRepository,
                          UserRepository userRepository,
                          RecipientTypeRepository recipientTypeRepository,
                          RecipientRepository recipientRepository,
                          AttachmentRepository attachmentRepository){

        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.recipientTypeRepository = recipientTypeRepository;
        this.recipientRepository = recipientRepository;
        this.attachmentRepository = attachmentRepository;
    }

    public Page<MessageSentView> findByUserFromId (int id, Pageable pageable){

        return this.messageRepository.findDistinctByUserFromIdAndIsDeletedByUserFromFalse(id, pageable);
    }

    public Page<MessageInboxView> findByRecipientId (int id, Pageable pageable){

        return this.messageRepository.findDistinctByRecipientListUserIdAndRecipientListIsDeletedByRecipientFalse(id,pageable);
    }
    public Page<MessageInboxView> findByRecipientIdAndLabel (int idUser, int idLabel, Pageable pageable){

        return this.messageRepository.findDistinctByRecipientListUserIdAndLabelXMessageListUserIdAndLabelXMessageListLabelIdAndLabelXMessageListLabelIsEnabledTrueAndRecipientListIsDeletedByRecipientFalse(idUser, idUser, idLabel, pageable);
    }

    public Page<MessageSentView> findByUserFromIdAndLabel (int idUser, int idLabel, Pageable pageable){

        return this.messageRepository.findDistinctByUserFromIdAndLabelXMessageListUserIdAndLabelXMessageListLabelIdAndLabelXMessageListLabelIsEnabledTrueAndIsDeletedByUserFromFalse(idUser, idUser, idLabel, pageable);
    }

    public Message send(NewMessageDto newMessageDto, User userFrom) throws MessageException, RecipientException {

        Message message = new Message();

        message.setBody(newMessageDto.getBody());
        message.setSubject(newMessageDto.getSubject());

        message.setUserFrom(userFrom);

        if((this.messageRepository.save(message)) != null) {

            this.saveAttachments(newMessageDto.getAttachments(), message);

            return this.saveRecipients(newMessageDto.getRecipients(), message);
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

    public Message deleteSent (int idMessage) throws MessageException {

        Message message = this.messageRepository.findById(idMessage);

        if( message != null ){

            message.setDeletedByUserFrom(true);

            this.messageRepository.save(message);

            return message;
        }
        else throw new MessageException("Delete Message Error", "Invalid Message Id");
    }

    public Recipient deleteInbox (int idMessage, int idUser) throws MessageException {

        Recipient recipient = this.recipientRepository.findByMessageIdAndUserId(idMessage, idUser);

        if( recipient != null ){

            recipient.setDeletedByRecipient(true);

            this.recipientRepository.save(recipient);

            return recipient;
        }
        else throw new MessageException("Delete Message Error", "Invalid Message Id");
    }
}

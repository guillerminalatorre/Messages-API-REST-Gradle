package course.springframeworkguru.messagesapirestg.services;

import course.springframeworkguru.messagesapirestg.dto.HttpMessageDto;
import course.springframeworkguru.messagesapirestg.dto.MessageDto;
import course.springframeworkguru.messagesapirestg.dto.RecipientDto;
import course.springframeworkguru.messagesapirestg.models.Label;
import course.springframeworkguru.messagesapirestg.models.Message;
import course.springframeworkguru.messagesapirestg.models.Recipient;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.repositories.MessageRepository;
import course.springframeworkguru.messagesapirestg.repositories.RecipientRepository;
import course.springframeworkguru.messagesapirestg.repositories.RecipientTypeRepository;
import course.springframeworkguru.messagesapirestg.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RecipientTypeRepository recipientTypeRepository;
    private final RecipientRepository recipientRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserRepository userRepository, RecipientTypeRepository recipientTypeRepository, RecipientRepository recipientRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.recipientTypeRepository = recipientTypeRepository;
        this.recipientRepository = recipientRepository;
    }

    public ResponseEntity findByUserFromId (int id){

        List<Message> messages = this.messageRepository.findByUserFromId(id);

        if( !messages.isEmpty()){

            return new ResponseEntity(messages, HttpStatus.OK);
        }
        else return new  ResponseEntity(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity findByRecipientId (int id){

        List<Message> messages = this.messageRepository.findByRecipientListUserId(id);

        if( !messages.isEmpty()){

            return new ResponseEntity(messages, HttpStatus.OK);
        }
        else return new  ResponseEntity(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity send(MessageDto messageDto) {

        Message message = new Message();

        message.setBody(messageDto.getBody());
        message.setSubject(messageDto.getSubject());

        User userFrom = this.userRepository
                .findByEmployeeMailUsername(messageDto.getMailUsernameFrom());

        message.setUserFrom(userFrom);

        if(this.messageRepository.save(message) != null) return this.saveRecipients(messageDto.getRecipients(), message);

        else return new ResponseEntity(new HttpMessageDto("Message not sent",HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()),HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private ResponseEntity saveRecipients(List<RecipientDto> recipientsDto, Message message){
        List<Recipient> recipients = new ArrayList<Recipient>();

        StringBuilder statusRecipients = new StringBuilder("Couldn't sent mail to: ");

        for (RecipientDto recipientDto: recipientsDto) {

            User user = this.userRepository.findByEmployeeMailUsername(recipientDto.getMailUsername());

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

            return new ResponseEntity(HttpStatus.OK);
        }
        else {
            statusRecipients.append(". Message sent to valid recipients.");

            return new ResponseEntity(new HttpMessageDto("Invalid Recipient", statusRecipients.toString()),HttpStatus.OK);
        }
    }


}

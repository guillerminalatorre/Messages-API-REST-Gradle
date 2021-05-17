package course.springframeworkguru.messagesapirestg.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import course.springframeworkguru.messagesapirestg.models.Message;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public ResponseEntity findByUserFromId (int id){

        List<Message> messages = this.messageRepository.findByUserFromId(id);

        if( !messages.isEmpty()){

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String json = gson.toJson(messages);

            return new ResponseEntity(json, HttpStatus.OK);
        }
        else return new  ResponseEntity(HttpStatus.NOT_FOUND);
    }

}

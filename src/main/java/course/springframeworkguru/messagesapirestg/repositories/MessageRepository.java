package course.springframeworkguru.messagesapirestg.repositories;

import course.springframeworkguru.messagesapirestg.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findByUserFromId (int id);

    List<Message> findByRecipientListUserId (int id);
}

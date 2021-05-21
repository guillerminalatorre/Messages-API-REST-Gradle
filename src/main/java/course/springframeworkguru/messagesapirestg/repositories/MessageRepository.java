package course.springframeworkguru.messagesapirestg.repositories;

import course.springframeworkguru.messagesapirestg.models.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends PagingAndSortingRepository<Message, Integer> {

    Page<Message> findByUserFromId(int id, Pageable pageable);

    Page<Message> findByRecipientListUserId (int id, Pageable pageable);

    Page<Message> findByRecipientListUserIdAndLabelXMessageListUserIdAndLabelXMessageListLabelIdAndLabelXMessageListLabelIsEnabledTrue (int idUser, int idUser2, int idLabel, Pageable pageable);

    Page<Message> findByUserFromIdAndLabelXMessageListUserIdAndLabelXMessageListLabelIdAndLabelXMessageListLabelIsEnabledTrue(int idUser, int idUser2, int idLabel, Pageable pageable);

    Message findById(int id);
}

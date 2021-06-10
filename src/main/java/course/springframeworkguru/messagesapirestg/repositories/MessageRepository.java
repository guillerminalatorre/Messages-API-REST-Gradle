package course.springframeworkguru.messagesapirestg.repositories;

import course.springframeworkguru.messagesapirestg.models.Message;
import course.springframeworkguru.messagesapirestg.views.MessageView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends PagingAndSortingRepository<Message, Integer> {

    Page<MessageView> findDistinctByUserFromIdAndIsDeletedByUserFromFalse(int id, Pageable pageable);

    Page<MessageView> findDistinctByRecipientListUserIdAndRecipientListIsDeletedByRecipientFalse(int id, Pageable pageable);

    Page<MessageView> findDistinctByRecipientListUserIdAndLabelXMessageListUserIdAndLabelXMessageListLabelIdAndLabelXMessageListLabelIsEnabledTrueAndRecipientListIsDeletedByRecipientFalse (int idUser, int idUser2, int idLabel, Pageable pageable);

    Page<MessageView> findDistinctByUserFromIdAndLabelXMessageListUserIdAndLabelXMessageListLabelIdAndLabelXMessageListLabelIsEnabledTrueAndIsDeletedByUserFromFalse(int idUser, int idUser2, int idLabel, Pageable pageable);

    Message findById(int id);
}

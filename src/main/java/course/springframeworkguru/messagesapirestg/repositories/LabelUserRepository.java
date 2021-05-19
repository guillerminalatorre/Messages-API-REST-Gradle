package course.springframeworkguru.messagesapirestg.repositories;

import course.springframeworkguru.messagesapirestg.models.LabelUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelUserRepository extends JpaRepository<LabelUser, Integer> {

    List<LabelUser> findByUserId(int id);
}

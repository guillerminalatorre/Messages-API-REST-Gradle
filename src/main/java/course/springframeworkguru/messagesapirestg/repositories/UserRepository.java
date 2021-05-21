package course.springframeworkguru.messagesapirestg.repositories;

import course.springframeworkguru.messagesapirestg.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

    User findByIdAndIsEnabledTrue(int id);

    User findByEmployeeMailUsernameAndIsEnabledTrue(String username);

    List<User> findByEmployeeMailUsernameLikeAndIsEnabledTrue(String username);

    User save(User user);

    List<User> findByIsEnabledTrue();

}

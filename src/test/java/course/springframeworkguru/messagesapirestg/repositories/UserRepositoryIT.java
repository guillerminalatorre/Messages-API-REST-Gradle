package course.springframeworkguru.messagesapirestg.repositories;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.models.employees.Employee;
import course.springframeworkguru.messagesapirestg.utils.ObjectsFactory;
import course.springframeworkguru.messagesapirestg.views.EmployeeView;
import course.springframeworkguru.messagesapirestg.views.UserView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryIT {

    @Autowired
    UserRepository userRepository;

    private ObjectsFactory objectsFactory = new ObjectsFactory();

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void findByIdAndIsEnabledTrue(){
        User user = objectsFactory.createUser();
        user.setId(1);

        User user1 = this.userRepository.findByIdAndIsEnabledTrue(1);

        Assert.assertEquals(user, user1);
    }

    @Test
    public void findByEmployeeMailUsernameAndIsEnabledTrue(){
        User user = objectsFactory.createUser();
        user.setId(1);

        User user1 = this.userRepository.findByEmployeeMailUsernameAndIsEnabledTrue(user.getEmployee().getMailUsername());

        Assert.assertEquals(user, user1);
    }

    @Test
    public void findByEmployeeMailUsernameLikeAndIsEnabledTrue(){

        List<UserView> usersView1 = this.userRepository.findByEmployeeMailUsernameLikeAndIsEnabledTrue("x");

        Assert.assertEquals(new ArrayList<UserView>(), usersView1);
    }

    @Test
    public void save(){
        User user = objectsFactory.createUser();
        user.setId(1);

        User user1 = this.userRepository.save(user);

        Assert.assertEquals(user, user1);
    }
}
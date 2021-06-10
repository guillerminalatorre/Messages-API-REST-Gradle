package course.springframeworkguru.messagesapirestg.services;

import course.springframeworkguru.messagesapirestg.dto.LoginDto;
import course.springframeworkguru.messagesapirestg.dto.NewUserDto;
import course.springframeworkguru.messagesapirestg.exceptions.UserException;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.models.employees.Employee;
import course.springframeworkguru.messagesapirestg.repositories.EmployeeRepository;
import course.springframeworkguru.messagesapirestg.repositories.UserRepository;
import course.springframeworkguru.messagesapirestg.utils.Hash;
import course.springframeworkguru.messagesapirestg.views.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public UserService(UserRepository userRepository, EmployeeRepository employeeRepository) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
    }

    public User login(LoginDto loginDto) throws UserException {

        User user = userRepository.findByEmployeeMailUsernameAndIsEnabledTrue(loginDto.getMailUsername());

        if(user != null){

            if( loginDto.getPassword().equals( user.getPassword() )){

                return user;
            }
            else throw new UserException("Login error", "Invalid password");

        }
        else throw new UserException("Login error", "No one User mail username is : " + loginDto.getMailUsername());

    }

    public User save (NewUserDto newUserDto) throws UserException {

        Employee employee = this.employeeRepository.findByMailUsername( newUserDto.getMailUsername());

        if( employee != null ){

            if(this.userRepository.findByEmployeeMailUsernameAndIsEnabledTrue(newUserDto.getMailUsername()) == null) {

                User user = new User();

                user.setAdmin(false);
                user.setEmployee(employee);
                user.setUsername(newUserDto.getUsername());
                user.setPassword(Hash.getHash(newUserDto.getPassword()));
                user.setEnabled(true);

                this.userRepository.save(user);

                return user;
            }
            else throw new UserException("Sing in error", "Employee already is an User" + newUserDto.getMailUsername());
        }
        else throw new UserException("Sing in error", "No one Employee mail username is : " + newUserDto.getMailUsername());
    }

    public User update(NewUserDto newUserDto) throws UserException {

        Employee employee = this.employeeRepository.findByMailUsername( newUserDto.getMailUsername());

        if( employee != null ){

            User user = this.userRepository.findByEmployeeMailUsernameAndIsEnabledTrue(newUserDto.getMailUsername());

            if(user != null) {

                user.setEmployee(employee);
                user.setUsername(newUserDto.getUsername());
                user.setPassword(Hash.getHash(newUserDto.getPassword()));

                this.userRepository.save(user);

                return user;

            }else throw new UserException("Update User Error", "No one User mail username is : " + newUserDto.getMailUsername());
        }
        else throw new UserException("Update User Error", "No one Employee mail username is : " + newUserDto.getMailUsername());
    }

    public User changeAdminStatus(int id, boolean status) throws UserException {

        User user = this.userRepository.findByIdAndIsEnabledTrue(id);

        if( user != null && user.isEnabled() == true){

            user.setAdmin(status);

            this.userRepository.save(user);

            return user;
        }
        else  throw new UserException("Update User Error", "Invalid User Id");
    }

    public List<UserView> findByMailUsernameLike(String username){

        return this.userRepository.findByEmployeeMailUsernameLikeAndIsEnabledTrue("%"+username+"%");
    }

    public List<UserView> findAll() {

        return this.userRepository.findByIsEnabledTrue();
    }

    public User delete (int idUser) throws UserException {

        User user = this.userRepository.findByIdAndIsEnabledTrue(idUser);

        if( user != null ){

            user.setEnabled(false);

            this.userRepository.save(user);

            return user;
        }
        else throw new UserException("Delete User Error", "Invalid User Id");
    }

}



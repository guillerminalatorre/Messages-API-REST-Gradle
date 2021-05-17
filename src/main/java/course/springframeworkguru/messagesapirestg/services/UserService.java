package course.springframeworkguru.messagesapirestg.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import course.springframeworkguru.messagesapirestg.dto.LoginDto;
import course.springframeworkguru.messagesapirestg.dto.NewUserDto;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.models.employees.Employee;
import course.springframeworkguru.messagesapirestg.repositories.EmployeeRepository;
import course.springframeworkguru.messagesapirestg.repositories.UserRepository;
import course.springframeworkguru.messagesapirestg.utils.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
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

    public ResponseEntity login(LoginDto loginDto){

        User user = userRepository.findByEmployeeMailUsername(loginDto.getMailUsername());

        if(user != null){

            if( (Hash.getHash(loginDto.getPassword())).equals( user.getPassword() )){

                return new ResponseEntity("User loged : " + user.getUsername(), HttpStatus.OK);
            }
            else return new  ResponseEntity("Wrong password ", HttpStatus.BAD_REQUEST);

        }
        else return new  ResponseEntity("No one User mail username is : " + loginDto.getMailUsername(), HttpStatus.NOT_FOUND);

    }

    public ResponseEntity findById(int id){

        User user = userRepository.findById(id);

        if(user != null){

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String json = gson.toJson(user);

            return new ResponseEntity(json, HttpStatus.OK);

        }
        else return new  ResponseEntity("User doesn't exist", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity findByMailUsername(String username){

        User user = userRepository.findByEmployeeMailUsername(username);

        if(user != null){

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String json = gson.toJson(user);

            return new ResponseEntity(json, HttpStatus.OK);

        }
        else return new  ResponseEntity("No one User username is : " + username, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity save (NewUserDto newUserDto){

        Employee employee = this.employeeRepository.findByMailUsername( newUserDto.getMailUsername());

        if( employee != null ){

            if(this.userRepository.findByEmployeeMailUsername(newUserDto.getMailUsername()) == null) {

                User user = new User();

                user.setAdmin(false);
                user.setEmployee(employee);
                user.setUsername(newUserDto.getUsername());
                user.setPassword(Hash.getHash(newUserDto.getPassword()));

                this.userRepository.save(user);

                return new ResponseEntity(HttpStatus.CREATED);
            }
            else return new  ResponseEntity("Employee already is an User", HttpStatus.CONFLICT);
        }
        else return new  ResponseEntity("Mail Username isn't valid", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity update(NewUserDto newUserDto){

        System.out.println(newUserDto.toString());

        Employee employee = this.employeeRepository.findByMailUsername( newUserDto.getMailUsername());

        if( employee != null ){

            User user = this.userRepository.findByEmployeeMailUsername(newUserDto.getMailUsername());

            user.setEmployee(employee);
            user.setUsername(newUserDto.getUsername());
            user.setPassword(Hash.getHash(newUserDto.getPassword()));

            this.userRepository.save(user);

            return new ResponseEntity(HttpStatus.OK);
        }
        else return new  ResponseEntity("Mail Username isn't valid", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity changeAdminStatus(int id, boolean status) {

        User user = this.userRepository.findById(id);

        if( user != null ){

            user.setAdmin(status);

            this.userRepository.save(user);

            return new ResponseEntity(HttpStatus.OK);
        }
        else return new  ResponseEntity("User id isn't valid", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity findAll() {

        List<User> users = this.userRepository.findAll();

        if( !users.isEmpty()){

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String json = gson.toJson(users);

            return new ResponseEntity(json, HttpStatus.OK);
        }
        else return new  ResponseEntity(HttpStatus.NOT_FOUND);
    }
}



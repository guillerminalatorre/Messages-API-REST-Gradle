package course.springframeworkguru.messagesapirestg.views;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public interface UserView {
    @JsonUnwrapped
    EmployeeView getEmployee();
    String getUsername();

    void setEmployee(EmployeeView employee);
    void setUsername(String username);
}


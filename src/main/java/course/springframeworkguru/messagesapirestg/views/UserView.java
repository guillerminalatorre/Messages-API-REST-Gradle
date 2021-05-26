package course.springframeworkguru.messagesapirestg.views;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public interface UserView {
    @JsonUnwrapped
    EmployeeView getEmployee();
    String getUsername();
}


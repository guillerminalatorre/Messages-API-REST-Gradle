package course.springframeworkguru.messagesapirestg.utils;

import course.springframeworkguru.messagesapirestg.dto.*;
import course.springframeworkguru.messagesapirestg.models.*;
import course.springframeworkguru.messagesapirestg.models.employees.Employee;
import course.springframeworkguru.messagesapirestg.views.*;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.ArrayList;
import java.util.List;

public class ObjectsFactory {

    private ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    public ObjectsFactory() {
        super();
    }

    public NewUserDto createNewUserDto(){
        return NewUserDto.builder()
                .username("jose perez")
                .mailUsername("pepe")
                .password("1234")
                .build();
    }

    public Employee createEmployee(){
        return Employee.builder()
                .id(1L)
                .mailUsername("pepe")
                .idNumber("1234567")
                .lastName("perez")
                .name("jose")
                .city(null)
                .build();
    }

    public User createUser(){
        return User.builder()
                .id(0)
                .username("jose perez")
                .isAdmin(false)
                .isEnabled(true)
                .password("1234")
                .employee(createEmployee())
                .build();
    }

    public RecipientType createRecipientType(){
        return RecipientType.builder()
                .id(1)
                .name("Primary Receptor")
                .acronym("To").build();
    }

    public Recipient createRecipient(Message message){
        return Recipient.builder().id(1)
                .message(message)
                .recipientType(createRecipientType())
                .isDeletedByRecipient(false)
                .user(createUser())
                .build();
    }

    public Message createMessage(){
        Message message = Message.builder()
                .id(0)
                .body("Body Message")
                .subject("Subject Message")
                .labelXMessageList(null)
                .isDeletedByUserFrom(false)
                .userFrom(createUser())
                .recipientList(null)
                .attachmentsList(null)
                .build();

        List<Recipient> recipientList = new ArrayList<Recipient>();
        recipientList.add(createRecipient(message));

        message.setRecipientList(recipientList);

        return message;
    }

    public MessageView createMessageView(){
        Employee employee = createEmployee();
        EmployeeView employeeView = this.factory.createProjection(EmployeeView.class);
        employeeView.setMailUsername(employee.getMailUsername());

        User user = createUser();
        UserView userView = this.factory.createProjection(UserView.class);
        userView.setUsername(user.getUsername());
        userView.setEmployee(employeeView);

        Message message = createMessage();

        RecipientTypeView recipientTypeView = this.factory.createProjection(RecipientTypeView.class);
        recipientTypeView.setAcronym(message.getRecipientList()
                .get(0).getRecipientType().getAcronym());

        RecipientView recipientView = this.factory.createProjection(RecipientView.class);
        recipientView.setUser(userView);
        recipientView.setRecipientType(recipientTypeView);

        List<RecipientView> recipientViewList =  new ArrayList<RecipientView>();
        recipientViewList.add(recipientView);

        List<AttachmentView> attachmentViews = new ArrayList<AttachmentView>();

        MessageView messageView = this.factory.createProjection(MessageView.class);
        messageView.setId(message.getId());
        messageView.setDatee(message.getDatee().toString());
        messageView.setUserFrom(userView);
        messageView.setAttachmentsList(null);
        messageView.setBody(message.getBody());
        messageView.setSubject(message.getSubject());
        messageView.setRecipientList(recipientViewList);

        return messageView;
    }

    public RecipientDto createRecipientDto(){
        return RecipientDto.builder().acronymRecipientType("To").mailUsername("pepe").build();
    }

    public NewMessageDto createNewMessageDto(){
        List<RecipientDto> recipientDtos = new ArrayList<RecipientDto>();
        recipientDtos.add(createRecipientDto());

        return NewMessageDto.builder()
                .body("Body Message")
                .subject("Subject Message")
                .attachments(null)
                .recipients(recipientDtos)
                .build();
    }

    public Label createLabel(){
        return Label.builder()
                .id(0)
                .name("Work")
                .isEnabled(true)
                .user(createUser())
                .build();
    }

    public LabelXMessage createLabelXMessage(){
        return LabelXMessage.builder()
                .id(0)
                .user(createUser())
                .label(createLabel())
                .message(createMessage())
                .build();
    }

    public LabelView createLabelView(){
        Label label = createLabel();
        LabelView labelView = this.factory.createProjection(LabelView.class);
        labelView.setName(label.getName());
        labelView.setId(label.getId());

        return labelView;
    }

    public LabelXMessageView createLabelXMessageView(){
        LabelXMessageView labelXMessageView = this.factory.createProjection(LabelXMessageView.class);
        labelXMessageView.setLabel(createLabelView());

        return labelXMessageView;
    }

    public NewLabelDto createNewLabelDto(){
        return NewLabelDto.builder().name("Work").build();
    }

    public LoginDto createLoginDto(){
        return new LoginDto("pepe", "1234");
    }

}

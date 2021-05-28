package course.springframeworkguru.messagesapirestg.controllers;

import course.springframeworkguru.messagesapirestg.dto.NewLabelDto;
import course.springframeworkguru.messagesapirestg.exceptions.LabelException;
import course.springframeworkguru.messagesapirestg.models.Label;
import course.springframeworkguru.messagesapirestg.models.LabelXMessage;
import course.springframeworkguru.messagesapirestg.models.Message;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.models.employees.Employee;
import course.springframeworkguru.messagesapirestg.services.LabelXMessageService;
import course.springframeworkguru.messagesapirestg.views.LabelView;
import course.springframeworkguru.messagesapirestg.views.LabelXMessageView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class LabelControllerTest {

    @Mock
    private LabelXMessageService labelXMessageService;

    private LabelController labelController;

    private ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.labelController = new LabelController(labelXMessageService);
    }

    private Employee createEmployee(){
        return Employee.builder()
                .id(1L)
                .mailUsername("pepe")
                .idNumber("1234567")
                .lastName("perez")
                .name("jose")
                .city(null)
                .build();
    }

    private User createUser(){
        return User.builder()
                .id(0)
                .username("jose perez")
                .isAdmin(false)
                .isEnabled(true)
                .password("1234")
                .employee(createEmployee())
                .build();
    }

    private Message createMessage(){
        return Message.builder()
                .id(0)
                .body("Body Message")
                .subject("Subject Message")
                .labelXMessageList(null)
                .isDeletedByUserFrom(false)
                .userFrom(createUser())
                .recipientList(null)
                .attachmentsList(null)
                .recipientList(null)
                .build();
    }

    private Label createLabel(){
        return Label.builder()
                .id(0)
                .name("Work")
                .isEnabled(true)
                .user(createUser())
                .build();
    }

    private LabelXMessage createLabelXMessage(){
        return LabelXMessage.builder()
                .id(0)
                .user(createUser())
                .label(createLabel())
                .message(createMessage())
                .build();
    }

    private LabelView createLabelView(){
        Label label = createLabel();
        LabelView labelView = this.factory.createProjection(LabelView.class);
        labelView.setName(label.getName());
        labelView.setId(label.getId());

        return labelView;
    }

    private LabelXMessageView createLabelXMessageView(){
        LabelXMessageView labelXMessageView = this.factory.createProjection(LabelXMessageView.class);
        labelXMessageView.setLabel(createLabelView());

        return labelXMessageView;
    }

    private NewLabelDto createNewLabelDto(){
        return NewLabelDto.builder().name("Work").build();
    }

    @Test
    public void findLabelsByMessageId() {

        Message message = createMessage();
        User user = createUser();

        List<LabelXMessageView> labels = new ArrayList<LabelXMessageView>();
        labels.add(createLabelXMessageView());

        when(this.labelXMessageService.findLabelsByMessageIdAndUserId(message.getId(), user.getId())).thenReturn(labels);

        List<LabelXMessageView> labels1 = this.labelController.findLabelsByMessageId(message.getId(), user.getId());

        Assert.assertEquals(labels, labels1);
    }

    @Test
    public void findLabelsByUserId() {
        User user = createUser();

        List<LabelView> labels = new ArrayList<LabelView>();
        labels.add(createLabelView());

        when(this.labelXMessageService.findLabelsByUser(user.getId())).thenReturn(labels);

        List<LabelView> labels1 = this.labelController.findLabelsByUserId(user.getId());

        Assert.assertEquals(labels, labels1);
    }

    @Test
    public void createNewLabelOk() throws LabelException {

        NewLabelDto newLabelDto = createNewLabelDto();
        User user = createUser();

        Label auxLabel = createLabel();
        auxLabel.setName(newLabelDto.getName());
        auxLabel.setUser(user);

        Label label = createLabel();

        when(this.labelXMessageService.saveNewLabel(newLabelDto, user)).thenReturn(label);

        Label label1 = this.labelController.createNewLabel(newLabelDto, user);

        Assert.assertEquals(label,label1);
    }

    @Test(expected = LabelException.class)
    public void createNewLabelFail() throws LabelException {

        NewLabelDto newLabelDto = createNewLabelDto();
        User user = createUser();

        when(this.labelXMessageService.saveNewLabel(newLabelDto, user)).thenThrow(LabelException.class);

        Label label1 = this.labelController.createNewLabel(newLabelDto, user);
    }

    @Test
    public void assignLabelToMessageOk() throws LabelException {

        Label label = createLabel();
        Message message = createMessage();
        User user = createUser();
        LabelXMessage labelXMessage = createLabelXMessage();

        when(this.labelXMessageService.assignLabelToMessage(message.getId(), label.getId(), user)).thenReturn(labelXMessage);

        LabelXMessage labelXMessage1 =  this.labelController.assignLabelToMessage(message.getId(), label.getId(), user);

        Assert.assertEquals(labelXMessage, labelXMessage1);
    }

    @Test(expected = LabelException.class)
    public void assignLabelToMessageFail() throws LabelException {

        Label label = createLabel();
        Message message = createMessage();
        User user = createUser();
        LabelXMessage labelXMessage = createLabelXMessage();

        when(this.labelXMessageService.assignLabelToMessage(message.getId(), label.getId(), user)).thenThrow(LabelException.class);

        LabelXMessage labelXMessage1 =  this.labelController.assignLabelToMessage(message.getId(), label.getId(), user);
    }

    @Test
    public void deleteOk() throws LabelException {
        Label label = createLabel();
        label.setEnabled(false);

        when(this.labelXMessageService.delete(label.getId())).thenReturn(label);

        Label label1 =  this.labelController.delete(label.getId());

        Assert.assertEquals(label,label1);
    }

    @Test(expected = LabelException.class)
    public void deleteFail() throws LabelException {
        Label label = createLabel();
        label.setEnabled(false);

        when(this.labelXMessageService.delete(label.getId())).thenThrow(LabelException.class);

        Label label1 =  this.labelController.delete(label.getId());
    }
}
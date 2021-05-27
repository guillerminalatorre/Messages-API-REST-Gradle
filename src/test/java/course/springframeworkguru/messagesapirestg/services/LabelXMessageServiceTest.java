package course.springframeworkguru.messagesapirestg.services;

import course.springframeworkguru.messagesapirestg.dto.NewLabelDto;
import course.springframeworkguru.messagesapirestg.exceptions.LabelException;
import course.springframeworkguru.messagesapirestg.models.*;
import course.springframeworkguru.messagesapirestg.models.employees.Employee;
import course.springframeworkguru.messagesapirestg.repositories.LabelRepository;
import course.springframeworkguru.messagesapirestg.repositories.LabelXMessageRepository;
import course.springframeworkguru.messagesapirestg.repositories.MessageRepository;
import course.springframeworkguru.messagesapirestg.repositories.UserRepository;
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

public class LabelXMessageServiceTest {

    @Mock
    private LabelXMessageRepository labelXMessageRepository;
    @Mock
    private LabelRepository labelRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MessageRepository messageRepository;

    private LabelXMessageService labelXMessageService;

    private ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.labelXMessageService = new LabelXMessageService(labelXMessageRepository, labelRepository, userRepository, messageRepository);
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
    public void findLabelsByMessageIdAndUserId() {
        Message message = createMessage();
        User user = createUser();

        List<LabelXMessageView> labels = new ArrayList<LabelXMessageView>();
        labels.add(createLabelXMessageView());

        when(this.labelXMessageRepository.findByMessageIdAndUserIdAndLabelIsEnabledTrue(message.getId(), user.getId())).thenReturn(labels);

        List<LabelXMessageView> labels1 = this.labelXMessageService.findLabelsByMessageIdAndUserId(message.getId(), user.getId());

        Assert.assertEquals(labels, labels1);
    }

    @Test
    public void findLabelsByUser() {
        User user = createUser();

        List<LabelView> labels = new ArrayList<LabelView>();
        labels.add(createLabelView());

        when(this.labelRepository.findByIsEnabledTrueAndUserIdOrUserId(user.getId(), null)).thenReturn(labels);

        List<LabelView> labels1 = this.labelXMessageService.findLabelsByUser(user.getId());

        Assert.assertEquals(labels, labels1);
    }

    @Test
    public void saveNewLabelOk() throws LabelException {
        NewLabelDto newLabelDto = createNewLabelDto();
        User user = createUser();

        Label auxLabel = createLabel();
        auxLabel.setName(newLabelDto.getName());
        auxLabel.setUser(user);

        Label label = createLabel();

        when(this.labelRepository.save(auxLabel)).thenReturn(label);

        Label label1 = this.labelXMessageService.saveNewLabel(newLabelDto, user);

        Assert.assertEquals(label,label1);
    }

    @Test(expected = LabelException.class)
    public void saveNewLabelFail() throws LabelException {
        NewLabelDto newLabelDto = createNewLabelDto();
        User user = createUser();

        Label label = createLabel();

        when(this.labelRepository.save(label)).thenReturn(null);

        Label label1 = this.labelXMessageService.saveNewLabel(newLabelDto, user);
    }


    @Test
    public void assignLabelToMessageOk() throws LabelException {
        Label label = createLabel();
        Message message = createMessage();
        User user = createUser();
        LabelXMessage labelXMessage = createLabelXMessage();

        LabelXMessage auxLabelXMessage = new LabelXMessage();
        auxLabelXMessage.setId(0);
        auxLabelXMessage.setMessage(message);
        auxLabelXMessage.setLabel(label);
        auxLabelXMessage.setUser(user);

        when(this.labelRepository.findByIdAndIsEnabledTrue(label.getId())).thenReturn(label);
        when(this.messageRepository.findById(message.getId())).thenReturn(message);
        when(this.labelXMessageRepository.save(auxLabelXMessage)).thenReturn(labelXMessage);

        LabelXMessage labelXMessage1 =  this.labelXMessageService.assignLabelToMessage(message.getId(), label.getId(), user);

        Assert.assertEquals(labelXMessage, labelXMessage1);
    }

    @Test(expected = LabelException.class)
    public void assignLabelToMessageFail() throws LabelException {
        Label label = createLabel();
        Message message = createMessage();
        User user = createUser();
        LabelXMessage labelXMessage = createLabelXMessage();

        when(this.labelRepository.findByIdAndIsEnabledTrue(label.getId())).thenReturn(label);
        when(this.messageRepository.findById(message.getId())).thenReturn(message);
        when(this.labelXMessageRepository.save(labelXMessage)).thenReturn(null);

        LabelXMessage labelXMessage1 =  this.labelXMessageService.assignLabelToMessage(message.getId(), label.getId(), user);
    }

    @Test
    public void deleteOk() throws LabelException {
        Label label = createLabel();
        label.setEnabled(false);

        when(this.labelRepository.findByIdAndIsEnabledTrue(label.getId())).thenReturn(label);

        Label label1 =  this.labelXMessageService.delete(label.getId());

        Assert.assertEquals(label,label1);
    }

    @Test(expected = LabelException.class)
    public void deleteFail() throws LabelException {
        Label label = createLabel();

        when(this.labelRepository.findByIdAndIsEnabledTrue(label.getId())).thenReturn(null);

        Label label1 =  this.labelXMessageService.delete(label.getId());
    }
}
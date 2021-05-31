package course.springframeworkguru.messagesapirestg.services;

import course.springframeworkguru.messagesapirestg.dto.NewLabelDto;
import course.springframeworkguru.messagesapirestg.exceptions.LabelException;
import course.springframeworkguru.messagesapirestg.models.*;
import course.springframeworkguru.messagesapirestg.repositories.LabelRepository;
import course.springframeworkguru.messagesapirestg.repositories.LabelXMessageRepository;
import course.springframeworkguru.messagesapirestg.repositories.MessageRepository;
import course.springframeworkguru.messagesapirestg.repositories.UserRepository;
import course.springframeworkguru.messagesapirestg.utils.ObjectsFactory;
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
    
    private ObjectsFactory objectsFactory = new ObjectsFactory();

    private ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.labelXMessageService = new LabelXMessageService(labelXMessageRepository, labelRepository, userRepository, messageRepository);
    }

    @Test
    public void findLabelsByMessageIdAndUserId() {
        Message message = objectsFactory.createMessage();
        User user = objectsFactory.createUser();

        List<LabelXMessageView> labels = new ArrayList<LabelXMessageView>();
        labels.add(objectsFactory.createLabelXMessageView());

        when(this.labelXMessageRepository.findByMessageIdAndUserIdAndLabelIsEnabledTrue(message.getId(), user.getId())).thenReturn(labels);

        List<LabelXMessageView> labels1 = this.labelXMessageService.findLabelsByMessageIdAndUserId(message.getId(), user.getId());

        Assert.assertEquals(labels, labels1);
    }

    @Test
    public void findLabelsByUser() {
        User user = objectsFactory.createUser();

        List<LabelView> labels = new ArrayList<LabelView>();
        labels.add(objectsFactory.createLabelView());

        when(this.labelRepository.findByIsEnabledTrueAndUserIdOrUserId(user.getId(), null)).thenReturn(labels);

        List<LabelView> labels1 = this.labelXMessageService.findLabelsByUser(user.getId());

        Assert.assertEquals(labels, labels1);
    }

    @Test
    public void saveNewLabelOk() throws LabelException {
        NewLabelDto newLabelDto = objectsFactory.createNewLabelDto();
        User user = objectsFactory.createUser();

        Label auxLabel = objectsFactory.createLabel();
        auxLabel.setName(newLabelDto.getName());
        auxLabel.setUser(user);

        Label label = objectsFactory.createLabel();

        when(this.labelRepository.save(auxLabel)).thenReturn(label);

        Label label1 = this.labelXMessageService.saveNewLabel(newLabelDto, user);

        Assert.assertEquals(label,label1);
    }

    @Test(expected = LabelException.class)
    public void saveNewLabelFail() throws LabelException {
        NewLabelDto newLabelDto = objectsFactory.createNewLabelDto();
        User user = objectsFactory.createUser();

        Label label = objectsFactory.createLabel();

        when(this.labelRepository.save(label)).thenReturn(null);

        Label label1 = this.labelXMessageService.saveNewLabel(newLabelDto, user);
    }


    @Test
    public void assignLabelToMessageOk() throws LabelException {
        Label label = objectsFactory.createLabel();
        Message message = objectsFactory.createMessage();
        User user = objectsFactory.createUser();
        LabelXMessage labelXMessage = objectsFactory.createLabelXMessage();

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
        Label label = objectsFactory.createLabel();
        Message message = objectsFactory.createMessage();
        User user = objectsFactory.createUser();
        LabelXMessage labelXMessage = objectsFactory.createLabelXMessage();

        when(this.labelRepository.findByIdAndIsEnabledTrue(label.getId())).thenReturn(label);
        when(this.messageRepository.findById(message.getId())).thenReturn(message);
        when(this.labelXMessageRepository.save(labelXMessage)).thenReturn(null);

        LabelXMessage labelXMessage1 =  this.labelXMessageService.assignLabelToMessage(message.getId(), label.getId(), user);
    }

    @Test
    public void deleteOk() throws LabelException {
        Label label = objectsFactory.createLabel();
        label.setEnabled(false);

        when(this.labelRepository.findByIdAndIsEnabledTrue(label.getId())).thenReturn(label);

        Label label1 =  this.labelXMessageService.delete(label.getId());

        Assert.assertEquals(label,label1);
    }

    @Test(expected = LabelException.class)
    public void deleteFail() throws LabelException {
        Label label = objectsFactory.createLabel();

        when(this.labelRepository.findByIdAndIsEnabledTrue(label.getId())).thenReturn(null);

        Label label1 =  this.labelXMessageService.delete(label.getId());
    }
}
package course.springframeworkguru.messagesapirestg.controllers;

import course.springframeworkguru.messagesapirestg.dto.NewLabelDto;
import course.springframeworkguru.messagesapirestg.exceptions.LabelException;
import course.springframeworkguru.messagesapirestg.models.Label;
import course.springframeworkguru.messagesapirestg.models.LabelXMessage;
import course.springframeworkguru.messagesapirestg.models.Message;
import course.springframeworkguru.messagesapirestg.models.User;
import course.springframeworkguru.messagesapirestg.services.LabelXMessageService;
import course.springframeworkguru.messagesapirestg.utils.ObjectsFactory;
import course.springframeworkguru.messagesapirestg.views.LabelView;
import course.springframeworkguru.messagesapirestg.views.LabelXMessageView;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
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
    
    private ObjectsFactory objectsFactory =  new ObjectsFactory();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        this.labelController = new LabelController(labelXMessageService);
    }

    @Test
    public void findLabelsByMessageId() {

        Message message = objectsFactory.createMessage();
        User user = objectsFactory.createUser();

        List<LabelXMessageView> labels = new ArrayList<LabelXMessageView>();
        labels.add(objectsFactory.createLabelXMessageView());

        when(this.labelXMessageService.findLabelsByMessageIdAndUserId(message.getId(), user.getId())).thenReturn(labels);

        List<LabelXMessageView> labels1 = this.labelController.findLabelsByMessageId(message.getId(), user.getId());

        Assert.assertEquals(labels, labels1);
    }

    @Test
    public void findLabelsByUserId() {
        User user = objectsFactory.createUser();

        List<LabelView> labels = new ArrayList<LabelView>();
        labels.add(objectsFactory.createLabelView());

        when(this.labelXMessageService.findLabelsByUser(user.getId())).thenReturn(labels);

        List<LabelView> labels1 = this.labelController.findLabelsByUserId(user.getId());

        Assert.assertEquals(labels, labels1);
    }

    @Test
    public void createNewLabelOk() throws LabelException {

        NewLabelDto newLabelDto = objectsFactory.createNewLabelDto();
        User user = objectsFactory.createUser();

        Label auxLabel = objectsFactory.createLabel();
        auxLabel.setName(newLabelDto.getName());
        auxLabel.setUser(user);

        Label label = objectsFactory.createLabel();

        when(this.labelXMessageService.saveNewLabel(newLabelDto, user)).thenReturn(label);

        Label label1 = this.labelController.createNewLabel(newLabelDto, user);

        Assert.assertEquals(label,label1);
    }

    @Test(expected = LabelException.class)
    public void createNewLabelFail() throws LabelException {

        NewLabelDto newLabelDto = objectsFactory.createNewLabelDto();
        User user = objectsFactory.createUser();

        when(this.labelXMessageService.saveNewLabel(newLabelDto, user)).thenThrow(LabelException.class);

        Label label1 = this.labelController.createNewLabel(newLabelDto, user);
    }

    @Test
    public void assignLabelToMessageOk() throws LabelException {

        Label label = objectsFactory.createLabel();
        Message message = objectsFactory.createMessage();
        User user = objectsFactory.createUser();
        LabelXMessage labelXMessage = objectsFactory.createLabelXMessage();

        when(this.labelXMessageService.assignLabelToMessage(message.getId(), label.getId(), user)).thenReturn(labelXMessage);

        LabelXMessage labelXMessage1 =  this.labelController.assignLabelToMessage(message.getId(), label.getId(), user);

        Assert.assertEquals(labelXMessage, labelXMessage1);
    }

    @Test(expected = LabelException.class)
    public void assignLabelToMessageFail() throws LabelException {

        Label label = objectsFactory.createLabel();
        Message message = objectsFactory.createMessage();
        User user = objectsFactory.createUser();
        LabelXMessage labelXMessage = objectsFactory.createLabelXMessage();

        when(this.labelXMessageService.assignLabelToMessage(message.getId(), label.getId(), user)).thenThrow(LabelException.class);

        LabelXMessage labelXMessage1 =  this.labelController.assignLabelToMessage(message.getId(), label.getId(), user);
    }

    @Test
    public void deleteOk() throws LabelException {
        Label label = objectsFactory.createLabel();
        label.setEnabled(false);

        when(this.labelXMessageService.delete(label.getId())).thenReturn(label);

        Label label1 =  this.labelController.delete(label.getId());

        Assert.assertEquals(label,label1);
    }

    @Test(expected = LabelException.class)
    public void deleteFail() throws LabelException {
        Label label = objectsFactory.createLabel();
        label.setEnabled(false);

        when(this.labelXMessageService.delete(label.getId())).thenThrow(LabelException.class);

        Label label1 =  this.labelController.delete(label.getId());
    }
}
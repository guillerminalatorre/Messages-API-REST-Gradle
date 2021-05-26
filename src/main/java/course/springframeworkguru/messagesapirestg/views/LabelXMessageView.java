package course.springframeworkguru.messagesapirestg.views;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public interface LabelXMessageView {
    @JsonUnwrapped
    LabelView getLabel();
}

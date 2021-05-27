package course.springframeworkguru.messagesapirestg.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewLabelDto {
    private String name;
}

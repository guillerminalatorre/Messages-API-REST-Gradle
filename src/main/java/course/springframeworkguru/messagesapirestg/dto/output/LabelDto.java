package course.springframeworkguru.messagesapirestg.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelDto {
    private String name;
    private int idLabel;
}

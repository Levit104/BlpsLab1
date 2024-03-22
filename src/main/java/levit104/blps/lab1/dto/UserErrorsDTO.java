package levit104.blps.lab1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserErrorsDTO {
    private String field;
    private List<String> errorsMessages;
    private Object rejectedValue;
}

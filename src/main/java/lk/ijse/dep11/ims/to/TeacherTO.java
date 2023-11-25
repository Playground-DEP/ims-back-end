package lk.ijse.dep11.ims.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherTO implements Serializable {

    @Null(message = "ID should be empty")
    private Integer id;
    @NotBlank(message = "Name should not be empty")
    private String name;
    @NotBlank(message = "Contact number should not be empty")
    private String contact;
}

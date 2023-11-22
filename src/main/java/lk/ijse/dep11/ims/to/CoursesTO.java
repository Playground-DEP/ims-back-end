package lk.ijse.dep11.ims.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoursesTO implements Serializable {

    @Null(message = "courses id can't be empty")
    private Integer id;
    @NotBlank(message = "courses name should not be empty")
    @Pattern(regexp = "^[A-Za-z0-9\\-]{2,}$", message = "name invalid")
    private String name;
    @NotBlank(message = "courses duration should not be empty")
    @Positive(message = "Invalid duration")
    private Integer durationMonths;
}

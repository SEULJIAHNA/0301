package shareYourFashion.main.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shareYourFashion.main.domain.*;

import javax.persistence.Lob;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BoardUpdateDTO {

    @NotEmpty(message = "board's id is not exists.")
    private Long id;
    @NotEmpty(message = "board's content is not exists")
    private String content;
    @NotEmpty(message = "board's title is not exists")
    private String title;


    @Builder
    public BoardUpdateDTO(Long id, String title, String content){
        this.id =id;
        this.title = title;
        this.content = content;
    }

}
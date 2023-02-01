package shareYourFashion.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shareYourFashion.main.domain.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor

public class BoardSaveRequestDTO {

    @NotEmpty(message = "board's content is not exists")
    private String content;
    @NotEmpty(message = "board's title is not exists")
    private String title;
//    @NotEmpty(message = "board's author is not exists")
//    private User author;


    @Builder
    public BoardSaveRequestDTO(String title, String content){
        this.title= title;
        this.content= content;
//        this.author= author;
    }


    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
//                .author(author)
                .build();
    }
}

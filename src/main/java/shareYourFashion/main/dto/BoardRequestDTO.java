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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardRequestDTO {

    @NotEmpty(message = "board's id is not exists.")
    private Long id;
    @NotEmpty(message = "board's content is not exists")

    private String content;
    private int view;
    @NotEmpty(message = "board's title is not exists")
    private String title;
    @NotEmpty(message = "board's author is not exists")

    private User author;
    private List<Comment> comments = new ArrayList<>();
    private List<HashTag> hashTags = new ArrayList<>();
    private List<Like> likes = new ArrayList<>();
    private Thumbnail thumbnail;
    private LocalDateTime createdDate;
    private LocalDateTime LastModifiedDate;


    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .author(author)
                .view(view)
                .build();
    }
}

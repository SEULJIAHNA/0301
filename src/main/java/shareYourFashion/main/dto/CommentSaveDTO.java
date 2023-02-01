package shareYourFashion.main.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import shareYourFashion.main.domain.Comment;

public class CommentSaveDTO {
    private final String content;

    public CommentSaveDTO(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
    public Comment toEntity() {
        return Comment.builder().content(content).build();
    }
}

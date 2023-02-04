package shareYourFashion.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shareYourFashion.main.domain.Board;
import shareYourFashion.main.domain.Comment;

@Data
@Getter
@NoArgsConstructor
public class CommentSaveDTO {
    private String content;
    private Board board;


    public CommentSaveDTO(String content , Board board) {
        this.content = content;
        this.board = board;
    }

    public Comment toEntity() {

        return Comment.builder().content(content).board(board).build();
    }
}

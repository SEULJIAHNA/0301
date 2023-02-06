package shareYourFashion.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shareYourFashion.main.domain.Board;
import shareYourFashion.main.domain.Comment;
import shareYourFashion.main.domain.User;

import java.io.Serializable;

@Data
@Getter
@NoArgsConstructor
public class CommentSaveDTO {
    private String content;
    private Board board;

    private User writer;
    private Comment parent;
    private int cDepth;



    public CommentSaveDTO(String content , Board board, Comment parent, int cDepth){
        this.content = content;
        this.board = board;
        this.parent = parent;
        this.cDepth = cDepth;
    }

    public Comment toEntity() {

        return Comment.builder().content(content).board(board).build();
    }
}

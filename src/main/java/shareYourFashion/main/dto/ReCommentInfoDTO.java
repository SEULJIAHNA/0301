package shareYourFashion.main.dto;


import lombok.Data;
import shareYourFashion.main.domain.Comment;

@Data
public class ReCommentInfoDTO {

    private final static String DEFAULT_DELETE_MESSAGE = "삭제된 댓글입니다";

    private Long postId;
    private Long parentId;


    private Long reCommentId;
    private String content;
    private boolean isRemoved;

    private UserInfoDTO writerDTO;

    public ReCommentInfoDTO(Comment reComment){
        this.postId = reComment.getBoard().getId();
        this.parentId = reComment.getParent().getId();
        this.reCommentId = reComment.getId();
        this.content = reComment.getContent();

        if(reComment.isRemoved()){
            this.content = DEFAULT_DELETE_MESSAGE;
        }

        this.isRemoved = reComment.isRemoved();
        this.writerDTO = new UserInfoDTO(reComment.getWriter());

    }

}

package shareYourFashion.main.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import shareYourFashion.main.domain.Board;
import shareYourFashion.main.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CommentInfoDTO {
    private final static String DEFAULT_DELETE_MESSAGE = "삭제된 댓글입니다.";

    private Long commentId;//해당 댓글의 ID

    private String content;//내용 (삭제되었다면 "삭제된 댓글입니다 출력")

    private UserInfoDTO writerDto;//댓글 작성자에 대한 정보 //User랑 맞춰봐야함

    private Long boardId;//댓글이 달린 POST의 ID

    private boolean isRemoved;//삭제되었는지?

    private List<ReCommentInfoDTO> reCommentListDTOList;//대댓글에 대한 정보들

    private LocalDateTime createdDate;

    private LocalDateTime LastModifiedDate;

    private List<Comment> comments;

    public CommentInfoDTO(Long commentId, String content, UserInfoDTO writerDto, Long boardId, boolean isRemoved, List<ReCommentInfoDTO> reCommentListDTOList, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.commentId = commentId;
        this.content = content;
        this.writerDto = writerDto;
        this.boardId = boardId;
        this.isRemoved = isRemoved;
        this.reCommentListDTOList = reCommentListDTOList;
        this.createdDate = createdDate;
        LastModifiedDate = lastModifiedDate;
    }

    /**
     * 삭제되었을 경우 삭제된 댓글입니다 출력
     */

    public CommentInfoDTO(Comment comment, List<Comment> reCommentList) {

        this.boardId = comment.getBoard().getId();
        this.commentId = comment.getId();
        this.content = comment.getContent();

        if(comment.isRemoved()){
            this.content = DEFAULT_DELETE_MESSAGE;
        }

        this.isRemoved = comment.isRemoved();



        this.writerDto = new UserInfoDTO(comment.getWriter());//User랑 상의해야함

        this.reCommentListDTOList = reCommentList.stream().map(ReCommentInfoDTO::new)
                .collect(Collectors.toList());

    }


}

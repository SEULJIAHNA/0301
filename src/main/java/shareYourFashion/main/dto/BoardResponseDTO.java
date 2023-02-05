package shareYourFashion.main.dto;

import lombok.Data;
import shareYourFashion.main.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class BoardResponseDTO {

    private Long id;
    private String content;
    private int view;
    private String title;
    private User author;
    private List<CommentInfoDTO> commentInfoDtoList;//댓글 정보들
    private List<HashTag> hashTags = new ArrayList<>();
    private List<Like> likes = new ArrayList<>();
    private Thumbnail thumbnail;
    private LocalDateTime createdDate;
    private LocalDateTime LastModifiedDate;
    private boolean isRemoved;



    public BoardResponseDTO(Board entity) {
        this.id = entity.getId();
        this.content = entity.getContent();
        this.view = entity.getView();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.hashTags = entity.getHashTags();
        this.likes = entity.getLikes();
        this.thumbnail = entity.getThumbnail();
        this.createdDate = entity.getCreatedDate();
        this.LastModifiedDate = entity.getLastModifiedDate();

        this.isRemoved = entity.isRemoved();
        /**
         * 댓글과 대댓글을 그룹짓기 board.getComments()는 댓글과 대댓글이 모두 조회된다.
         */

        Map<Comment, List<Comment>> commentListMap = entity.getComments().stream()
                .filter(comment -> comment.getParent() != null)
                //.filter(comment -> comment.isRemoved() == false)
                .collect(Collectors.groupingBy(Comment::getParent));

        /**
         * 댓글과 대댓글을 통해 CommentInfoDto 생성
         */
        commentInfoDtoList = commentListMap.keySet()
                .stream()
                .map(comment -> new CommentInfoDTO(comment, commentListMap.get(comment)))
                .collect(Collectors.toList());
    }

}

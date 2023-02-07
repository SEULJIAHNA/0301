package shareYourFashion.main.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import shareYourFashion.main.dto.CommentInfoDTO;
import shareYourFashion.main.dto.ReCommentInfoDTO;
import shareYourFashion.main.dto.UserInfoDTO;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "comment")
@Getter
@Setter

public class Comment extends BaseTimeEntity {

    private final static String DEFAULT_DELETE_MESSAGE = "삭제된 댓글입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lookBook_id")
    private LookBook lookBook;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Max(2)
    @Min(0)
    private int cDepth;

    private boolean isRemoved= false;




    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @Builder.Default // 특정 속성에 기본값을 지정
    private List<Comment> children = new ArrayList<>();

    private String isParent;

    //== 생성자 ==//
    public Comment() {
    }



//= 연관관계 메소드 =//
//    public void setBoardComment(Board board){
//        this.board = board;
//        board.getComments().add(this);
//    } //광현이가 한거

    public void confirmBoard(Board board) {
        this.board = board;
        board.addComment(this);
    }

    public void confirmWriter(User writer) {
        this.writer = writer;
        writer.addComment(this); //User랑 맞춰봐야함
    }

    public void confirmParent(Comment parent) {
        this.parent = parent;
        parent.addChild(this);
    }

    public void addChild(Comment child) {
        children.add(child);
    }

    //== 삭제 ==//
    public void remove() {
        this.isRemoved = true;
    }
    @Builder
    public Comment(Long id, String content, User writer, Board board, LookBook lookBook, Comment parent, boolean isRemoved, int cDepth, String isParent) {
        this.id=id;
        this.content = content;
        this.writer = writer;
        this.board = board;
        this.lookBook = lookBook;
        this.parent = parent;
        this.isRemoved = isRemoved;
        this.cDepth = cDepth;
        this.isParent = isParent;

    }
    public Comment(String content, User writer, Board board, String isParent) {
        this.content = content;
        this.writer = writer;
        this.board = board;
        this.isParent = isParent;

    }

    //== 비즈니스 로직 ==//
    public List<Comment> findRemovableList() {

        List<Comment> result = new ArrayList<>();

        Optional.ofNullable(this.parent).ifPresentOrElse(

                parentComment ->{//대댓글인 경우 (부모가 존재하는 경우)
                    if( parentComment.isRemoved()&& parentComment.isAllChildRemoved()){
                        result.addAll(parentComment.getChildren());
                        result.add(parentComment);
                    }
                },

                () -> {//댓글인 경우
                    if (isAllChildRemoved()) {
                        result.add(this);
                        result.addAll(this.getChildren());
                    }
                }
        );

        return result;
    }

    //모든 자식 댓글이 삭제되었는지 판단
    private boolean isAllChildRemoved() {
        return getChildren().stream()//https://kim-jong-hyun.tistory.com/110 킹종현님 사랑합니다.
                .map(Comment::isRemoved)//지워졌는지 여부로 바꾼다
                .filter(isRemove -> !isRemove)//지워졌으면 true, 안지워졌으면 false이다. 따라서 filter에 걸러지는 것은 false인 녀석들이고, 있다면 false를 없다면 orElse를 통해 true를 반환한다.
                .findAny()//지워지지 않은게 하나라도 있다면 false를 반환
                .orElse(true);//모두 지워졌다면 true를 반환

    }

    public void update(boolean isRemoved) {
        this.isRemoved = isRemoved;
    }

//    public Comment toEntity(Board board, User writer) {
//        return Comment.of(
//                board,
//                writer,
//                content
//        );
//    }
//
//    public record CommentDTO(Long id, String content, User writer, Board board, LookBook lookBook, Comment parent, boolean isRemoved, int cDepth, String isParent ) {
//
//        public static CommentInfoDTO of(String content, UserInfoDTO writerDto, Long boardId, boolean isRemoved, List<ReCommentInfoDTO> reCommentListDTOList, String isParent) {
//            return new CommentInfoDTO(null, content, writerDto, boardId, isRemoved, reCommentListDTOList, null, null, isParent);
//        }
//
//        public static CommentInfoDTO of(Long id, String content, UserInfoDTO writerDto, Long boardId, boolean isRemoved, List<ReCommentInfoDTO> reCommentListDTOList, LocalDateTime createdDate, LocalDateTime lastModifiedDate, List<Comment> comments, String isParent) {
//            return new CommentInfoDTO(id, content, writerDto, boardId, isRemoved, reCommentListDTOList, createdDate, lastModifiedDate, comments, isParent);
//        }
//
//
//        @Builder
//        public static CommentDTO from(Comment entity) {
//            return CommentDTO.builder()
//                    .id(entity.getId())
//                    .articleId(entity.getArticle().getId())
//                    .userAccountDto(UserAccount.UserAccountDto.from(entity.getUserAccount()))
//                    .content(entity.getContent())
//                    .createdAt(entity.getCreatedAt())
//                    .createdBy(entity.getCreatedBy())
//                    .modifiedAt(entity.getModifiedAt())
//                    .modifiedBy(entity.getModifiedBy())
//                    .deleted(entity.getDeleted())
//                    .children(entity.getChildren() != null ? entity.getChildren().stream().map(ArticleCommentDto::from).collect(Collectors.toSet()) : new HashSet<>())
//                    .isParent(entity.getParent() == null ? "Y" : "N")
//                    .build();
//
//        }


}


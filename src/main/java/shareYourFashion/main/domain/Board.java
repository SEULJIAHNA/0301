package shareYourFashion.main.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board extends BaseTimeEntity {

    @Column(name = "board_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // @GeneratedValue : numbering 전략 -> 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
    private Long id;

    @Lob // 대용량 데이터
    private String content; // 섬머노트 라이브러리 <html>태그가 섞여서 디자인

    @Column(columnDefinition = "integer default 0" , nullable = false)  // 조회수
    private int view; // 조회수

    @Column(nullable = false , length = 100)
    private String title; // title

//    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @OneToMany(mappedBy = "board" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    // hashTag n : 1 board
    @OneToMany(mappedBy = "board"  , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<HashTag> hashTags = new ArrayList<>();

    // like n : 1 board
    @OneToMany(mappedBy = "board" ,cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    // thumbnail (board 조회 할 경우에만 필요 하므로 주인은 board)
    @OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "thumbnail_id")
    private Thumbnail thumbnail;

    // == ENTity constructor == //

    //==게시글 수정 메소드==//
    public void update( String title, String content) {
        this.title = title;
        this.content = content;
    }

    //==조회수 업데이트 메소드==//
    public void updateView(int view){
        this.view = view;
    }

    //== 연관관계 메소드 ==//
    public void setUser(User user){
        this.author = user;
        user.getBoards().add(this);
    }
    public void addComment(Comment comment){
        //comment의 Post 설정은 comment에서 함
        comments.add(comment);
    }



    //작성일, 수정일 추가?

}

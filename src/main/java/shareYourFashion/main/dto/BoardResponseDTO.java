package shareYourFashion.main.dto;

import lombok.Data;
import shareYourFashion.main.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BoardResponseDTO {

    private Long id;
    private String content;
    private int view;
    private String title;
    private User author;
    private List<Comment> comments = new ArrayList<>();
    private List<HashTag> hashTags = new ArrayList<>();
    private List<Like> likes = new ArrayList<>();
    private Thumbnail thumbnail;
    private LocalDateTime createdDate;
    private LocalDateTime LastModifiedDate;


    public BoardResponseDTO(Board entity) {
        this.id = entity.getId();
        this.content = entity.getContent();
        this.view = entity.getView();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.comments = entity.getComments();
        this.hashTags = entity.getHashTags();
        this.likes = entity.getLikes();
        this.thumbnail = entity.getThumbnail();
        this.createdDate = entity.getCreatedDate();
        this.LastModifiedDate = entity.getLastModifiedDate();
    }
}

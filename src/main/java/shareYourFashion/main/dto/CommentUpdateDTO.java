package shareYourFashion.main.dto;

import shareYourFashion.main.domain.Comment;

import java.util.Optional;

public class CommentUpdateDTO {

    private final Optional<String> content;


    public CommentUpdateDTO(Optional<String> content) {
        this.content = content;
    }

    public Optional<String> getContent() {
        return content;
    }

    public Comment toEntity() {
        return Comment.builder().content(String.valueOf(content)).build();
    }

}
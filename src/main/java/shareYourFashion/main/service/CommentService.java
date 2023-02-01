package shareYourFashion.main.service;


import shareYourFashion.main.exception.comment.CommentException;
import shareYourFashion.main.dto.CommentSaveDTO;
import shareYourFashion.main.dto.CommentUpdateDTO;

public interface CommentService {

    void save(Long boardId , CommentSaveDTO commentSaveDto);
    void saveReComment(Long boardId, Long parentId ,CommentSaveDTO commentSaveDto);


    void remove(Long id) throws CommentException;
}

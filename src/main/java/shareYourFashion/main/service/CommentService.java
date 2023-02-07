package shareYourFashion.main.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shareYourFashion.main.domain.Comment;
import shareYourFashion.main.dto.CommentInfoDTO;
import shareYourFashion.main.dto.CommentRemovedDTO;
import shareYourFashion.main.exception.comment.CommentException;
import shareYourFashion.main.dto.CommentSaveDTO;

public interface CommentService {



    void save(Long boardId , CommentSaveDTO commentSaveDto);
    void saveReComment(Long boardId, Long parentId , CommentSaveDTO commentSaveDTO);


//    void remove(Long id) throws CommentException;

    void remove(Long id, CommentRemovedDTO commentRemovedDTO) throws CommentException;
    void update(Long id, CommentRemovedDTO commentRemovedDTO) throws CommentException;

    public Page<Comment> findByBoardId(Long boardId, Pageable Pageable);

}

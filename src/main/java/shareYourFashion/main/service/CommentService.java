package shareYourFashion.main.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import shareYourFashion.main.domain.Board;
import shareYourFashion.main.domain.Comment;
import shareYourFashion.main.dto.BoardResponseDTO;
import shareYourFashion.main.dto.CommentRemovedDTO;
import shareYourFashion.main.exception.comment.CommentException;
import shareYourFashion.main.dto.CommentSaveDTO;
import shareYourFashion.main.dto.CommentUpdateDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface CommentService {



    void save(Long boardId , CommentSaveDTO commentSaveDto);
    void saveReComment(Long boardId, Long parentId ,CommentSaveDTO commentSaveDto);


//    void remove(Long id) throws CommentException;

    void remove(Long id, CommentRemovedDTO commentRemovedDTO) throws CommentException;
    void update(Long id, CommentRemovedDTO commentRemovedDTO) throws CommentException;

    public Page<Comment> findByBoardId(Long boardId, Pageable Pageable);

}

package shareYourFashion.main.service;


import org.springframework.transaction.annotation.Transactional;
import shareYourFashion.main.dto.BoardResponseDTO;
import shareYourFashion.main.exception.comment.CommentException;
import shareYourFashion.main.dto.CommentSaveDTO;
import shareYourFashion.main.dto.CommentUpdateDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface CommentService {



    void save(Long boardId , CommentSaveDTO commentSaveDto);
    void saveReComment(Long boardId, Long parentId ,CommentSaveDTO commentSaveDto);


    void remove(Long id) throws CommentException;


}

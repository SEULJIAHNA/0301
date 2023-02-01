package shareYourFashion.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shareYourFashion.main.config.auth.SecurityUtil;
import shareYourFashion.main.exception.comment.*;
import shareYourFashion.main.domain.Comment;
import shareYourFashion.main.dto.CommentSaveDTO;
import shareYourFashion.main.repository.BoardRepository;
import shareYourFashion.main.repository.CommentRepository;
import shareYourFashion.main.repository.UserRepository;


import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Override
    public void save(Long boardId, CommentSaveDTO commentSaveDTO) {
        Comment comment = commentSaveDTO.toEntity();

        comment.confirmWriter(userRepository.findByNickname(SecurityUtil.getLoginNickname()).orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_USER)));

        comment.confirmBoard(boardRepository.findById(boardId).orElseThrow(() -> new BoardException(BoardExceptionType.BOARD_NOT_POUND)));


        commentRepository.save(comment);

    }

    @Override
    public void saveReComment(Long boardId, Long parentId, CommentSaveDTO commentSaveDto) {
        Comment comment = commentSaveDto.toEntity();

        comment.confirmWriter(userRepository.findByNickname(SecurityUtil.getLoginNickname()).orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_USER)));

        comment.confirmBoard(boardRepository.findById(boardId).orElseThrow(() -> new BoardException(BoardExceptionType.BOARD_NOT_POUND)));

        comment.confirmParent(commentRepository.findById(parentId).orElseThrow(() -> new CommentException(CommentExceptionType.NOT_POUND_COMMENT)));

        commentRepository.save(comment);

    }




    @Override
    public void remove(Long id) throws CommentException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentException(CommentExceptionType.NOT_POUND_COMMENT));

        if(!comment.getWriter().getNickname().equals(SecurityUtil.getLoginNickname())){
            throw new CommentException(CommentExceptionType.NOT_AUTHORITY_DELETE_COMMENT);
        }

        comment.remove();
        List<Comment> removableCommentList = comment.findRemovableList();
        commentRepository.deleteAll(removableCommentList);
    }
}

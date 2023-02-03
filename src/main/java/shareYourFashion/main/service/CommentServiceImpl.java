package shareYourFashion.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shareYourFashion.main.config.auth.SecurityUtil;
import shareYourFashion.main.domain.Board;
import shareYourFashion.main.dto.BoardResponseDTO;
import shareYourFashion.main.dto.CommentInfoDTO;
import shareYourFashion.main.exception.comment.*;
import shareYourFashion.main.domain.Comment;
import shareYourFashion.main.dto.CommentSaveDTO;
import shareYourFashion.main.repository.BoardRepository;
import shareYourFashion.main.repository.CommentRepository;
import shareYourFashion.main.repository.UserRepository;


import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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


//    @Transactional(readOnly = true)
//    public HashMap< String, Object > findAll(Integer page, Integer size) {
//
//        HashMap < String, Object > resultMap = new HashMap < String, Object > ();
//
//        Page<Comment> list = commentRepository.findAll(PageRequest.of(page, size));
//
//        resultMap.put("list", list.stream().map(CommentInfoDTO::new).collect(Collectors.toList()));
//        resultMap.put("paging", list.getPageable());
//        resultMap.put("totalCnt", list.getTotalElements());
//        resultMap.put("totalPage", list.getTotalPages());
//
//        return resultMap;
//    }

}

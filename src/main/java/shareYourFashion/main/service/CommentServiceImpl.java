package shareYourFashion.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shareYourFashion.main.config.auth.SecurityUtil;
import shareYourFashion.main.domain.User;
import shareYourFashion.main.dto.*;
import shareYourFashion.main.exception.comment.*;
import shareYourFashion.main.domain.Comment;
import shareYourFashion.main.repository.BoardRepository;
import shareYourFashion.main.repository.CommentRepository;
import shareYourFashion.main.repository.UserRepository;


import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Override
    public void save(Long boardId, CommentSaveDTO commentSaveDTO) {
        try {
            Comment comment = commentSaveDTO.toEntity();
    //        comment.confirmWriter(userRepository.findByNickname(SecurityUtil.getLoginNickname()).orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_USER)));
            comment.confirmBoard(boardRepository.findById(boardId).orElseThrow(() -> new BoardException(BoardExceptionType.BOARD_NOT_POUND)));
//            comment.confirmParent(comment);
            // 추후 로그인 적용시 삭제 필수!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            Long aLong = 1L;
            Optional<User> user = userRepository.findById(aLong);
            comment.confirmWriter(user.get());
            comment.setIsParent("Y");
            commentRepository.save(comment);
        } catch (EntityNotFoundException e) {
            System.out.println("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void saveReComment(Long boardId, Long parentId, CommentSaveDTO commentSaveDto ) {
        try {


            Comment parent = commentRepository.getReferenceById(parentId);
            commentSaveDto.setParent(parent);
            Comment comment = commentSaveDto.toEntity();

            System.out.println("comment = " + comment.toString());
//로그인시 주석풀기

            Long aLong = 1L;
            Optional<User> user = userRepository.findById(aLong);
            comment.confirmWriter(user.get());
//            comment.confirmWriter(userRepository.findByNickname(SecurityUtil.getLoginNickname()).orElseThrow(() -> new UserException(UserExceptionType.NOT_FOUND_USER)));

            comment.confirmBoard(boardRepository.findById(boardId).orElseThrow(() -> new BoardException(BoardExceptionType.BOARD_NOT_POUND)));

            comment.confirmParent(commentRepository.findById(parentId).orElseThrow(() -> new CommentException(CommentExceptionType.NOT_POUND_COMMENT)));
            comment.confirmParent(parent);
            comment.setIsParent("N");
            comment.setParent(parent);

            List<Comment> set = parent.getChildren();
            set.add(comment);
            parent.setChildren(set);

            commentRepository.save(comment);
            System.out.println("comment = " + comment);

        } catch (EntityNotFoundException e) {
            System.out.println("댓글 저장 실패. 댓글 작성에 필요한 정보를 찾을 수 없습니다 - {}" + e.getMessage());
            e.printStackTrace();
        }
    }




    @Override
    @Transactional
    public void remove(Long id, CommentRemovedDTO commentRemovedDTO) throws CommentException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentException(CommentExceptionType.NOT_POUND_COMMENT));

        //로그인시 주석풀기
       /* if(!comment.getWriter().getNickname().equals(SecurityUtil.getLoginNickname())){
            throw new CommentException(CommentExceptionType.NOT_AUTHORITY_DELETE_COMMENT);
        }*/ 
        comment.update(commentRemovedDTO.isRemoved());
        //List<Comment> removableCommentList = comment.findRemovableList(); //대댓글 리스트 불러오기
        //commentRepository.deleteAll(removableCommentList); //불러온 대댓글 전체리스트 삭제
    }

    @Transactional
    public void update(Long id, CommentRemovedDTO commentRemovedDTO) throws CommentException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentException(CommentExceptionType.NOT_POUND_COMMENT));

        //로그인시 주석풀기
       /* if(!comment.getWriter().getNickname().equals(SecurityUtil.getLoginNickname())){
            throw new CommentException(CommentExceptionType.NOT_AUTHORITY_DELETE_COMMENT);
        }*/
        comment.update(commentRemovedDTO.isRemoved());
        //List<Comment> removableCommentList = comment.findRemovableList(); //대댓글 리스트 불러오기
        //commentRepository.deleteAll(removableCommentList); //불러온 대댓글 전체리스트 삭제
    }

    public Page<Comment> findByBoardId(Long boardId, Pageable Pageable){
        return commentRepository.findByBoardId(boardId, Pageable);
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

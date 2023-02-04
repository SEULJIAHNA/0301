package shareYourFashion.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shareYourFashion.main.domain.Board;
import shareYourFashion.main.dto.BoardRequestDTO;
import shareYourFashion.main.dto.BoardResponseDTO;
import shareYourFashion.main.dto.BoardSaveRequestDTO;
import shareYourFashion.main.dto.BoardUpdateDTO;
import shareYourFashion.main.exception.comment.BaseException;
import shareYourFashion.main.exception.comment.BoardException;
import shareYourFashion.main.repository.BoardRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final static String VIEWCOOKIENAME = "alreadyViewCookie";

    @Transactional
    public Long save(BoardSaveRequestDTO boardSaveDTO) {
        return boardRepository.save(boardSaveDTO.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, BoardUpdateDTO updateDTO){
        Board board = boardRepository.findById(id).orElseThrow(()-> new IllegalStateException("게시글이 없습니다. id="+id));
        board.update(updateDTO.getTitle(), updateDTO.getContent());
        return  id;
    }

    @Transactional(readOnly = true)
    public HashMap< String, Object > findAll(Integer page, Integer size) {

        HashMap < String, Object > resultMap = new HashMap < String, Object > ();

        Page<Board> list = boardRepository.findAll(PageRequest.of(page, size));

        resultMap.put("list", list.stream().map(BoardResponseDTO::new).collect(Collectors.toList()));
        resultMap.put("paging", list.getPageable());
        resultMap.put("totalCnt", list.getTotalElements());
        resultMap.put("totalPage", list.getTotalPages());

        return resultMap;
    }



    @Transactional(readOnly = true)
    public List<BoardResponseDTO> findAll() {
        return boardRepository.findAll().stream()
                .map(BoardResponseDTO::new).collect(Collectors.toList());
    }

    public BoardResponseDTO findById(Long id){
        return new BoardResponseDTO(boardRepository.findById(id).get());


    }

    @Transactional
    public Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable Pageable) {
        return boardRepository.findByTitleContainingOrContentContaining(title, content, Pageable);
    }


    public BoardResponseDTO getBoardResponseDTO(Long id) {


        /**
         * Post + MEMBER 조회 -> 쿼리 1번 발생
         *
         * 댓글&대댓글 리스트 조회 -> 쿼리 1번 발생(POST ID로 찾는 것이므로, IN쿼리가 아닌 일반 where문 발생)
         * (댓글과 대댓글 모두 Comment 클래스이므로, JPA는 구분할 방법이 없어서, 당연히 CommentList에 모두 나오는것이 맞다,
         * 가지고 온 것을 가지고 우리가 구분지어주어야 한다.)
         *
         * 댓글 작성자 정보 조회 -> 배치사이즈를 이용했기때문에 쿼리 1번 발생
         *
         *
         */
        return new BoardResponseDTO(boardRepository.findWithAuthorById(id)
                .orElseThrow(() -> new BoardException()));

    }

    public void deleteById(Long id) {
        boardRepository.deleteById(id);
    } //한 게시물만 삭제

    public void deleteAll(Long[] deleteId) {
        boardRepository.deleteBoard(deleteId);
    } //다수의 게시물 삭제

    @Transactional
    public int updateView(Long id, HttpServletRequest request, HttpServletResponse response){

        Cookie[] cookies = request.getCookies();
        boolean checkCookie = false;
        int result = 0;
        if(cookies != null){
            for(Cookie cookie : cookies)
            {
                if(cookie.getName().equals((VIEWCOOKIENAME+id))) checkCookie = true;
            }
            if(!checkCookie){
                Cookie newCookie = createCookieForForNotOverlap(id);
                response.addCookie(newCookie);
                result = boardRepository.updateView(id);
            }
        }else{
            Cookie newCookie = createCookieForForNotOverlap(id);
            response.addCookie(newCookie);
            result = boardRepository.updateView(id);
        }
        return result;

    }
    //조회수 중복 방지
    private Cookie createCookieForForNotOverlap(Long id){
        Cookie cookie = new Cookie(VIEWCOOKIENAME+id, String.valueOf(id));
        cookie.setComment("조회수 중복 방지 체크 쿠키");
        cookie.setMaxAge(60 * 60 * 24);//하루의 기한
        cookie.setHttpOnly(true);//서버에서만 조작
        return cookie;
    }


//    @Transactional
//    private int likeView



}

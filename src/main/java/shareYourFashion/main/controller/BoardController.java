package shareYourFashion.main.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import shareYourFashion.main.domain.Board;
import shareYourFashion.main.domain.Comment;
import shareYourFashion.main.dto.BoardRequestDTO;
import shareYourFashion.main.dto.BoardResponseDTO;
import shareYourFashion.main.dto.CommentInfoDTO;
import shareYourFashion.main.dto.ReCommentInfoDTO;
import shareYourFashion.main.repository.BoardRepository;
import shareYourFashion.main.repository.CommentRepository;
import shareYourFashion.main.service.BoardService;
import shareYourFashion.main.service.CommentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.logging.Logger;
@Controller
@RequiredArgsConstructor

public class BoardController {


    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final CommentRepository commentRepository;
    private final CommentService commentService;


    private static Logger logger = Logger.getLogger(BoardController.class.getName());

    private final String BOARD_MAIN_URL = "/boards";
    private final String BOARD_MAIN_HTML = "pc/board/mainBoardPage";
    private final String BOARD_FORM_URL = "/boards/form";
    private final String BOARD_FORM_HTML = "pc/board/boardPageForm";
    private final String BOARD_CONTENT_VIEW_PAGE_URL = "/boards/view";
    private final String BOARD_CONTENT_VIEW_PAGE_HTML = "pc/board/boardViewPage";
    private final String BOARD_UPDATE_PAGE_URL = "/boards/updateForm";
    private final String BOARD_DELETE_PAGE_URL = "/boards/deletePage";
    private final String BOARD_DELETE_ALL_URL = "/boards/deleteAll";
    /* return main board pages */
    @GetMapping(value = BOARD_MAIN_URL) //"/boards";
    public String board(Model m, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                              @RequestParam(required = false, defaultValue = "") String searchText) {
        Page<Board> boards = boardService.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);


        m.addAttribute("startPage", startPage);
        m.addAttribute("endPage", endPage);
        m.addAttribute("boards", boards);

        return BOARD_MAIN_HTML;
    }

    @GetMapping(value = BOARD_FORM_URL)  // "/boards/form"; 로그인 작성 폼
    public String boardForm(Model m, @RequestParam(required = false) Long id) {
        if (id == null) {
            m.addAttribute("board", new Board());
        } else {
            Board board = boardRepository.findById(id).orElse(null);
            m.addAttribute("board", board);
        }

        return BOARD_FORM_HTML;
    }

    @PostMapping(value = BOARD_FORM_URL) //"/boards/form"; 로그인 작성 폼
    public String pastForm(@Valid Board board, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return BOARD_FORM_HTML;
        }
        boardRepository.save(board);
        return BOARD_CONTENT_VIEW_PAGE_URL;//바로 위 if 모델앤뷰와 걸리지 않는지 체크해야함
    }

    @GetMapping(value = BOARD_CONTENT_VIEW_PAGE_URL) //"/boards/view"; 게시글 상세조회
    public String getBoardViewPage(Model m, BoardRequestDTO boardRequestDTO, HttpServletRequest request, HttpServletResponse response,
                                    @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) throws Exception{
        try {
            if(boardRequestDTO.getId() != null){

                boardService.updateView(boardRequestDTO.getId(), request, response);
                BoardResponseDTO board = boardService.getBoardResponseDTO(boardRequestDTO.getId());
//                List<CommentInfoDTO> comments = board.getCommentInfoDtoList();
                m.addAttribute("board", board);
                //m.addAttribute("comments", comments);

                //CommentInfoDTO
                Page<Comment> commentList = commentService.findByBoardId(boardRequestDTO.getId(), pageable);
//                List<CommentInfoDTO> reCommentList =

                /* 생성자에 하는법찾아서 넣어야함*/
                int cnt = commentList.toList().size();
                for(int i=0 ;i<cnt;i++){
                    if(commentList.toList().get(i).isRemoved()){
                        commentList.toList().get(i).setContent("삭제된 댓글입니다");
                    }
                }

                /* 생성자에 하는법찾아서 넣어야함*/

                int startPage = Math.max(1, commentList.getPageable().getPageNumber() - 4);
                int endPage = Math.min(commentList.getTotalPages(), commentList.getPageable().getPageNumber() + 4);
                m.addAttribute("startPage", startPage);
                m.addAttribute("endPage", endPage);
                m.addAttribute("comments", commentList);

//                if (commentList != null && !commentList.isEmpty()){
//                    m.addAttribute("comments",commentList);
//                }


            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return BOARD_CONTENT_VIEW_PAGE_HTML;
    }


}



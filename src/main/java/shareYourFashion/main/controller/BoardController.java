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
import shareYourFashion.main.repository.BoardRepository;
import shareYourFashion.main.repository.CommentRepository;
import shareYourFashion.main.service.BoardService;

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
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("/pc/board/mainBoardPage.html");

        return BOARD_MAIN_HTML;
    }

    @GetMapping(value = BOARD_FORM_URL)  // "/boards/form";
    public String boardForm(Model m, @RequestParam(required = false) Long id) {
        if (id == null) {
            m.addAttribute("board", new Board());
        } else {
            Board board = boardRepository.findById(id).orElse(null);
            m.addAttribute("board", board);
        }

        return BOARD_FORM_HTML;
    }

    @PostMapping(value = BOARD_FORM_URL) //게시판 글 작성 폼
    public String pastForm(@Valid Board board, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return BOARD_FORM_HTML;
        }
        boardRepository.save(board);
        return BOARD_CONTENT_VIEW_PAGE_URL;//바로 위 if 모델앤뷰와 걸리지 않는지 체크해야함
    }

    @GetMapping(value = BOARD_CONTENT_VIEW_PAGE_URL) //게시글 상세조회 "/boards/view";
    public String getBoardViewPage(Model m, BoardRequestDTO boardRequestDTO, HttpServletRequest request, HttpServletResponse response,
                                    @PageableDefault(page = 0, size = 10, direction = Sort.Direction.DESC) Pageable pageable) throws Exception{
        try {
            if(boardRequestDTO.getId() != null){

                boardService.updateView(boardRequestDTO.getId(), request, response);
                //BoardResponseDTO board = boardService.findById(boardRequestDTO.getId());
                BoardResponseDTO board = boardService.getBoardResponseDTO(boardRequestDTO.getId());
                List<CommentInfoDTO> comments = board.getCommentInfoDtoList();
                m.addAttribute("board", board);
                m.addAttribute("comments", comments);

                Page<Comment> commentList = commentRepository.findAll(pageable);
                int startPage = Math.max(1, commentList.getPageable().getPageNumber() - 4);
                int endPage = Math.min(commentList.getTotalPages(), commentList.getPageable().getPageNumber() + 4);
//                if (commentList != null && !commentList.isEmpty()){
//                    m.addAttribute("comment",commentList);
//                }
//
//                m.addAttribute("board",boardService.(id));//0203 아까 서비스때문에 중단
                m.addAttribute("startPage", startPage);
                m.addAttribute("endPage", endPage);
//                m.addAttribute("commentList", commentList);

            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return BOARD_CONTENT_VIEW_PAGE_HTML;
    }

//    @GetMapping(value = BOARD_MAIN_URL)//조회수
//    public String updateView(@PathVariable("id") Long id, Model m){
//        Board board = boardRepository.findById(id).get();
//        int view = board.getView() + 1;
//
//        BoardRequestDTO boardRequestDTO = BoardRequestDTO.builder()
//                .view(view)
//                .build();
//
//        boardService.updateView(board.getId(), boardRequestDTO);
//
//        m.addAttribute(board);
//        return BOARD_CONTENT_VIEW_PAGE_HTML;
//    }


    @PostMapping(value = BOARD_DELETE_PAGE_URL)
    public String boardDeletePage(Model m, @RequestParam() Long id) throws Exception{
        try {
            boardService.deleteById(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return BOARD_MAIN_HTML;
    }
    @PostMapping(value = BOARD_DELETE_ALL_URL)
    public String boardDeleteALL(Model m, @RequestParam() Long[] deleteId) throws Exception{
        try {
            boardService.deleteAll(deleteId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return BOARD_MAIN_HTML;
    }


}



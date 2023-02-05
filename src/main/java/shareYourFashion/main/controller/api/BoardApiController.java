package shareYourFashion.main.controller.api;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.util.StringUtils;
import shareYourFashion.main.domain.Board;
import shareYourFashion.main.dto.BoardSaveRequestDTO;
import shareYourFashion.main.dto.BoardUpdateDTO;
import shareYourFashion.main.repository.BoardRepository;
import shareYourFashion.main.service.BoardService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor

public class BoardApiController {

    private final BoardRepository boardRepository;
    private final BoardService boardService;


    @GetMapping("/api/board")
    List<Board> all(@RequestParam(required = false, defaultValue = "") String title,
                    @RequestParam(required = false, defaultValue = "") String content) {
        if (StringUtils.isEmpty(title) && StringUtils.isEmpty(content)) {
            return boardRepository.findAll();
        } else {
            return boardRepository.findByTitleOrContent(title, content);
        }

    }

    @PostMapping("/api/board")//글저장
    ResponseEntity<?>  newBoardSave(@RequestBody BoardSaveRequestDTO newBoardSaveRequestDTO, Errors errors , Model model , UriComponentsBuilder b) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        UriComponents uriComponents;
        try {
            Long id = boardService.save(newBoardSaveRequestDTO);
            /*저장 성공 후 boards/view 페이지로 리다이렉트*/
            uriComponents = b.fromUriString("/boards/view?id="+ id).build();
            headers.setLocation(uriComponents.toUri());
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<Void>(headers , HttpStatus.OK);

    }

    @GetMapping("/api/board/{id}")//글 하나만 보기
    Board one(@PathVariable Long id){
        return boardRepository.findById(id).orElse(null);
    }

    @PostMapping("/api/board/update")//게시글 수정
    ResponseEntity<?>  update2(@RequestBody BoardUpdateDTO updateDTO, Errors errors , Model model , UriComponentsBuilder b) throws Exception{

        HttpHeaders headers = new HttpHeaders();
        UriComponents uriComponents;
        try {
            boardService.update(updateDTO.getId(), updateDTO);
            /*저장 성공 후 boards/view 페이지로 리다이렉트*/
            uriComponents = b.fromUriString("/boards/view?id=" + updateDTO.getId()).build();
            headers.setLocation(uriComponents.toUri());
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<Void>(headers , HttpStatus.OK);

    }


    @ResponseBody
    @PostMapping("/api/board/delete/{id}")//글 삭제
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        System.out.println("id = " + id);
        HttpHeaders headers = new HttpHeaders();

        try {
            boardService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("e = " + e);
            return new ResponseEntity<>(headers , HttpStatus.BAD_REQUEST);
        }

        // 성공적으로 게시글 삭제 시 게시글 목록 페이지로 이동
        headers.setLocation(URI.create("/boards"));
        System.out.println("headers.getLocation() = " + headers.getLocation());
        return new ResponseEntity<>(headers , HttpStatus.MOVED_PERMANENTLY);
    }

//    @PostMapping("/api/board/delete/{id}") //remove로 구현
//    public void delete(@PathVariable("id") Long id) {
//
//
//        boardService.remove(id);
//    }
    
    
    
    
    
    
//    @ResponseBody
//    @PostMapping("/api/board/delete/{id}")//글 삭제
//    public ResponseEntity<?> deleteById(@PathVariable Long id, Errors errors , Model model , UriComponentsBuilder b) throws Exception {
//        HttpHeaders headers = new HttpHeaders();
//        UriComponents uriComponents;
//        try {
//            boardService.deleteById(id);
//            /*저장 성공 후 boards 페이지로 리다이렉트*/
//            uriComponents = b.fromUriString("/boards").build();
//            headers.setLocation(uriComponents.toUri());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(headers , HttpStatus.BAD_REQUEST);
//        }
//        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
//    }



    }



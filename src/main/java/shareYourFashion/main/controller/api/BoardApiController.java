package shareYourFashion.main.controller.api;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.util.StringUtils;
import shareYourFashion.main.constant.ImageType;
import shareYourFashion.main.domain.*;
import shareYourFashion.main.domain.valueTypeClass.Image;
import shareYourFashion.main.dto.BoardRemovedDTO;
import shareYourFashion.main.dto.BoardSaveRequestDTO;
import shareYourFashion.main.dto.BoardUpdateDTO;
import shareYourFashion.main.dto.CommentRemovedDTO;
import shareYourFashion.main.exception.DoNotFoundImageObjectException;
import shareYourFashion.main.repository.BoardRepository;
//import shareYourFashion.main.service.BoardImageUtils;
import shareYourFashion.main.service.BoardService;
import shareYourFashion.main.service.FileService;
import shareYourFashion.main.service.FileUtils;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor

public class BoardApiController {

    private final BoardRepository boardRepository;
    private final BoardService boardService;


//    private final BoardImageUtils boardImageUtils;

    private final FileService fileService;


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
    Board one (@PathVariable Long id){
        return boardRepository.findById(id).orElse(null);
    }

    @PostMapping("/api/board/update")//게시글 수정
    ResponseEntity<?> update2 (@RequestBody BoardUpdateDTO updateDTO, Errors errors, Model
    model, UriComponentsBuilder b) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        UriComponents uriComponents;
        try {
            boardService.update(updateDTO.getId(), updateDTO);
            /*저장 성공 후 boards/view 페이지로 리다이렉트*/
            uriComponents = b.fromUriString("/boards/view?id=" + updateDTO.getId()).build();
            headers.setLocation(uriComponents.toUri());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Void>(headers, HttpStatus.OK);

    }



    @ResponseBody
    @PostMapping("/api/board/delete/{id}")//글 삭제
    public ResponseEntity<?> deleteById(@PathVariable Long id,
                                        Model model, UriComponentsBuilder b)
                                       {
        System.out.println("id = " + id);
        HttpHeaders headers = new HttpHeaders();

        try {
            BoardRemovedDTO boardRemovedDTO= new BoardRemovedDTO(id,true);
            boardService.update(id, boardRemovedDTO);
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



    }



package shareYourFashion.main.controller.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import shareYourFashion.main.dto.CommentRemovedDTO;
import shareYourFashion.main.dto.CommentSaveDTO;
import shareYourFashion.main.dto.UserInfoDTO;
import shareYourFashion.main.service.CommentService;
import shareYourFashion.main.utills.ControllerUtill;

import javax.persistence.EntityNotFoundException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;



    @PostMapping("/api/comments/{boardId}")//글저장
    ResponseEntity<?>  commentSave(@PathVariable("boardId") Long boardId,
                                   @RequestBody CommentSaveDTO commentSaveDTO, Errors errors , Model model , UriComponentsBuilder b) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        UriComponents uriComponents;
        try {
            commentService.save(boardId, commentSaveDTO);
            /*저장 성공 후 boards 페이지로 리다이렉트*/
            uriComponents = b.fromUriString("/boards/view?id="+ boardId).build();
            headers.setLocation(uriComponents.toUri());

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<Void>(headers , HttpStatus.OK);

    }


    @PostMapping("/api/comments/{boardId}/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> reCommentSave(@PathVariable("boardId") Long boardId,
                              @PathVariable("commentId") Long commentId,
                              CommentSaveDTO commentSaveDTO,
//                               @AuthenticationPrincipal UserInfoDTO.BoardPrincipal principal,
                              BindingResult bindingResult,
                               Errors errors , Model model , UriComponentsBuilder b) throws Exception{
        HttpHeaders headers = new HttpHeaders();
        UriComponents uriComponents;
    try{

            if (bindingResult.hasErrors()) {
                return new ResponseEntity<>(ControllerUtill.getErrors(bindingResult), HttpStatus.BAD_REQUEST);
            }
//            if (principal == null) {
//                return new ResponseEntity<>(ErrorMessages.ACCESS_TOKEN_NOT_FOUND, HttpStatus.BAD_REQUEST);
//            } //나중에 로그인 구현되면 주석풀기
            commentService.saveReComment(boardId, commentId, commentSaveDTO);
            uriComponents = b.fromUriString("/boards/view?id="+ boardId).build();
            headers.setLocation(uriComponents.toUri());
            return new ResponseEntity<>("등록되었습니다.", HttpStatus.OK);
        } catch (EntityNotFoundException e){
            return new ResponseEntity<Void>(headers , HttpStatus.OK);
        }
    }

    @PostMapping("/api/commentDelete/{commentId}")
    public ResponseEntity<?> delete(@PathVariable("commentId") Long commentId , Model model, UriComponentsBuilder b) throws Exception {
//, @RequestBody CommentRemovedDTO commentRemovedDTO
        HttpHeaders headers = new HttpHeaders();
        UriComponents uriComponents;
        try {
            CommentRemovedDTO commentRemovedDTO = new CommentRemovedDTO(commentId,true);
            commentService.update(commentId,commentRemovedDTO);
            /*저장 성공 후 boards 페이지로 리다이렉트*/
            // uriComponents = b.fromUriString("/boards/view?id="+ boardId).build();
            //headers.setLocation(uriComponents.toUri());
            headers.setLocation(URI.create("/"));
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<Void>(headers , HttpStatus.OK);


    }


}


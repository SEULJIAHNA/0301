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
import shareYourFashion.main.dto.BoardSaveRequestDTO;
import shareYourFashion.main.dto.CommentSaveDTO;
import shareYourFashion.main.service.CommentService;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;


//    @PostMapping("/api/comments/{boardId}")
//    @ResponseStatus(HttpStatus.CREATED)
//    public void Save(@PathVariable("boardId")Long boardId, CommentSaveDTO commentSaveDTO){
//        commentService.save(boardId, commentSaveDTO);
//    }



    @PostMapping("/api/comments")//글저장
    ResponseEntity<?>  commentSave(@PathVariable("boardId") Long boardId, @RequestBody CommentSaveDTO commentSaveDTO, Errors errors , Model model , UriComponentsBuilder b) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        UriComponents uriComponents;
        try {
            commentService.save(boardId, commentSaveDTO);
            /*저장 성공 후 boards 페이지로 리다이렉트*/
            uriComponents = b.path("/boards/view").build();
            headers.setLocation(uriComponents.toUri());
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<Void>(headers , HttpStatus.OK);

    }


    @PostMapping("/api/comments/{boardId}/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void reCommentSave(@PathVariable("boardId") Long boardId,
                              @PathVariable("commentId") Long commentId,
                              CommentSaveDTO commentSaveDTO){
        commentService.saveReComment(boardId, commentId, commentSaveDTO);

    }

    @PostMapping("/api/{commentId}")
    public void delete(@PathVariable("commentId") Long commentId) {
        commentService.remove(commentId);
    }


}

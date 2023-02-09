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
import shareYourFashion.main.dto.BoardSaveRequestDTO;
import shareYourFashion.main.dto.BoardUpdateDTO;
import shareYourFashion.main.exception.DoNotFoundImageObjectException;
import shareYourFashion.main.repository.BoardRepository;
import shareYourFashion.main.service.BoardImageUtils;
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


    private final BoardImageUtils boardImageUtils;

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


//    @PostMapping("/api/board")//글저장
//    ResponseEntity<?>  newBoardSave(@RequestBody BoardSaveRequestDTO newBoardSaveRequestDTO, Errors errors , Model model , UriComponentsBuilder b) throws Exception {
//
//        HttpHeaders headers = new HttpHeaders();
//        UriComponents uriComponents;
//        try {
//            Long id = boardService.save(newBoardSaveRequestDTO);
//            /*저장 성공 후 boards/view 페이지로 리다이렉트*/
//            uriComponents = b.fromUriString("/boards/view?id="+ id).build();
//            headers.setLocation(uriComponents.toUri());
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return new ResponseEntity<Void>(headers , HttpStatus.OK);
//
//    }
            @PostMapping("/api/board")//글저장
            ResponseEntity<?> newBoardSave (@RequestBody BoardSaveRequestDTO newBoardSaveRequestDTO,
                    @RequestParam Map < String, Object > paramMap,
                    MultipartHttpServletRequest multipartRequest,
                    String type, Errors errors, Model model, UriComponentsBuilder b) throws Exception {

                HttpHeaders headers = new HttpHeaders();
                UriComponents uriComponents;
                try {

                    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                    System.out.println("authentication = " + authentication);

                    MultipartFile file;

                    try {
                        // 전달 받은 blob image data를 multipartFile type 으로 전환
                        file = multipartRequest.getFile("blob");

                        // blob image 가 null 인 경우 예외 발생 시킨다.
                        if (file.isEmpty()) {
                            throw new DoNotFoundImageObjectException(" do not found blob image multipartFile Object (byte size : 0)");
                        }
                    } catch (Exception e) {
                        throw e;
                    }


                    String imageType = (String) paramMap.get("imageType");

                    // 외부 루트(image upload folder)에 저장된 image 내용을 담은 image 객체 생성한다.
                    BoardImage image = boardImageUtils.saveImage(file, imageType);

                    // db에 전달할 Image entity 생성(정보는 내부에 저장 , 실제 데이터는 외부 루트에 저장)
                    Optional<Object> imageEntity = Optional.empty();
                    System.out.println("imageEntity = " + imageEntity);
                    // 유저와 이미지 관계 매핑
//        PrincipalDetails userDetails = (PrincipalDetails)authentication.getPrincipal();
                    // 이미지 등록 요청한 유저 entity 찾아오기

//        User principal = userService.findByEmail(userDetails.getEmail());
//        System.out.println("principal = " + principal);

                    if (image.getImageType().equals(ImageType.USER_PROFILE_IMAGE)) {
                        Object img = imageEntity.orElse(fileService.createUserProfileEntity(image));
                        System.out.println("img = " + img);

                        // 유저와 이미지 연관관계 매핑
//            principal.userToProfileImage((UserProfileImage) img);
                    } else if (image.getImageType().equals(ImageType.USER_BACKGROUND_PROFILE_IMAGE)) {
                        Object img = imageEntity.orElse(fileService.createBackgroundProfileImageEntity(image));

                        // 유저와 이미지 연관관계 매핑
//            principal.userToBDProfileImage( (BackgroundProfileImage) img);
                    } else {
                        throw new DoNotFoundImageObjectException("image Entity is null");
                    }


                    Long id = boardService.save(newBoardSaveRequestDTO, image);
                    /*저장 성공 후 boards/view 페이지로 리다이렉트*/
                    uriComponents = b.fromUriString("/boards/view?id=" + id).build();
                    headers.setLocation(uriComponents.toUri());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new ResponseEntity<Void>(headers, HttpStatus.OK);

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



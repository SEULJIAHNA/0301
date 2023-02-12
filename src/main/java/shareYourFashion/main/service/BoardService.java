package shareYourFashion.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import shareYourFashion.main.config.auth.SecurityUtil;
import shareYourFashion.main.constant.ImageType;
import shareYourFashion.main.domain.Board;
//import shareYourFashion.main.domain.BoardImage;
import shareYourFashion.main.domain.valueTypeClass.Image;
import shareYourFashion.main.dto.BoardResponseDTO;
import shareYourFashion.main.dto.BoardSaveRequestDTO;
import shareYourFashion.main.dto.BoardUpdateDTO;
import shareYourFashion.main.exception.DoNotFoundImageObjectException;
import shareYourFashion.main.exception.comment.BoardException;
import shareYourFashion.main.exception.comment.BoardExceptionType;
import shareYourFashion.main.exception.comment.CommentException;
import shareYourFashion.main.repository.BoardRepository;
import shareYourFashion.main.repository.UserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileUtils fileUtils;
    private final FileService fileService;
    private final static String VIEWCOOKIENAME = "alreadyViewCookie";
    private final UserRepository userRepository;


    @Transactional
    public Long save(BoardSaveRequestDTO boardSaveDTO) {
        return boardRepository.save(boardSaveDTO.toEntity()).getId();
    }



//    @Transactional
//    public Long save(BoardSaveRequestDTO boardSaveDTO, @RequestParam Map<String  , Object> paramMap , MultipartHttpServletRequest multipartRequest)throws IOException, DoNotFoundImageObjectException {
//
//
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        System.out.println("authentication = " + authentication);
//
//        MultipartFile file;
//
//        try {
//            // 전달 받은 blob image data를 multipartFile type 으로 전환
//            file = multipartRequest.getFile("blob");
//
//            // blob image 가 null 인 경우 예외 발생 시킨다.
//            if(file.isEmpty()) {
//                throw new DoNotFoundImageObjectException(" do not found blob image multipartFile Object (byte size : 0)");
//            }
//        } catch(Exception e) {
//            throw e;
//        }
//
//
//        String imageType = (String) paramMap.get("imageType");
//
//        // 외부 루트(image upload folder)에 저장된 image 내용을 담은 image 객체 생성한다.
//        Image image = fileUtils.saveImage(file, imageType);
//
//        // db에 전달할 Image entity 생성(정보는 내부에 저장 , 실제 데이터는 외부 루트에 저장)
//        Optional<Object> imageEntity = Optional.empty();
//        System.out.println("imageEntity = " + imageEntity);
//        // 유저와 이미지 관계 매핑
////        PrincipalDetails userDetails = (PrincipalDetails)authentication.getPrincipal();
//        // 이미지 등록 요청한 유저 entity 찾아오기
//
////        User principal = userService.findByEmail(userDetails.getEmail());
////        System.out.println("principal = " + principal);
//
//        if(image.getImageType().equals(ImageType.USER_PROFILE_IMAGE)) {
//            Object img = imageEntity.orElse(fileService.createUserProfileEntity(image));
//            System.out.println("img = " + img);
//
//            // 유저와 이미지 연관관계 매핑
////            principal.userToProfileImage((UserProfileImage) img);
//        }
//        else if(image.getImageType().equals(ImageType.USER_BACKGROUND_PROFILE_IMAGE)) {
//            Object img = imageEntity.orElse(fileService.createBackgroundProfileImageEntity(image));
//
//            // 유저와 이미지 연관관계 매핑
////            principal.userToBDProfileImage( (BackgroundProfileImage) img);
//        }
//        else {
//            throw new DoNotFoundImageObjectException("image Entity is null");
//        }
//
//        BoardImage boardImage = image;
//
//        return boardRepository.save(boardSaveDTO.toEntity(), boardImage).getId());
//    }


    @Transactional
    public Board findById2(Long id) {
        return boardRepository.findById(id).get();
    }

    @Transactional
    public Long update(Long id, BoardUpdateDTO updateDTO){
        Board board = boardRepository.findById(id).orElseThrow(()-> new IllegalStateException("게시글이 없습니다. id="+id));
        board.update(updateDTO.getTitle(), updateDTO.getContent());

//        fileService.saveFile(updateDTO);
//
//        return board.getId();
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

    @Transactional
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

    public void remove(Long id) throws CommentException {
        Board board = boardRepository.findById(id).orElseThrow(() -> new BoardException(BoardExceptionType.BOARD_NOT_POUND));

        if(!board.getAuthor().getNickname().equals(SecurityUtil.getLoginNickname())){
            throw new BoardException(BoardExceptionType.NOT_AUTHORITY_DELETE_BOARD);
        }

        board.remove();
    }




}

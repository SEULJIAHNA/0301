package shareYourFashion.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import shareYourFashion.main.domain.Board;
import shareYourFashion.main.domain.BoardImage;
import shareYourFashion.main.domain.Comment;
import shareYourFashion.main.dto.BoardImageDTO;
import shareYourFashion.main.dto.BoardRequestDTO;
import shareYourFashion.main.dto.BoardSaveRequestDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTitleOrContent(String title, String content);
    Page<Board> findByTitleContainingOrContentContaining(String title, String content, Pageable Pageable);

    String UPDATE_BOARD="UPDATE Board "+
            "SET TITLE = :#{#boardRequestDTO.title}, " +
            "CONTENT = :#{#boardRequestDTO.content}, " +
            "HASHTAG = :#{#boardRequestDTO.hashTag}, " +
            "UPDATE_DATE = NOW() "+
            "WHERE ID = :#{#boardRequestDTO.id}";
    @Transactional
    @Modifying
    @Query(value = UPDATE_BOARD, nativeQuery = true) //상세글 조회 및 수정
    public int updateBoard(@Param("boardRequestDTO") BoardRequestDTO boardRequestDTO);





    static final String DELETE_BOARD = "DELETE FROM Board "//게시물삭제
            + "WHERE ID IN (:deleteList)";


    @Transactional
    @Modifying
    @Query(value = DELETE_BOARD, nativeQuery = true)
    public int deleteBoard(@Param("deleteList") Long[] deleteList);


//

    static final String UPDATE_BOARD_READ_CNT_INC = "UPDATE BOARD " //조회수 숫자 증가
            + "SET VIEW = VIEW + 1 "
            + "WHERE BOARD_ID = :id";
    @Transactional
    @Modifying
    @Query(value = UPDATE_BOARD_READ_CNT_INC, nativeQuery = true)
    int updateView(@Param(value = "id") Long id);

    @EntityGraph(attributePaths = {"author"})
    Optional<Board> findWithAuthorById(Long id);

    Long save(BoardSaveRequestDTO saveDTO, BoardImageDTO boardImageDTO);

}

package shareYourFashion.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shareYourFashion.main.domain.Board;
import shareYourFashion.main.domain.Comment;
import shareYourFashion.main.dto.CommentInfoDTO;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    public Page<Comment> findByBoardId(Long boardId, Pageable Pageable);


}

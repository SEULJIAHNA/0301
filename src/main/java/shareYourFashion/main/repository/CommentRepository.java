package shareYourFashion.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shareYourFashion.main.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {



}

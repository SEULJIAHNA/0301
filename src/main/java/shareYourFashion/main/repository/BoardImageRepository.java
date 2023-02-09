package shareYourFashion.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shareYourFashion.main.domain.Board;
import shareYourFashion.main.domain.BoardImage;
import shareYourFashion.main.domain.UserProfileImage;
import shareYourFashion.main.dto.BoardImageDTO;

import java.util.Optional;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImageRepository, Long>{
    Optional<BoardImage> findByImageFileName(String fileName);

}

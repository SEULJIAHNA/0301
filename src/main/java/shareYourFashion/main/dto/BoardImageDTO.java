package shareYourFashion.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shareYourFashion.main.constant.ImageType;
import shareYourFashion.main.domain.Board;
import shareYourFashion.main.domain.BoardImage;
import shareYourFashion.main.domain.valueTypeClass.Image;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardImageDTO {

    private Long id;

    private Board board;

    private Image image;

    @NotEmpty
    private String fileName;

    @NotEmpty
    private String fileOriginName;

    @NotEmpty
    private String storedFilePath;

    @NotEmpty
    private ImageType imageType;

//    public BoardImageDTO(BoardImage boardImage){
//        this.id = boardImage.getId();
//        this.board = boardImage.getBoard();
//        this.image = boardImage.getImage();
//    }

//    public BoardImage toEntity()
//
//    {
//        return BoardImage.builder()
//                .id(id)
//                .board(board)
//                .image(image)
//                .build();
//    }

}

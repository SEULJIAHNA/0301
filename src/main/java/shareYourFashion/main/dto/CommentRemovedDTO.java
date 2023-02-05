package shareYourFashion.main.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class CommentRemovedDTO {

    @NotEmpty(message = "board's id is not exists.")
    private Long id;
    private boolean isRemoved;


    @Builder
    public CommentRemovedDTO(Long id, boolean isRemoved){
        this.id =id;
        this.isRemoved = isRemoved;
    }
}

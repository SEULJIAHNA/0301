package shareYourFashion.main.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import shareYourFashion.main.domain.User;

@Data
@NoArgsConstructor
public class UserInfoDTO {

    private String nickName;
    private String age;



    @Builder
    public UserInfoDTO(User user) {
        this.nickName = user.getNickname();
        this.age = user.getAge();
    }
}

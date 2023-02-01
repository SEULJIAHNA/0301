package shareYourFashion.main.domain;

import org.springframework.security.crypto.password.PasswordEncoder;
import shareYourFashion.main.constant.Roles;
import shareYourFashion.main.dto.UserFormDTO;
import shareYourFashion.main.constant.JoinPath;
import shareYourFashion.main.constant.Sex;
import shareYourFashion.main.domain.valueTypeClass.Address;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter @Setter
@Table(name = "users")
public class User extends BaseTimeEntity{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column( unique = true , length = 10)
    private String nickname;

    @Column( unique = true , length = 30)
    private String email;

    @Column( length = 100) // 비밀번호를 해쉬로 암호화하여 저장하기 위해 길이 충분히 줌
    private String password; // 패스워드

    @Column
    private String age;

    @Column
    @Embedded
    private Address address;

    @Column
    @Enumerated(EnumType.STRING)
    private Sex sex;

    @Column(nullable = false , columnDefinition = "varchar(30) default 'ROLE_USER'")
    @Enumerated(EnumType.STRING)
    private Roles roles;

    @Column( columnDefinition = "varchar(30) default 'DEFAULT_ACCOUNT'") // 기본 값 사이트 경로로 지정
    @Enumerated(EnumType.STRING)
    private JoinPath joinPath;

    // user entity에 대한 모든 작업을 like entity에 전파
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Like> like = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "userProfileImage_id")
    private UserProfileImage profileImage;

    @OneToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "backgroundProfileImage_id")
    private BackgroundProfileImage backgroundImage;

    // follow (User n : 1 follow)
    @OneToMany(fetch = FetchType.LAZY  , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Follow> follows = new ArrayList<>();

    // user 1 : n board -> user 삭제 시 관련된 모든 board 삭제 (생명주기 user에 위임)
    @OneToMany(mappedBy = "author" ,cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();
    // user 1 : n lookbook -> user 삭제 시 관련된 모든 lookbook 삭제
    @OneToMany(mappedBy = "writer" ,cascade = CascadeType.ALL , orphanRemoval = true)
    private List<LookBook> lookBooks = new   ArrayList<>();
    @OneToMany(mappedBy = "writer" ,cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    //== 연관관계 메소드 ==//
    public User hashPassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
        return this;
    }

    /*userJoinForm을 통하 user 가입 메소드 */
    public static User create(UserFormDTO userFormDto) {

        User user = new User().builder()
                .roles(userFormDto.getUser_role()).age(userFormDto.getAge())
                .nickname(userFormDto.getNickname()).email(userFormDto.getEmail())
                .address(userFormDto.getAddress()).joinPath(userFormDto.getJoinPath())
                .sex(userFormDto.getSex()).password(userFormDto.getPassword())
                .build();

        return user;
    }

    //== 연관관계 메소드 ==//
    public void userToProfileImage(UserProfileImage profileImage) {
        this.profileImage = profileImage;
        profileImage.setUser(this);
    }

    public void userToBDProfileImage(BackgroundProfileImage backgroundProfileImage) {
        this.backgroundImage = backgroundProfileImage;
        backgroundProfileImage.setUser(this);
    }

    public void addComment(Comment comment){
        //comment의 writer 설정은 comment에서 함
        comments.add(comment);
    }



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", age='" + age + '\'' +
                ", address=" + address +
                ", sex=" + sex +
                ", roles=" + roles +
                ", joinPath=" + joinPath +
                '}';
    }



    //    // user 1 : n chat
//    @OneToMany(mappedBy = "chat" , cascade = CascadeType.ALL , orphanRemoval = true)
//    private List<Chat> chats = new ArrayList<>();
//
    //

}

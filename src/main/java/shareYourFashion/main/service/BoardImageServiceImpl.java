package shareYourFashion.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shareYourFashion.main.domain.BackgroundProfileImage;
import shareYourFashion.main.domain.BoardImage;
import shareYourFashion.main.domain.UserProfileImage;
import shareYourFashion.main.domain.valueTypeClass.Image;
import shareYourFashion.main.repository.BackgroundProfileImageRepository;
import shareYourFashion.main.repository.ProfileImageRepository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardImageServiceImpl implements BoardImageService {

    @Autowired
    private BackgroundProfileImageRepository backgroundProfileImageRepository;

    @Autowired
    private ProfileImageRepository profileImageRepository;

//    // 이미지 entity db에 저장 하는 메소드
//    @Transactional
//    public void storeImageToDB(@NotNull User user, @NotNull BoardImage image)  {
//
//        // create imageEntity
//        if(image.getImageType() == ImageType.USER_PROFILE_IMAGE) { // 유저 프로파일 이미지 생성
//            UserProfileImage profileEntity = UserProfileImage.createUserProfileEntity(image);
//
//            // 이미지 등록한 유저와 일대일 맵핑
//            user.userToProfileImage(profileEntity);
//
//            profileImageRepository.save(profileEntity);
//        }
//
//
//    }


    // 유저 프로파일 이미지 찾기
    @Override
    public UserProfileImage findProfileImageByImageFileName(String fileName) {
        Optional<UserProfileImage> profileImage = profileImageRepository.findByImageFileName(fileName);
        if(!profileImage.isPresent()) {
            // 찾는 이미지가 없을 경우 null 리턴

            return null;
        }

        return profileImage.get();
    }

    // 유저 백그라운드 이미지 찾기
    @Override
    public BackgroundProfileImage findBackgroundImageByFileName(String fileName) {

        Optional<BackgroundProfileImage> bgProfileImage = backgroundProfileImageRepository.findByImageFileName(fileName);
        if(!bgProfileImage.isPresent()) {  // 찾는 이미지가 없을 경우 null 리턴

            return null;
        }

        return bgProfileImage.get();
    }

    @Override
    public String findProfileImageURL(UserProfileImage profileImage) {
        if(profileImage == null) { // profile image 존재하지 않을 시 default profile url 반환
            return Image.getDefaultProfileURL();
        }

        return profileImage.getImage().getStoredFilePath();
    }

    @Override
    public String findBgProfileURL(BackgroundProfileImage bgProfileImage) {
        if(bgProfileImage == null) { // profile image 존재하지 않을 시 default profile url 반환
            return Image.getDefaultProfileURL();
        }

        return bgProfileImage.getImage().getStoredFilePath();
    }

    @Override
    @Transactional // dirty checking 된 유저 와 이미지 관계 업데이트 내용 서버(db)로 전달 및 적용
    public UserProfileImage createUserProfileEntity( @NotNull BoardImage image) {

        // 파라미터로 받은 Image 정보를 담은 객체(profile image Entity에 embedded object)를 넣어 profile entity 생성
        UserProfileImage profileImage = new UserProfileImage().builder().image(image)
                .build();


        return profileImage;

    }

    @Override
    @Transactional
    public BackgroundProfileImage createBackgroundProfileImageEntity(@NotNull BoardImage image) {
        // 파라미터로 받은 Image 정보를 담은 객체(profile image Entity에 embedded object)를 넣어 profile entity 생성
        BackgroundProfileImage profileImage = new BackgroundProfileImage().builder().image(image)
                .build();

        return profileImage;
    }
}

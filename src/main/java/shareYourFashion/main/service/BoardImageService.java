package shareYourFashion.main.service;

import shareYourFashion.main.domain.BackgroundProfileImage;
import shareYourFashion.main.domain.BoardImage;
import shareYourFashion.main.domain.UserProfileImage;
import shareYourFashion.main.domain.valueTypeClass.Image;

public interface BoardImageService {

//    void storeImageToDB(User user , Image image);

    /*
     *  image crud 서비스
     * */


    UserProfileImage findProfileImageByImageFileName(String fileName);

    BackgroundProfileImage findBackgroundImageByFileName(String fileName);

    UserProfileImage createUserProfileEntity(BoardImage image);

    BackgroundProfileImage createBackgroundProfileImageEntity(BoardImage image);

    String findProfileImageURL(UserProfileImage profileImage);

    String findBgProfileURL(BackgroundProfileImage bgProfileImage);
}

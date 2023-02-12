package shareYourFashion.main.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import shareYourFashion.main.constant.ImageType;
import shareYourFashion.main.domain.BackgroundProfileImage;
//import shareYourFashion.main.domain.BoardImage;
import shareYourFashion.main.domain.UserProfileImage;
import shareYourFashion.main.domain.valueTypeClass.Image;
import shareYourFashion.main.dto.BoardImageDTO;
import shareYourFashion.main.dto.ImageDTO;
import shareYourFashion.main.exception.DoNotFoundImageObjectException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

//@Service
//@Transactional(readOnly = true)
//public class BoardImageUtils {
//
//
//    @Value("${custom.path.absolute-user-image-path}")
//    private static String EXTERNAL_IMAGE_PATH = "C://seul//projectimage";
//
//    private final static String IMAGE_ABSOLUTE_PATH; // 파일이 저장될 절대 경로
//
//    private final FileService fileService;
//
//    static {
//        assert false;
//        System.out.println("EXTERNAL_IMAGE_PATH = " + EXTERNAL_IMAGE_PATH);
//        IMAGE_ABSOLUTE_PATH = new File(EXTERNAL_IMAGE_PATH).getAbsolutePath() + "/";
//    }
//
//
//    private static final Logger log = LoggerFactory.getLogger(BoardImageUtils.class);
//
//
//    public BoardImageUtils(FileService fileService) {
//        this.fileService = fileService;
//    }
//
//    public BoardImage saveImage(MultipartFile boardImage , String type) throws IOException , IllegalArgumentException {
//
//        ImageType imageType = ImageType.valueOf(type);
//
//        BoardImage imageObj = null;
//
//        // 폴더 생성과 파일명 새로 부여를 위한 현재 시간 알아내기
//        LocalDateTime now = LocalDateTime.now();
//        String year = Integer.toString(now.getYear()) + "년";
//        String month = Integer.toString(now.getMonthValue()) + "월";
//        String day = Integer.toString(now.getDayOfMonth())  + "일";
//        int hour = now.getHour();
//        int minute = now.getMinute();
//
//        String absolutePath = IMAGE_ABSOLUTE_PATH + imageType.toString() + "/"; // 파일이 저장될 절대 경로
//        String fileExtension = extractExt(boardImage.getOriginalFilename()); // 확장자 축출
//        String storeFileName = UUID.randomUUID().toString(); // 새로 부여한 이미지명
//        String path =  year + "/" + month + "/" + day; // 저장될 폴더 경로
//        try {
//
//            if(!boardImage.isEmpty()) {
//                File file = new File(absolutePath + path);
//
//                if(!file.exists()){
//                    file.mkdirs(); // mkdir()과 다르게 상위 폴더가 없을 때 상위폴더까지 생성
//                }
//
//                file = new File(absolutePath + path + "/" + storeFileName + "."  + fileExtension);
//
//
//                // 파일 저장하는 부분 -> 파일 경로 + storeFileName + fileExtraction 에 저장
//                boardImage.transferTo(file);
//
//                // 파일 저장 후 image entity 생성을 위해 imageDto를 embedded Object인 Image로 변경
//                BoardImageDTO boardImageDTO = BoardImageDTO.builder()
//                        .fileOriginName(boardImage.getOriginalFilename())
//                        .storedFilePath(storeFileName)
//                        .fileName(storeFileName + "." + fileExtension)
//                        .imageType(imageType)
//                        .build();
//
//
//                // image file Image entity의 embedded object로 들어가는 Image Object로 변환 한다.
//                imageObj = this.transferToImageObject(boardImageDTO);
//
//                // 이미지 파일명이 존재 하지 않을경우 이미지 경로를 얻을 수 없으니 예외 처리해야 한다.
//                if(imageObj.getFileName() == null) throw new IllegalArgumentException("image file name doesn't exists");
//
//
//            } else {
//                throw new DoNotFoundImageObjectException("do not found image object (MultipartFile size = 0)");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return imageObj;
//    }
//
//    @Transactional
//    public BoardImage transferToImageObject (BoardImageDTO imgDto) throws IOException {
//        return new BoardImage().builder().fileName(imgDto.getFileName())
//                .fileOriginName(imgDto.getFileOriginName())
//                .storedFilePath(imgDto.getStoredFilePath())
//                .imageType(imgDto.getImageType())
//                .build();
//    }
//
//    // 요청 온 유저 background profile 외부 이미지 파일을 담은 responseEntity 생성 후 반환
//    @Transactional
//    public ResponseEntity<Resource> getBGProfileResponseEntity(BackgroundProfileImage image) throws IOException {
//
//        String absolutePath;
//        String imagePath;
//        HttpHeaders header = new HttpHeaders();
//
//        // 실제 이미지가 저장된 외부 파일 위치 반환
//        absolutePath = IMAGE_ABSOLUTE_PATH + ImageType.USER_BACKGROUND_PROFILE_IMAGE.toString()+ "/"; // 파일이 저장될 절대 경로
//
//        // 유저 프로파일로 저장된 이미지가 기본 이미지 인 경우 또는 파라미터로 넘어온 이미지가 null 인경우 기본 이미지 resource 반환
//        if(image == null || image.getImage().getFileOriginName().equals("defaultBDProfileImg.jpg")) {
//
//            imagePath = absolutePath + "defaultBDProfileImg.jpg";
//
//            Resource resource = new FileSystemResource(imagePath);
//
//
//            Path filePath = Paths.get(imagePath); // file 경로 가져오기
//
//            header.add("Content-Type" , Files.probeContentType(filePath));
//
//            return new ResponseEntity<Resource>(resource ,header , HttpStatus.OK);
//        }
//
//        return createImageEntity(image.getCreatedDate() ,image.getImage().getFileName() , absolutePath );
//
//    }
//
//    // 요청 온 유저 프로파일 외부 이미지 파일을 담은 responseEntity 생성 후 반환
//    @Transactional
//    public ResponseEntity<Resource> getProfileResponseEntity(UserProfileImage image) throws IOException {
//
//        String absolutePath;
//        String imagePath;
//        HttpHeaders header = new HttpHeaders();
//
//        // 실제 이미지가 저장된 외부 파일 위치 반환
//        absolutePath = IMAGE_ABSOLUTE_PATH + ImageType.USER_PROFILE_IMAGE.toString() + "/"; // 파일이 저장될 절대 경로
//
//        // 유저 프로파일로 저장된 이미지가 기본 이미지 인 경우 또는 파라미터로 넘어온 이미지가 null 인경우 기본 이미지 resource 반환
//        if(image == null || ((UserProfileImage) image).getImage().getFileOriginName().equals("defaultProfileImage.jpeg") ) {
//
//            imagePath = absolutePath + "defaultProfileImage.jpeg";
//
//            Resource resource = new FileSystemResource(imagePath);
//
//
//            Path filePath = Paths.get(imagePath); // file 경로 가져오기
//
//            header.add("Content-Type" , Files.probeContentType(filePath));
//
//            return new ResponseEntity<Resource>(resource ,header , HttpStatus.OK);
//        }
//
//        // default image가 아닌 유저가 저장한 이미지 불러오기
//
//        // 이미지 폴더 생성 날짜 ( ex 2022 10월 4일 -> imageType/2022/10월/4일/imageName 루트에 저장됨)
//
//        return createImageEntity(image.getCreatedDate() ,image.getImage().getFileName() , absolutePath );
//    }
//
//
//    // 확장자 추출
//    private String extractExt(String originalFilename) {
//        int pos = originalFilename.lastIndexOf(".");
//        return originalFilename.substring(pos + 1);
//    }
//
//    // default image 가 아닌 외부 이미지를 담은 responseEntity 생성
//    private ResponseEntity<Resource> createImageEntity(LocalDateTime createdAt , String fileName , String absolutePath) {
//
//        String year = Integer.toString(createdAt.getYear())+"년";
//        String month = Integer.toString(createdAt.getMonthValue()) + "월";
//        String day = Integer.toString(createdAt.getDayOfMonth()) + "일";
//
//        String imagePath = absolutePath + year +"/" + month + "/" + day + "/" + fileName;
//
//        HttpHeaders header = new HttpHeaders();
//
//        log.info("image path : " + imagePath );
//
//        // file:///Users/ahnkwanghyun/uploads/fashionCommunity/UserProfileImage/
//        Resource resource = new FileSystemResource(imagePath);
//
//        if(!resource.exists()){ // 이미지가 존재하지 않으면 404 에러 발생
//            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
//        }
//        try {
//            Path filePath = Paths.get(imagePath); // file 경로 가져오기
//            // 이미지의 확장자명(.jpg , .png)에 따라 달라지는 content-type , image/jpeg or image/png
//            header.add("Content-Type" , Files.probeContentType(filePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return new ResponseEntity<Resource>(resource , header , HttpStatus.OK);
//    }
//
//}

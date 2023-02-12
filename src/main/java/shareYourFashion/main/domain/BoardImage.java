//package shareYourFashion.main.domain;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.DynamicInsert;
//import shareYourFashion.main.constant.ImageType;
//import shareYourFashion.main.domain.valueTypeClass.Image;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotEmpty;
//
//@Entity
//@Data
//@DynamicInsert // db에 insert 시 null인 field 제외
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class BoardImage extends BaseTimeEntity{
//
//    @Column(name= "boardImage_id")
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "board_id")
//    private Board board;
//
//    @Column(nullable = false)
//    @Embedded
//    private Image image;
//
//    @NotEmpty
//    private String fileName;
//
//    @NotEmpty
//    private String fileOriginName;
//
//    @NotEmpty
//    private String storedFilePath;
//
//    @NotEmpty
//    private ImageType imageType;
//}

package blogsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdDTO {
    private String adId;
    private String adTypeId;
    private String adTypeTitle;
    private String adTitle;
    private String adImgUrl;
    private String adLinkUrl;
    private Integer adSort;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime adBeginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime adEndTime;
//    private LocalDateTime adAddTime;


}

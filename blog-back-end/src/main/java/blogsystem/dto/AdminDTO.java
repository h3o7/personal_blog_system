package blogsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {
    private Integer articleCount;
    private Integer articleTagCount;
    private Integer adTypeCount;
    private Integer userCount;
//    private String systemVersion;
    private String systemName;
    private String systemIP;

}

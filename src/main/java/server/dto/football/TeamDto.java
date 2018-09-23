package server.dto.football;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamDto {
    private Long id;
    private AreaDto area;
    private String name;
    private String shortName;
    private String tla;
    private String address;
    private String phone;
    private String website;
    private String email;
    private String crestUrl;
    private Long founded;
    private String clubColors;
    private String venue;

}

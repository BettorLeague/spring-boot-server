package server.dto.authentification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class  JwtAuthenticationRequest {
    private String username;
    private String password;
}

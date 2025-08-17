package fr.sd.reservcreneaux.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

    @Schema(description = "Username", required = true, example = "login")
    private String username;
    @Schema(description = "Password", required = true, example = "12345678")
    private String password;

    public void LoginDto(String username) {
        this.username = username;
    }
}

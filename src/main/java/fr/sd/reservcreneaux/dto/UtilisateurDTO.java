package fr.sd.reservcreneaux.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UtilisateurDTO {
    @NotBlank(message = "Le nom d'utilisateur  est obligatoire")
    @Size(min = 6, message = "Le nom d'utilisateur doit contenir au moins 6 caractères")
    public String username;
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    public String email;
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    public String password;
    public String role;
    public String statut;
    public ProfilUtilisateurDTO profile;

    public UtilisateurDTO() {
    }

    public UtilisateurDTO(String username, String email, String role, String statut) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.statut = statut;

    }

    public UtilisateurDTO(String username, String email, String role, String statut, String password) {
        this(username, email, role, statut);
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ProfilUtilisateurDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfilUtilisateurDTO profile) {
        this.profile = profile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}

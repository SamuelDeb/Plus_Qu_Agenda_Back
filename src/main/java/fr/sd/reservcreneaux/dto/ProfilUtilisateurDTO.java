package fr.sd.reservcreneaux.dto;

public class ProfilUtilisateurDTO {
    public String nom;
    public String prenom;
    public String adresse;
    public String description;

    public ProfilUtilisateurDTO() {
    }

    public ProfilUtilisateurDTO(String nom, String prenom, String adresse, String description) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.description = description;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

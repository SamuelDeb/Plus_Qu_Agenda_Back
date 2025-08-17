package fr.sd.reservcreneaux.entities;

import java.util.Objects;

public class ProfilUtilisateur {
    public String nom;
    public String prenom;
    public String adresse;
    public String description;

    public ProfilUtilisateur() {
    }

    public ProfilUtilisateur(String nom, String prenom, String adresse, String description) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProfilUtilisateur that = (ProfilUtilisateur) o;
        return Objects.equals(nom, that.nom) && Objects.equals(prenom, that.prenom) && Objects.equals(adresse, that.adresse) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(nom);
        result = 31 * result + Objects.hashCode(prenom);
        result = 31 * result + Objects.hashCode(adresse);
        result = 31 * result + Objects.hashCode(description);
        return result;
    }

    @Override
    public String toString() {
        return "ProfilUtilisateur{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

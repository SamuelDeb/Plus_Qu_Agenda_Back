package fr.sd.reservcreneaux.dto;

import java.time.LocalDate;

public class CreneauxDTO {
    public String id;
    public LocalDate date;
    public String heureDebut;
    public String heureFin;
    public String type;
    public String creeParUsername;
    public String reserverParUsername;
    public String statut = "disponible";
    public String description;

    public CreneauxDTO() {
    }

    public CreneauxDTO(LocalDate date, String heureDebut, String heureFin, String type, String creeParUsername, String description) {
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.type = type;
        this.creeParUsername = creeParUsername;
        this.description = description;
    }

    public CreneauxDTO(String string, String creeParUsername, String heureDebut, String heureFin, String type) {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreeParUsername() {
        return creeParUsername;
    }

    public void setCreeParUsername(String creeParUsername) {
        this.creeParUsername = creeParUsername;
    }

    public String getReserverParUsername() {
        return reserverParUsername;
    }

    public void setReserverParUsername(String reserverParUsername) {
        this.reserverParUsername = reserverParUsername;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void creeParUsername(String creeParUsername) {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

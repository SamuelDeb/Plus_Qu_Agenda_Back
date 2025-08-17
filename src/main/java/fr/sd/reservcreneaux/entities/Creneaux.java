package fr.sd.reservcreneaux.entities;


import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.Objects;

@MongoEntity(collection = "creneaux")

public class Creneaux extends PanacheMongoEntity {

    public ObjectId id;
    public LocalDate date;
    public String heureDebut;
    public String heureFin;
    public String type;
    public String creeParUsername;
    public String reserverParUsername;
    public String statut = "disponible";
    public String description;

    public Creneaux() {
    }

    public Creneaux(LocalDate date, String heureDebut, String heureFin, String type, String creeParUsername, String reserverParUsername, String statut, String description) {
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.type = type;
        this.creeParUsername = creeParUsername;
        this.reserverParUsername = reserverParUsername;
        this.statut = statut;
        this.description = description;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
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

        Creneaux creneaux = (Creneaux) o;
        return Objects.equals(id, creneaux.id) && Objects.equals(date, creneaux.date) && Objects.equals(heureDebut, creneaux.heureDebut) && Objects.equals(heureFin, creneaux.heureFin) && Objects.equals(type, creneaux.type) && Objects.equals(creeParUsername, creneaux.creeParUsername) && Objects.equals(reserverParUsername, creneaux.reserverParUsername) && Objects.equals(statut, creneaux.statut) && Objects.equals(description, creneaux.description);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(date);
        result = 31 * result + Objects.hashCode(heureDebut);
        result = 31 * result + Objects.hashCode(heureFin);
        result = 31 * result + Objects.hashCode(type);
        result = 31 * result + Objects.hashCode(creeParUsername);
        result = 31 * result + Objects.hashCode(reserverParUsername);
        result = 31 * result + Objects.hashCode(statut);
        result = 31 * result + Objects.hashCode(description);
        return result;
    }

    @Override
    public String toString() {
        return "Creneaux{" +
                "id=" + id +
                ", date=" + date +
                ", heureDebut='" + heureDebut + '\'' +
                ", heureFin='" + heureFin + '\'' +
                ", type='" + type + '\'' +
                ", creeParUsername='" + creeParUsername + '\'' +
                ", reserverParUsername='" + reserverParUsername + '\'' +
                ", statut='" + statut + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                '}';
    }
}

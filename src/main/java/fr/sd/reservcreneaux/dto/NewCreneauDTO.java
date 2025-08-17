package fr.sd.reservcreneaux.dto;

public class NewCreneauDTO {

    public String creeParUsername;
    public String heureDebut;
    public String heureFin;
    public String type;

    public NewCreneauDTO() {
    }

    public NewCreneauDTO(String creeParUsername, String heureDebut, String heureFin, String type) {
        this.creeParUsername = creeParUsername;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.type = type;
    }
}

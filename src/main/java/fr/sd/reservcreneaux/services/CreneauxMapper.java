package fr.sd.reservcreneaux.services;

import fr.sd.reservcreneaux.dto.CreneauxDTO;
import fr.sd.reservcreneaux.entities.Creneaux;
import org.bson.types.ObjectId;

public class CreneauxMapper {

    public static CreneauxDTO toDTO(Creneaux creneau) {
        return new CreneauxDTO(
                creneau.id.toString(),
                creneau.creeParUsername,
                creneau.heureDebut,
                creneau.heureFin,
                creneau.type
        );
    }

    public static Creneaux fromDTO(CreneauxDTO dto) {
        Creneaux creneau = new Creneaux();
        creneau.id = new ObjectId(String.valueOf(dto.id));
        creneau.creeParUsername = dto.creeParUsername;
        creneau.heureDebut = dto.heureDebut;
        creneau.heureFin = dto.heureFin;
        creneau.type = dto.type;
        return creneau;
    }
}
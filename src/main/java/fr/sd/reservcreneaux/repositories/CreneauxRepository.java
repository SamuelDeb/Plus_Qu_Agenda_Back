package fr.sd.reservcreneaux.repositories;

import fr.sd.reservcreneaux.entities.Creneaux;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class CreneauxRepository implements PanacheMongoRepository<Creneaux> {
    // Méthode pour trouver tous les créneaux par username
    public List<Creneaux> findByUsername(String username) {
        return list("createdByUsername", username);
    }

    // Méthode pour trouver un créneau spécifique par ID et username
    public Creneaux findByIdAndUsername(String id, String username) {
        return find("{'id': ?1, 'createdByUsername': ?2}", id, username).firstResult();
    }

    // Méthode personnalisée pour trouver un créneau par ObjectId
    public Creneaux findByObjectId(String id) {
        ObjectId objectId;
        try {
            objectId = new ObjectId(id);
        } catch (IllegalArgumentException e) {
            // ObjectId non valide
            return null;
        }
        return findById(objectId);
    }


    public List<Creneaux> findByReserverParUsername(String username) {
        return list("reserverParUsername", username);
    }
}

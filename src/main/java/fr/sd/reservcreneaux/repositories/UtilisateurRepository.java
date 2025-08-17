package fr.sd.reservcreneaux.repositories;

import fr.sd.reservcreneaux.entities.Utilisateur;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UtilisateurRepository implements PanacheMongoRepository<Utilisateur> {

    public Utilisateur findByUsername(String username) {
        return find("username", username).firstResult();
    }

    public Utilisateur findByEmail(String email) {
        return find("email", email).firstResult();
    }

}

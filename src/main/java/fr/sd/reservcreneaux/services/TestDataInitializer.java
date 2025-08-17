package fr.sd.reservcreneaux.services;

import fr.sd.reservcreneaux.entities.Utilisateur;
import fr.sd.reservcreneaux.repositories.UtilisateurRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class TestDataInitializer {

    @Inject
    UtilisateurRepository userRepository;

    @Inject
    UtilisateurService userService;

    public void generateTestData() {
        List<Utilisateur> users = Arrays.asList(
                createUser("admin", "admin@example.com", "adminpassword", "admin", "actif"),
                createUser("user1", "user1@example.com", "user1password", "user", "actif"),
                createUser("user2", "user2@example.com", "user2password", "user", "actif"),
                createUser("inactiveuser", "inactiveuser@example.com", "inactivepassword", "user", "inactif"),
                createUser("user3", "user3@example.com", "user3password", "user", "actif")
        );

        for (Utilisateur user : users) {
            if (userRepository.find("username", user.username).firstResult() == null) {
                userRepository.persist(user);
            }
        }
    }

    private Utilisateur createUser(String username, String email, String password, String role, String statut) {
        Utilisateur user = new Utilisateur();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(userService.hashPassword(password)); // Ensure password is hashed
        user.setRole(role);
        user.setStatut(statut);
        return user;
    }
}

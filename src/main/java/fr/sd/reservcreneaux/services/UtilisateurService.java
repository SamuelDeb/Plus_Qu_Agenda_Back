package fr.sd.reservcreneaux.services;

import fr.sd.reservcreneaux.entities.Utilisateur;
import fr.sd.reservcreneaux.repositories.UtilisateurRepository;
import fr.sd.reservcreneaux.util.CodeUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@ApplicationScoped

public class UtilisateurService {
    @Inject
    UtilisateurRepository userRepository;


    //Validate email adresse format
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    //validate password stregth
    public static boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean checkPassword(String plainTextPassword, String hashedPassword) {
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }

    public String getRoleForLogin(String login) {
        Utilisateur user = userRepository.findByUsername(login);
        if (user != null) {
            return user.getRole();
        } else {
            return "user";

        }
    }

public void changePassword(String username, String oldPassword, String newPassword) {
    Utilisateur user = userRepository.findByUsername(username);
    if (user != null && checkPassword(oldPassword, user.getPassword())) {
        user.setPassword(hashPassword(newPassword));
        userRepository.persistOrUpdate(user);
    } else {
        throw new IllegalArgumentException("Invalid old password");
    }
}

    private Map<String, String> resetCodes = new HashMap<>();

    public Utilisateur findUserByUsername(String username) {
        return userRepository.find("username", username).firstResult();
    }

    public void saveVerificationCode(String username, String code) {
        Utilisateur user = findUserByUsername(username);
        if (user != null) {
            user.setVerificationCode(code);
            userRepository.persistOrUpdate(user);
        }
    }

    public boolean isVerificationCodeValid(String username, String code) {
        Utilisateur user = findUserByUsername(username);
        return user != null && code.equals(user.getVerificationCode());
    }

    public void updatePassword(String username, String newPassword) {
        Utilisateur user = findUserByUsername(username);
        if (user != null) {
            user.setPassword(hashPassword(newPassword)); // Assurez-vous de hasher le mot de passe avant de le sauvegarder
            userRepository.persistOrUpdate(user);
        }
    }

    public String generateResetCode() {
        return CodeUtil.generateRandomCode();
    }

}

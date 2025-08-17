package fr.sd.reservcreneaux.entities;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Objects;

@MongoEntity(collection = "utilisateur")

public class Utilisateur extends PanacheMongoEntity {

    public ObjectId id;
    @BsonProperty("username")
    public String username;
    @BsonProperty("password")
    public String password;
    @BsonProperty("email")
    public String email;
    @BsonProperty("role")
    public String role = "user";
    @BsonProperty("statut")
    public String statut = "actif";
    public ProfilUtilisateur profile;
    @BsonProperty("verification_code")
    private String verificationCode;

    public Utilisateur() {
    }


    public Utilisateur(String username, String password, String email, String role, String statut) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.statut = statut;
    }

    public Utilisateur(String username, String password, String email, String role, String statut, ProfilUtilisateur profile) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.statut = statut;
        this.profile = profile;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public ProfilUtilisateur getProfile() {
        return profile;
    }

    public void setProfile(ProfilUtilisateur profile) {
        this.profile = profile;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Utilisateur that = (Utilisateur) o;
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(password, that.password) && Objects.equals(email, that.email) && Objects.equals(role, that.role) && Objects.equals(statut, that.statut) && Objects.equals(profile, that.profile);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(username);
        result = 31 * result + Objects.hashCode(password);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(role);
        result = 31 * result + Objects.hashCode(statut);
        result = 31 * result + Objects.hashCode(profile);
        return result;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", statut='" + statut + '\'' +
                ", profile=" + profile +
                '}';
    }
}

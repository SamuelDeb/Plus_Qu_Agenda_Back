package fr.sd.reservcreneaux.resources;
import fr.sd.reservcreneaux.dto.LoginDTO;
import fr.sd.reservcreneaux.dto.NewUtilisateurDTO;
import fr.sd.reservcreneaux.entities.Utilisateur;
import fr.sd.reservcreneaux.repositories.UtilisateurRepository;
import fr.sd.reservcreneaux.services.UtilisateurService;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class AuthResourceTest {

    @Inject
    UtilisateurRepository userRepository;

    @BeforeEach
    public void setup() {
        // Clean up database before each test
        userRepository.deleteAll();

        // Optionally, create some users for testing
        createUser("existingUser", "existing@example.com", "existingpassword");
    }

    @Test
    public void testCreateUser() {
        NewUtilisateurDTO newUser = new NewUtilisateurDTO("newuser", "newuser@example.com", "validpassword");

        given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("username", is("newuser"));
    }

    @Test
    public void testCreateUserWithExistingUsername() {
        NewUtilisateurDTO newUser = new NewUtilisateurDTO("existingUser", "newuser1@example.com", "validpassword");

        given()
                .contentType(ContentType.JSON)
                .body(newUser)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(containsString("Username already in use"));
    }

    @Test
    public void testLoginSuccessful() {
        createUser("loginUser", "loginuser@example.com", "loginpassword");

        LoginDTO loginDto = new LoginDTO("loginUser", "loginpassword");

        given()
                .contentType(ContentType.JSON)
                .body(loginDto)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(containsString("Token: "));
    }

    @Test
    public void testLoginUserNotFound() {
        LoginDTO loginDto = new LoginDTO("nonexistentuser", "password");

        given()
                .contentType(ContentType.JSON)
                .body(loginDto)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testLoginInactiveUser() {
        createUser("inactiveUser", "inactiveuser@example.com", "inactivepassword", "inactif");

        LoginDTO loginDto = new LoginDTO("inactiveUser", "inactivepassword");

        given()
                .contentType(ContentType.JSON)
                .body(loginDto)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode())
                .body(containsString("Your account has been deactivated"));
    }

    private void createUser(String username, String email, String password) {
        createUser(username, email, password, "actif");
    }

    private void createUser(String username, String email, String password, String statut) {
        Utilisateur user = new Utilisateur();
        user.username = username;
        user.email = email;
        user.password = new UtilisateurService().hashPassword(password); // Assurez-vous de hasher le mot de passe
        user.role = "user";
        user.statut = statut;
        userRepository.persist(user);
    }
}

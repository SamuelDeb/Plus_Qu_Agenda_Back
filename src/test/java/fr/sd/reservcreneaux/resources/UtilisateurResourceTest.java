package fr.sd.reservcreneaux.resources;

import fr.sd.reservcreneaux.dto.ChangePasswordDTO;
import fr.sd.reservcreneaux.dto.UtilisateurDTO;
import fr.sd.reservcreneaux.entities.Creneaux;
import fr.sd.reservcreneaux.entities.Utilisateur;
import fr.sd.reservcreneaux.repositories.UtilisateurRepository;
import fr.sd.reservcreneaux.services.UtilisateurService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.annotations.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class UtilisateurResourceTest {

    @Inject
    UtilisateurRepository userRepository;

    @Inject
    UtilisateurService userService;

    @BeforeEach
    public void setup() {
        Mockito.reset(userRepository, userService);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"admin"})
    public void testGetAllUsers() {
        Utilisateur user = new Utilisateur();
        user.username = "testUser";
        user.email = "test@example.com";
        user.role = "user";
        user.statut = "actif";

        when(userRepository.listAll()).thenReturn(Collections.singletonList(user));

        given()
                .when().get("/users")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("$.size()", is(1))
                .body("[0].username", is("testUser"));
    }

    @Test
    public void testGetAllUsernames() {
        Utilisateur user = new Utilisateur();
        user.username = "testUser";

        when(userRepository.listAll()).thenReturn(Collections.singletonList(user));

        given()
                .when().get("/users/users/all")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("$.size()", is(1))
                .body("[0]", is("testUser"));
    }

//    @Test
//    @TestSecurity(user = "admin", roles = {"admin", "user"})
//    public void testFindUserByUsername() {
//        Utilisateur user = new Utilisateur();
//        user.username = "testUser";
//        user.email = "test@example.com";
//        user.role = "user";
//        user.statut = "actif";
//
//        when(userRepository.find("username", "testUser")).thenReturn(Mockito.mock(Query.class));
//
//        given()
//                .when().get("/users/search?username=testUser")
//                .then()
//                .statusCode(Response.Status.OK.getStatusCode())
//                .body("username", is("testUser"))
//                .body("email", is("test@example.com"));
//    }

//    @Test
//    @TestSecurity(user = "admin", roles = {"admin"})
//    @Transactional
//    public void testUpdateUserByUsername() {
//        Utilisateur user = new Utilisateur();
//        user.username = "testUser";
//        user.email = "test@example.com";
//        user.role = "user";
//        user.statut = "actif";
//
//        when(userRepository.find("username", "testUser")).thenReturn(Mockito.mock(Query.class));
//
//        UtilisateurDTO userDTO = new UtilisateurDTO();
//        userDTO.username = "testUser";
//        userDTO.email = "new@example.com";
//        userDTO.role = "admin";
//        userDTO.statut = "actif";
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(userDTO)
//                .when().put("/users/testUser")
//                .then()
//                .statusCode(Response.Status.OK.getStatusCode())
//                .body("email", is("new@example.com"))
//                .body("role", is("admin"));
//    }

    @Test
    @TestSecurity(user = "testUser", roles = {"user"})
    public void testChangePassword() {
        ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO();
        changePasswordDTO.setOldPassword("oldPassword");
        changePasswordDTO.setNewPassword("newPassword");

        doNothing().when(userService).changePassword("testUser", "oldPassword", "newPassword");

        given()
                .contentType(ContentType.JSON)
                .body(changePasswordDTO)
                .when().post("/users/change-password")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }
}
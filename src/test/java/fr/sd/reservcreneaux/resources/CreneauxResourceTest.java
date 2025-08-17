package fr.sd.reservcreneaux.resources;
import fr.sd.reservcreneaux.dto.CreneauxDTO;
import fr.sd.reservcreneaux.dto.NewCreneauDTO;
import fr.sd.reservcreneaux.entities.Creneaux;
import fr.sd.reservcreneaux.repositories.CreneauxRepository;
import fr.sd.reservcreneaux.services.CreneauxService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
@QuarkusTest
public class CreneauxResourceTest {

    @Inject
    CreneauxService creneauxService;

    @Inject
    CreneauxRepository creneauxRepository;

    @BeforeEach
    public void setup() {
        // Clean up database before each test
        Mockito.reset(creneauxRepository, creneauxService);
    }

    @Test
    @TestSecurity(user = "testuser")
    public void testCreateCreneau() {
        NewCreneauDTO newCreneauDTO = new NewCreneauDTO("testUser", "08:00", "10:00", "type1");

        given()
                .contentType(ContentType.JSON)
                .body(newCreneauDTO)
                .when()
                .post("/creneaux")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("creeParUsername", is("testUser"));
    }



    @Test
    @TestSecurity(user = "testUser")
    public void testListCreneauxByUsername() {
        CreneauxDTO creneauxDTO = new CreneauxDTO();
        creneauxDTO.setCreeParUsername("testUser");

        Mockito.when(creneauxService.listCreneauxByUsername("testUser"))
                .thenReturn(Collections.singletonList(creneauxDTO));

        given()
                .when()
                .get("/creneaux/user?username=testUser")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("$", hasSize(1))
                .body("[0].creeParUsername", is("testUser"));
    }

    @Test
    @TestSecurity(user = "testUser")
    public void testListAllCreneaux() {
        CreneauxDTO creneauxDTO = new CreneauxDTO();
        creneauxDTO.setCreeParUsername("testUser");

        Mockito.when(creneauxService.listAllCreneaux())
                .thenReturn(Collections.singletonList(creneauxDTO));

        given()
                .when()
                .get("/creneaux/allCreneaux")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("$", hasSize(1))
                .body("[0].creeParUsername", is("testUser"));
    }

    @Test
    @TestSecurity(user = "testUser")
    public void testReserveCreneau() {
        Mockito.doNothing().when(creneauxService).reserverCreneau(Mockito.anyString(), Mockito.anyString());

        given()
                .contentType(ContentType.JSON)
                .when()
                .post("/creneaux/reserve?creneauId=1&reserverParUsername=testUser")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(containsString("Créneau réservé avec succès"));
    }
}

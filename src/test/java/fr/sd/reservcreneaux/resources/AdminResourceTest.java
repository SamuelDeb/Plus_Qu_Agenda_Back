package fr.sd.reservcreneaux.resources;

import com.google.common.net.HttpHeaders;
import fr.sd.reservcreneaux.entities.Utilisateur;
import fr.sd.reservcreneaux.repositories.UtilisateurRepository;
import fr.sd.reservcreneaux.services.TestDataInitializer;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
class AdminResourceTest {

    @Inject
    UtilisateurRepository userRepository;

    @Inject
    TestDataInitializer testDataInitializer;

    @BeforeEach
    public void setup() {
        testDataInitializer.generateTestData();
    }

    @Test
    public void testGetAllUsers() {
        long userCount = userRepository.count();

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
                .when()
                .get("/admin/protected")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .body("$", hasSize((int) userCount));
    }

    @Test
    public void testActivateByUsername() {
        String username = "user1";

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
                .when()
                .put("/admin/activate/" + username)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("statut", is("actif"));

    }

    @Test
    public void testActivateByUsernameNotFound() {
        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
                .when()
                .put("/admin/activate/nonexistentuser")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testDisableUserByUsername() {
        String username = "user1";

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
                .when()
                .put("/admin/disable/" + username)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("statut", is("inactif"));

    }

    @Test
    public void testDisableUserByUsernameNotFound() {
        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
                .when()
                .put("/admin/disable/nonexistentuser")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testDeleteUserByEmail() {
        String username = "user1";

        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
                .when()
                .delete("/admin/" + username)
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body(is("User with username " + username + " deleted successfully."));
    }

    @Test
    public void testDeleteUserByEmailNotFound() {
        given()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getAdminToken())
                .when()
                .delete("/admin/nonexistentuser")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }





    private String getAdminToken() {
        // Implementation to get a valid admin JWT token
        return "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJqd3QtdG9rZW4iLCJzdWIiOiJzYW1kZWI1OSIsImdyb3VwcyI6WyJhZG1pbiJdLCJpYXQiOjE3MjE2Mzc0ODIsImV4cCI6MTcyMTY0MTA4MiwianRpIjoiMDZiYWY1MjMtM2RiZi00ZmVkLTkwYzgtMjk0MWFiZjIxOTk1In0.CF85a4vbYlDsgqTags0TVbH4JdP0Si1Smbly0hlu0-f4S3wrAqvzmbGKjw4DYFn3BRtfb4gNHyvYhsRkAJ4IEiVxUqpjv2RE2JXeI5oclORnGTdwL377_-9BQCkADLub8b8bDATv2bHpgdRC20Dp5RBkv-rT7VgVafqgvSwj8CGS43QV0alDfLKn24Sm26jQLTTe3g9Tt8h96T4EMYo1Hk5DRP8L4ioYfXs8buyFVgrOXl44Ao408iVg3TMqg6jsQNpS_fkrd5267DcvLAZtZcaWRZbEaixnI4pIPL3M1lvPBCZfXeth3SmY6CihZzEerzGrau_j3MlvE-VpH3runQ";
    }
}
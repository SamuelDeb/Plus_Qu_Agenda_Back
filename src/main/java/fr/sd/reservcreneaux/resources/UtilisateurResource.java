package fr.sd.reservcreneaux.resources;

import fr.sd.reservcreneaux.dto.ChangePasswordDTO;
import fr.sd.reservcreneaux.dto.UtilisateurDTO;
import fr.sd.reservcreneaux.entities.Creneaux;
import fr.sd.reservcreneaux.entities.Utilisateur;
import fr.sd.reservcreneaux.repositories.UtilisateurRepository;
import fr.sd.reservcreneaux.security.JwtToken;
import fr.sd.reservcreneaux.services.UtilisateurService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Utilisateur Resource", description = "Gestion des utilisateurs")
public class UtilisateurResource {

    @Inject
    UtilisateurRepository userRepository;
    @Inject
    UtilisateurService userService;
    @Inject
    JwtToken tokenService;
    @Context
    UriInfo uriInfo;

    @GET
//    @Path("/protected")
    @RolesAllowed({"admin", "user"})
    @Operation(summary = "Get all users", description = "Retrieves a list of all users in the system.")
    public Response getAllUsers() {
        List<Utilisateur> users = userRepository.listAll();
        List<UtilisateurDTO> userDTOs = users.stream().map(this::convertToDTO).collect(Collectors.toList());
        return Response.ok(userDTOs).build();
    }

    @GET
    @Path("/users/all")
    @Operation(summary = "Get all usersname", description = "Retrieves a list of all username in the system.")

    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAllUsernames() {
        return userRepository.listAll().stream()
                .map(Utilisateur::getUsername)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/search")
    @RolesAllowed({"admin", "user"})
    @Operation(summary = "Search for a user by username")
    public Response findUserByUsername(@Parameter(description = "Username of the user to be fetched") @QueryParam("username") String username) {
        Utilisateur user = userRepository.find("username", username).firstResult();
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
        }
        UtilisateurDTO userDTO = convertToDTO(user);
        return Response.ok(userDTO).build();
    }

    private UtilisateurDTO convertToDTO(Utilisateur user) {
        UtilisateurDTO dto = new UtilisateurDTO();
        dto.username = user.getUsername();
        dto.email = user.email;
        dto.password = user.password;
        dto.role = user.role;
        dto.statut = user.statut;
        return dto;
    }

    @Path("/{username}")
    @RolesAllowed({"admin", "user"})
    @Transactional
    @Operation(summary = "Update a user by username", description = "Updates a single user based on the username provided.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = UtilisateurDTO.class))),
            @APIResponse(responseCode = "404", description = "User not found")
    })
    public Response updateUserByUsername(
            @PathParam("username") String username,
            @RequestBody(description = "Updated user data", required = true, content = @Content(schema = @Schema(implementation = UtilisateurDTO.class))) UtilisateurDTO userDTO) {

        Utilisateur user = userRepository.find("username", username).firstResult();
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User with username " + username + " not found.").build();
        }

        user.email = userDTO.email;
        user.role = userDTO.role;
        user.statut = userDTO.statut;

        userRepository.persistOrUpdate(user);
        return Response.ok(user).build();
    }

    @GET
    @Path("/user/{username}")
    public List<Creneaux> getCreneauxByUser(@PathParam("username") String username) {
        return Creneaux.list("createdByUsername", username);
    }

@POST
@Path("/change-password")
@RolesAllowed({"user", "admin"})
@Operation(summary = "Change password", description = "Allows a user to change their password")
@APIResponses({
        @APIResponse(responseCode = "200", description = "Password changed successfully"),
        @APIResponse(responseCode = "400", description = "Invalid old password or other error"),
        @APIResponse(responseCode = "401", description = "Unauthorized")
})
public Response changePassword(ChangePasswordDTO changePasswordDTO, @Context SecurityContext securityContext) {
    String username = securityContext.getUserPrincipal().getName();

    // Ajoutez de logs pour vérifier les valeurs reçu

    System.out.println("Username from token: " + username);
    System.out.println("Old Password: " + changePasswordDTO.getOldPassword());
    System.out.println("New Password: " + changePasswordDTO.getNewPassword());

    try {
        userService.changePassword(username, changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        return Response.ok().build();
    } catch (IllegalArgumentException e) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("{\"error\":\"" + e.getMessage() + "\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"error\":\"" + e.getMessage() + "\"}")
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
}
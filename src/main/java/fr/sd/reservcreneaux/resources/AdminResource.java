package fr.sd.reservcreneaux.resources;

import fr.sd.reservcreneaux.dto.UtilisateurDTO;
import fr.sd.reservcreneaux.entities.Utilisateur;
import fr.sd.reservcreneaux.repositories.UtilisateurRepository;
import fr.sd.reservcreneaux.security.JwtToken;
import fr.sd.reservcreneaux.services.UtilisateurService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Resource pour les resources nécessaire aux admin
 */
@Path("admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Admin Resource", description = "Gestion des admin")
public class AdminResource {

    @Inject
    UtilisateurRepository userRepository;
    @Inject
    UtilisateurService userService;
    @Inject
    JwtToken tokenService;
    @Context
    UriInfo uriInfo;

    @GET
    @Path("/protected")
    @RolesAllowed("admin")
    @Operation(summary = "Get all users", description = "Retrieves a list of all users in the system.")
    public Response getAllUsers() {
        List<Utilisateur> users = userRepository.listAll();
        List<UtilisateurDTO> userDTOs = users.stream().map(this::convertToDTO).collect(Collectors.toList());
        return Response.ok(userDTOs).build();
    }

    @PUT
    @Path("/activate/{username}")
    @RolesAllowed({"admin"})
    @Operation(summary = "Activate a user by username", description = "Activates a single user based on the username provided.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = UtilisateurDTO.class))),
            @APIResponse(responseCode = "404", description = "User not found")
    })
    public Response activateByUsername(@PathParam("username") String username) {
        Utilisateur user = userRepository.find("username", username).firstResult();
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User with username " + username + " not found.").build();
        }
        user.statut = "actif";
        userRepository.persistOrUpdate(user);
        return Response.ok(user).build();
    }

    @PUT
    @Path("/disable/{username}")
    @RolesAllowed({"admin"})
    @Operation(summary = "Disable a user by username", description = "Disables a single user based on the username provided.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = UtilisateurDTO.class))),
            @APIResponse(responseCode = "404", description = "User not found")
    })
    public Response disableUserByUsername(@PathParam("username") String username) {
        Utilisateur user = userRepository.find("username", username).firstResult();
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("User with username " + username + " not found.").build();
        }
        user.statut = "inactif";
        userRepository.persistOrUpdate(user);
        return Response.ok(user).build();
    }

    @DELETE
    @Path("/delete/{username}")
    @RolesAllowed({"admin"})

    @Operation(summary = "Delete a user by username", description = "Deletes a single user based on the username provided.")
    @APIResponses({
            @APIResponse(responseCode = "200", description = "User deleted successfully"),
            @APIResponse(responseCode = "404", description = "User not found")
    })
    public Response deleteUserByEmail(@PathParam("username") @Parameter(description = "Username of the user to be deleted", required = true) String username) {
        Utilisateur user = userRepository.find("username", username).firstResult();
        if (user != null) {
            userRepository.delete(user);
            return Response.ok().entity("User with username " + username + " deleted successfully.").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("User with username " + username + " not found.").build();
        }
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
}

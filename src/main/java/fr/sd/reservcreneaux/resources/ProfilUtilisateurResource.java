package fr.sd.reservcreneaux.resources;

import fr.sd.reservcreneaux.entities.ProfilUtilisateur;
import fr.sd.reservcreneaux.entities.Utilisateur;
import fr.sd.reservcreneaux.repositories.UtilisateurRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

@Path("profiles")
@Tag(name = "Profil Resource", description = "Gestion des profils")

public class ProfilUtilisateurResource {

    @Inject
    UtilisateurRepository userRepository;


    @GET
    @Path("/{username}")
    @Operation(summary = "Get a user profile", description = "Retrieves the profile for a given user identified by username.")
    public Response getProfil(@PathParam("username") String username) {
        Utilisateur utilisateur = userRepository.findByUsername(username);
        if (utilisateur == null || utilisateur.getProfile() == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(utilisateur.getProfile()).build();
    }

    @PUT
    @Path("/update")
    @Operation(summary = "Update a user profile", description = "Updates the profile for a given user identified by username.")
    public Response CreateOrUpdateProfil(@QueryParam("username") String username, ProfilUtilisateur newProfil) {
        Utilisateur user = userRepository.find("username", username).firstResult();
        if (user != null) {
            if (user.getProfile() == null) {
                user.setProfile(newProfil);
            } else {
                ProfilUtilisateur currentProfile = user.getProfile();
                currentProfile.setNom(newProfil.getNom());
                currentProfile.setPrenom(newProfil.getPrenom());
                currentProfile.setAdresse(newProfil.getAdresse());
                currentProfile.setDescription(newProfil.getDescription());
            }
            userRepository.persistOrUpdate(user);
            return Response.ok().entity("Profil de l'utilisateur " + username + " mis à jour.").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Utilisateur avec username " + username + " introuvable.").build();
        }
    }
}

package fr.sd.reservcreneaux.resources;

import fr.sd.reservcreneaux.dto.CreneauxDTO;
import fr.sd.reservcreneaux.dto.NewCreneauDTO;
import fr.sd.reservcreneaux.entities.Creneaux;
import fr.sd.reservcreneaux.repositories.CreneauxRepository;
import fr.sd.reservcreneaux.services.CreneauxService;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/creneaux")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Créneaux Resource", description = "Gestion des créneaux")

public class CreneauxResource {

    @Inject
    SecurityIdentity securityIdentity;
    @Inject
    CreneauxRepository creneauxRepository;
    @Inject
    CreneauxService creneauxService;


    @POST
    public Response createCreneau(NewCreneauDTO creneauDTO) {
        Creneaux creneau = new Creneaux();
        creneau.creeParUsername = creneauDTO.creeParUsername;
        creneau.heureDebut = creneauDTO.heureDebut;
        creneau.heureFin = creneauDTO.heureFin;
        creneau.type = creneauDTO.type;
        creneauxRepository.persist(creneau);
        return Response.status(Response.Status.CREATED).entity(creneau).build();
    }


    @PUT
    @Path("/{creneauId}")
    @Operation(summary = "Modifier un créneau", description = "Modifier un créneau ")
    public Response updateCreneau(@PathParam("creneauId") String creneauId,
                                  @QueryParam("type") String type,
                                  @QueryParam("description") String description) {
        try {
            CreneauxDTO updatedCreneau = creneauxService.updateCreneau(creneauId, type, description);
            return Response.ok(updatedCreneau).build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }


    @GET
    @Path("/user")
    @Operation(summary = "Lister les créneaux d'un utilisateur", description = "Lister les créneaux d'un utilisateur à partir de son username.")
    public List<CreneauxDTO> listCreneauxByUsername(@QueryParam("username") String username) {
        return creneauxService.listCreneauxByUsername(username);
    }

    @GET
    @Path("/reserver/user")
    @Operation(summary = "Lister les créneaux réserver par un utilisateur", description = "Lister les créneaux réserver par un utilisateur à partir de son username.")
    public List<CreneauxDTO> listCreneauxReserverByUsername(@QueryParam("username") String username) {
        return creneauxService.listCreneauxReserverByUsername(username);
    }

    @POST
    @Path("/create")
    @Operation(summary = "Create a new creneau", description = "Creates a new creneau and saves it to the database.")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Creneau created successfully",
                    content = @Content(schema = @Schema(implementation = Creneaux.class))),
            @APIResponse(responseCode = "400", description = "Invalid creneau data provided")
    })
    public Response createCreneau(CreneauxDTO creneauDTO) {
        creneauxService.addCreneau(creneauDTO);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/allCreneaux")
    @Operation(summary = "Lister tout les créneaux disponible", description = "Lister tout les créneaux disponible")
    public List<CreneauxDTO> listAllCreneaux() {
        return creneauxService.listAllCreneaux();
    }

    @POST
    @Path("/generate")
    @Operation(summary = "Générer une liste de créneaux", description = "Génére une liste de créneaux")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Liste crée avec succes",
                    content = @Content(schema = @Schema(implementation = Creneaux.class))),
            @APIResponse(responseCode = "400", description = "Données saisie non valide")
    })
    public Response generateCreneaux(@QueryParam("username") String username,
                                     @QueryParam("dureeMinutes") int dureeMinutes,
                                     @QueryParam("nombreJours") int nombreJours,
                                     @QueryParam("heureDebut") String heureDebut,
                                     @QueryParam("heureFin") String heureFin,
                                     @QueryParam("type") String type,
                                     @QueryParam("description") String description) {
        try {
            List<CreneauxDTO> creneaux = creneauxService.generateCreneaux(username, dureeMinutes, nombreJours, heureDebut, heureFin, type, description);
            return Response.ok(creneaux).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/reserve")
    @Operation(summary = "Réserver un créneau", description = "Réserver un créneau à partie de son id")

    public Response reserverCreneau(@QueryParam("creneauId") String creneauId, @QueryParam("reserverParUsername") String reserverParUsername) {
        try {
            creneauxService.reserverCreneau(creneauId, reserverParUsername);
            return Response.ok().entity("Créneau réservé avec succès").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}

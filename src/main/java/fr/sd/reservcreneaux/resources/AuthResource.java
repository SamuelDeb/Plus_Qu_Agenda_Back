package fr.sd.reservcreneaux.resources;

import com.mongodb.MongoException;
import fr.sd.reservcreneaux.dto.*;
import fr.sd.reservcreneaux.entities.Utilisateur;
import fr.sd.reservcreneaux.repositories.UtilisateurRepository;
import fr.sd.reservcreneaux.security.JwtToken;
import fr.sd.reservcreneaux.services.MailService;
import fr.sd.reservcreneaux.services.UtilisateurService;
import fr.sd.reservcreneaux.util.CodeUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
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
import vn.loto.HateOas;

import java.net.URI;
import java.net.URISyntaxException;

@Path("auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Authentification Resource", description = "Gestion des authentification")
public class AuthResource {

    @Inject
    UtilisateurRepository userRepository;
    @Inject
    UtilisateurService userService;
    @Inject
    JwtToken tokenService;
    @Context
    UriInfo uriInfo;
    @Inject
    MailService mailService;

    @POST
    @Path("register")
    @PermitAll
    @Operation(summary = "Create a new user", description = "Creates a new user and saves it to the database.")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = Utilisateur.class))),
            @APIResponse(responseCode = "400", description = "Invalid user data provided")
    })
    public Response createUser(@Valid @Parameter(required = true) NewUtilisateurDTO userDTO) {
        if (userRepository.find("username", userDTO.getUsername()).count() > 0) {
            return Response.status(Response.Status.CONFLICT).entity("Username already in use").build();
        }
        if (!userService.isValidEmail(userDTO.getEmail())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Email invalide").build();
        }
        if (!userService.isValidPassword(userDTO.getPassword())) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Mot de passe invalide").build();
        }

        Utilisateur user = new Utilisateur();
        user.username = userDTO.getUsername();
        user.email = userDTO.getEmail();
        user.password = userService.hashPassword(userDTO.getPassword()); // Make sure to hash the password in real applications
        user.role = "user";
        user.statut = "actif";

        try {
            userRepository.persist(user);
            String subject = "Inscription valider";
            String body = "Votre inscription sur la plateforme de réservation est validée! ";

            mailService.sendEmail(user.email, subject, body);
            return Response.status(Response.Status.CREATED).entity(user).build();

        } catch (MongoException e) {
            if (e.getCode() == 11000) { // 11000 code pour une clé dupliquer sur mongodb
                return Response.status(Response.Status.CONFLICT).entity("Nom d'utilisateur déjà utiliser").build();
            }
            throw e;
        }
    }

    @POST
    @Path("/login")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Connexion Utilisateur", description = "Connexion utilisateur et envoi d'un code de vérification")
    public Response login(@Valid @RequestBody LoginDTO loginDto) throws URISyntaxException {
        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder();
        HateOas hateOas = new HateOas();

        if (loginDto.getUsername() == null || loginDto.getUsername().isEmpty() || loginDto.getPassword() == null || loginDto.getPassword().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Please enter login and password").type(MediaType.TEXT_PLAIN_TYPE).build();
        }

        Utilisateur user = userRepository.find("username", loginDto.getUsername()).firstResult();
        if (user == null) {
            hateOas.addLink("register", HttpMethod.POST, new URI(uriBuilder.clone().path("auth").path("register").build().toString()));
            hateOas.addLink("forgot login", HttpMethod.POST, new URI(uriBuilder.clone().path("users").path("enterYourEmail").path("forgot_login").build().toString()));
            return Response.ok(hateOas).status(Response.Status.NOT_FOUND).build();
        }

        if (user.getStatut().equals("inactif")) {
            return Response.status(Response.Status.FORBIDDEN).entity("Your account has been deactivated").type(MediaType.TEXT_PLAIN_TYPE).build();
        }
        if (!userService.checkPassword(loginDto.getPassword(), user.password)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid password").type(MediaType.TEXT_PLAIN_TYPE).build();
        }

        String randomCode = CodeUtil.generateRandomCode();
        user.setVerificationCode(randomCode);
        userRepository.persistOrUpdate(user);

        String subject = "Votre code de connexion";
        String body = "Votre code de connexion est : " + randomCode;

        mailService.sendEmail(user.email, subject, body);

        return Response.ok("Code de connexion envoyé à votre email").build();
    }

    @POST
    @Path("/validate-code")
    @Consumes(MediaType.APPLICATION_JSON)

    public Response validateCode(CodeValidationDTO codeValidationDto) {
        Utilisateur user = userRepository.find("username", codeValidationDto.getUsername()).firstResult();
        if (user == null || !user.getVerificationCode().equals(codeValidationDto.getCode())) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid code").build();
        }

        user.setVerificationCode(null);
        userRepository.persistOrUpdate(user);

        String token = JwtToken.generateToken(user.username, user.role);

        return Response.ok("{\"token\": \"" + token + "\"}").build();
    }

    @POST
    @Path("/send-reset-code")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response sendResetCode(ResetRequest resetRequest) {
        Utilisateur user = userService.findUserByUsername(resetRequest.getUsername());
        if (user != null) {
            String code = userService.generateResetCode();
            userService.saveVerificationCode(resetRequest.getUsername(), code);
            mailService.sendEmail(user.getEmail(), "Code de réinitialisation", "Votre code de réinitialisation est : " + code);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Utilisateur non trouvé").build();
        }
    }

    @POST
    @Path("/reset-password")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response resetPassword(ResetPasswordRequest resetPasswordRequest) {
        if (userService.isVerificationCodeValid(resetPasswordRequest.getUsername(), resetPasswordRequest.getResetCode())
                && resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            userService.updatePassword(resetPasswordRequest.getUsername(), resetPasswordRequest.getNewPassword());
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Code invalide ou les mots de passe ne correspondent pas").build();
        }
    }

}

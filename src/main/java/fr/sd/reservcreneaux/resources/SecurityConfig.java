package fr.sd.reservcreneaux.resources;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Set;

@ApplicationScoped
@Path("/secured")
public class SecurityConfig {

    @Inject
    SecurityIdentity securityIdentity;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public String getSecured() {
        return "This is a secured endpoint. Welcome " + securityIdentity.getPrincipal().getName();
    }

    @GET
    @Path("/roles")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Set<String> getRoles() {
        return securityIdentity.getRoles();
    }
}

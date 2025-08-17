package fr.sd.reservcreneaux.security;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JwtToken {

    @Inject
    JWTParser parser;

    public static String generateToken(String username, String role) {
        Set<String> roles = new HashSet<>();
        roles.add(role);

        JwtClaimsBuilder claimsBuilder = Jwt.claims()
                .upn(username)
                .groups(roles)
                .issuer("jwt-token");

        return claimsBuilder.sign();
    }

    public String getUsernameFromToken(String token) {
        try {
            JsonWebToken jwt = parser.parse(token);
            return jwt.getName(); // Assuming the username is stored in the "upn" claim
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse token", e);
        }
    }


}

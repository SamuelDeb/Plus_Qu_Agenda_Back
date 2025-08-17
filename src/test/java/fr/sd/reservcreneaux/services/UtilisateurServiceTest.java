package fr.sd.reservcreneaux.services;
import fr.sd.reservcreneaux.entities.Utilisateur;
import fr.sd.reservcreneaux.repositories.UtilisateurRepository;
import io.quarkus.mongodb.panache.PanacheQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UtilisateurServiceTest {

    @Mock
    private UtilisateurRepository userRepository;

    @InjectMocks
    private UtilisateurService utilisateurService;

    @BeforeEach
    public void setUp() {
        reset(userRepository);
    }

    @Test
    public void testIsValidEmail() {
        assertTrue(UtilisateurService.isValidEmail("test@example.com"));
        assertFalse(UtilisateurService.isValidEmail("invalid-email"));
    }

    @Test
    public void testIsValidPassword() {
        assertTrue(UtilisateurService.isValidPassword("strongpassword"));
        assertFalse(UtilisateurService.isValidPassword("short"));
    }

    @Test
    public void testHashPassword() {
        String plainPassword = "password";
        String hashedPassword = utilisateurService.hashPassword(plainPassword);

        assertNotNull(hashedPassword);
        assertTrue(BCrypt.checkpw(plainPassword, hashedPassword));
    }

    @Test
    public void testCheckPassword() {
        String plainPassword = "password";
        String hashedPassword = utilisateurService.hashPassword(plainPassword);

        assertTrue(utilisateurService.checkPassword(plainPassword, hashedPassword));
        assertFalse(utilisateurService.checkPassword("wrongpassword", hashedPassword));
    }

    @Test
    public void testGetRoleForLogin() {
        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");
        user.setRole("admin");

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        String role = utilisateurService.getRoleForLogin("testuser");
        assertEquals("admin", role);

        when(userRepository.findByUsername("unknownuser")).thenReturn(null);

        String defaultRole = utilisateurService.getRoleForLogin("unknownuser");
        assertEquals("user", defaultRole);
    }

    @Test
    public void testChangePassword() {
        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");
        user.setPassword(utilisateurService.hashPassword("oldpassword"));

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        utilisateurService.changePassword("testuser", "oldpassword", "newpassword");

        verify(userRepository, times(1)).update(any(Utilisateur.class));
        assertTrue(utilisateurService.checkPassword("newpassword", user.getPassword()));
    }

    @Test
    public void testChangePasswordWithInvalidOldPassword() {
        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");
        user.setPassword(utilisateurService.hashPassword("oldpassword"));

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        assertThrows(IllegalArgumentException.class, () -> {
            utilisateurService.changePassword("testuser", "wrongpassword", "newpassword");
        });
    }

    @Test
    public void testSaveVerificationCode() {
        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");

        when(userRepository.find("username", "testuser")).thenReturn(mockQuery(user));

        utilisateurService.saveVerificationCode("testuser", "123456");

        verify(userRepository, times(1)).persistOrUpdate(any(Utilisateur.class));
        assertEquals("123456", user.getVerificationCode());
    }

    @Test
    public void testIsVerificationCodeValid() {
        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");
        user.setVerificationCode("123456");

        when(userRepository.find("username", "testuser")).thenReturn(mockQuery(user));

        assertTrue(utilisateurService.isVerificationCodeValid("testuser", "123456"));
        assertFalse(utilisateurService.isVerificationCodeValid("testuser", "654321"));
    }

    @Test
    public void testUpdatePassword() {
        Utilisateur user = new Utilisateur();
        user.setUsername("testuser");

        when(userRepository.find("username", "testuser")).thenReturn(mockQuery(user));

        utilisateurService.updatePassword("testuser", "newpassword");

        verify(userRepository, times(1)).persistOrUpdate(any(Utilisateur.class));
        assertTrue(utilisateurService.checkPassword("newpassword", user.getPassword()));
    }

    private <T> PanacheQuery<T> mockQuery(T entity) {
        PanacheQuery<T> query = mock(PanacheQuery.class);
        when(query.firstResult()).thenReturn(entity);
        return query;
    }
}

package fr.sd.reservcreneaux.services;

import fr.sd.reservcreneaux.entities.Utilisateur;
import fr.sd.reservcreneaux.repositories.UtilisateurRepository;
import fr.sd.reservcreneaux.services.TestDataInitializer;
import fr.sd.reservcreneaux.services.UtilisateurService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@QuarkusTest
public class TestDataInitializerTest {

    @Inject
    TestDataInitializer testDataInitializer;

    @Inject
    UtilisateurRepository userRepository;

    @Inject
    UtilisateurService userService;

    @BeforeEach
    public void setup() {
        Mockito.reset(userRepository, userService);
    }

    @Test
    public void testGenerateTestData() {
        when(userRepository.find("username", "admin").firstResult()).thenReturn(null);
        when(userRepository.find("username", "user1").firstResult()).thenReturn(null);
        when(userRepository.find("username", "user2").firstResult()).thenReturn(null);
        when(userRepository.find("username", "inactiveuser").firstResult()).thenReturn(null);
        when(userRepository.find("username", "user3").firstResult()).thenReturn(null);

        testDataInitializer.generateTestData();

        verify(userRepository, times(5)).persist(any(Utilisateur.class));
    }

    @Test
    public void testGenerateTestDataAlreadyExists() {
        Utilisateur existingUser = new Utilisateur();
        existingUser.setUsername("admin");

        when(userRepository.find("username", "admin").firstResult()).thenReturn(existingUser);
        when(userRepository.find("username", "user1").firstResult()).thenReturn(existingUser);
        when(userRepository.find("username", "user2").firstResult()).thenReturn(existingUser);
        when(userRepository.find("username", "inactiveuser").firstResult()).thenReturn(existingUser);
        when(userRepository.find("username", "user3").firstResult()).thenReturn(existingUser);

        testDataInitializer.generateTestData();

        verify(userRepository, times(0)).persist(any(Utilisateur.class));
    }
}
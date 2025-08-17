package fr.sd.reservcreneaux.services;

import fr.sd.reservcreneaux.dto.CreneauxDTO;
import fr.sd.reservcreneaux.entities.Creneaux;
import fr.sd.reservcreneaux.entities.Utilisateur;
import fr.sd.reservcreneaux.repositories.CreneauxRepository;
import fr.sd.reservcreneaux.repositories.UtilisateurRepository;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CreneauxService {

    @Inject
    CreneauxRepository creneauxRepository;
    @Inject
    UtilisateurRepository utilisateurRepository;


    public void addCreneau(CreneauxDTO creneauxDTO) {
        Creneaux creneau = new Creneaux();
        creneau.date = creneauxDTO.getDate();
        creneau.heureDebut = creneauxDTO.getHeureDebut();
        creneau.heureFin = creneauxDTO.getHeureFin();
        creneau.type = creneauxDTO.getType();
        creneau.creeParUsername = creneauxDTO.getCreeParUsername();
        creneau.setDescription(creneauxDTO.getDescription());

        creneauxRepository.persist(creneau);
    }

    public List<CreneauxDTO> listAllCreneaux() {
        List<Creneaux> creneaux = creneauxRepository.listAll();
        return creneaux.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<CreneauxDTO> listCreneauxByUsername(String username) {
        List<Creneaux> creneaux = creneauxRepository.find("creeParUsername", username).list();
        return creneaux.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public List<CreneauxDTO> listCreneauxReserverByUsername(String username) {
        List<Creneaux> creneauxList = creneauxRepository.findByReserverParUsername(username);
        return creneauxList.stream().map(this::mapToDTO).toList();
    }


    public List<CreneauxDTO> generateCreneaux(String username, int dureeMinutes, int nombreJours,
                                              String heureDebut, String heureFin, String type, String description) {
        Utilisateur utilisateur = utilisateurRepository.find("username", username).firstResult();
        if (utilisateur == null) {
            throw new IllegalArgumentException("L'utilisateur n'existe pas");
        }
        List<CreneauxDTO> creneaux = new ArrayList<>();
        LocalTime startTime = LocalTime.parse(heureDebut);
        LocalTime endTime = LocalTime.parse(heureFin);
        LocalDate startDate = LocalDate.now();

        for (int i = 0; i < nombreJours; i++) {
            LocalDate currentDay = startDate.plusDays(i);
            LocalTime currentStartTime = startTime;
            while (currentStartTime.isBefore(endTime)) {
                CreneauxDTO dto = new CreneauxDTO();
                dto.setDate(currentDay);
                dto.setHeureDebut(currentDay.atTime(currentStartTime).toString());
                dto.setHeureFin(currentDay.atTime(currentStartTime.plusMinutes(dureeMinutes)).toString());
                dto.setType(type);
                dto.setCreeParUsername(username);
                dto.setDescription(description);
                creneaux.add(dto);

                // Enregistrer dans la base de données
                Creneaux creneau = new Creneaux();
                creneau.date = dto.getDate();
                creneau.heureDebut = dto.getHeureDebut();
                creneau.heureFin = dto.getHeureFin();
                creneau.type = dto.getType();
                creneau.creeParUsername = dto.getCreeParUsername();
                creneau.description = dto.getDescription();
                creneauxRepository.persist(creneau);

                currentStartTime = currentStartTime.plusMinutes(dureeMinutes);
            }
        }

        return creneaux;
    }

    public CreneauxDTO updateCreneau(String creneauId, String newType, String newDescription) {
        Creneaux creneau = creneauxRepository.findById(new ObjectId(creneauId));
        if (creneau == null) {
            throw new IllegalStateException("Créneau non trouvé avec l'ID fourni.");
        }

        creneau.setType(newType);
        creneau.setDescription(newDescription);
        creneauxRepository.persistOrUpdate(creneau);
        return mapToDTO(creneau);
    }

    public void reserverCreneau(String creneauId, String reserverParUsername) {
        Creneaux creneau = creneauxRepository.findById(new ObjectId(creneauId));
        if (creneau == null) {
            throw new IllegalArgumentException("Le créneau n'existe pas");
        }

        if (!creneau.statut.equals("disponible")) {
            throw new IllegalArgumentException("Le créneau est déjà réservé");
        }
        if (creneau.getDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Impossible de réserver un créneau dont la date est passée");
        }

        Utilisateur utilisateur = utilisateurRepository.find("username", reserverParUsername).firstResult();
        if (utilisateur == null) {
            throw new IllegalArgumentException("L'utilisateur réservant n'existe pas");
        }

        creneau.statut = "réservé";
        creneau.reserverParUsername = reserverParUsername;
        creneauxRepository.update(creneau);
    }

    /**
     * Vérifie régulièrement et met à jour le statut des créneaux dont la date est passée.
     */
    @Scheduled(every = "24h") // Exécute cette tâche une fois par jour
    public void updateCreneauStatus() {
        List<Creneaux> allCreneaux = creneauxRepository.listAll();
        for (Creneaux creneau : allCreneaux) {
            if (creneau.getDate().isBefore(LocalDate.now()) && !creneau.getStatut().equals("indisponible")) {
                creneau.setStatut("indisponible");
                creneauxRepository.persistOrUpdate(creneau);
            }
        }
    }

    private CreneauxDTO mapToDTO(Creneaux creneau) {
        CreneauxDTO dto = new CreneauxDTO();
        dto.setId(creneau.id.toString());
        dto.setDate(creneau.date);
        dto.setHeureDebut(creneau.heureDebut);
        dto.setHeureFin(creneau.heureFin);
        dto.setType(creneau.type);
        dto.creeParUsername(creneau.creeParUsername);
        dto.setReserverParUsername(creneau.reserverParUsername);
        dto.setStatut(creneau.statut);
        dto.setDescription(creneau.description);
        return dto;
    }
}

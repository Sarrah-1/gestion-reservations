package com.example;

import com.example.model.Equipement;
import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.Utilisateur;
import com.example.repository.SalleRepository;
import com.example.repository.SalleRepositoryImpl;
import com.example.service.SalleService;
import com.example.service.SalleServiceImpl;
import com.example.util.PaginationResult;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gestion-reservations");
        EntityManager em = emf.createEntityManager();

        try {
            SalleRepository salleRepository = new SalleRepositoryImpl(em);
            SalleService salleService = new SalleServiceImpl(em, salleRepository);

            initializeTestData(em);

            System.out.println("\nTest 1: Recherche de salles disponibles par créneau");
            testAvailableRooms(salleService);

            System.out.println("\nTest 2: Recherche multi-critères");
            testMultiCriteriaSearch(salleService);

            System.out.println("\nTest 3: Pagination");
            testPagination(salleService);

        } finally {
            em.close();
            emf.close();
        }
    }

    private static LocalDateTime at(int daysFromNow, int hour) {
        return LocalDateTime.now().plusDays(daysFromNow)
                .withHour(hour).withMinute(0).withSecond(0).withNano(0);
    }

    private static void initializeTestData(EntityManager em) {
        em.getTransaction().begin();

        Equipement projecteur = new Equipement("Projecteur", "Projecteur HD");
        Equipement ecran = new Equipement("Écran interactif", "Écran tactile 65 pouces");
        Equipement visioconference = new Equipement("Système de visioconférence", "Système complet avec caméra HD");

        em.persist(projecteur);
        em.persist(ecran);
        em.persist(visioconference);

        Utilisateur user1 = new Utilisateur("Dupont", "Jean", "jean.dupont@example.com");
        Utilisateur user2 = new Utilisateur("Martin", "Sophie", "sophie.martin@example.com");

        em.persist(user1);
        em.persist(user2);

        Salle salle1 = new Salle("Salle A1", 30);
        salle1.setDescription("Salle de réunion standard");
        salle1.setBatiment("Bâtiment A");
        salle1.setEtage(1);
        salle1.addEquipement(projecteur);

        Salle salle2 = new Salle("Salle B1", 15);
        salle2.setDescription("Petite salle de réunion");
        salle2.setBatiment("Bâtiment B");
        salle2.setEtage(2);
        salle2.addEquipement(ecran);

        Salle salle3 = new Salle("Salle C1", 50);
        salle3.setDescription("Grande salle de conférence");
        salle3.setBatiment("Bâtiment C");
        salle3.setEtage(3);
        salle3.addEquipement(projecteur);
        salle3.addEquipement(visioconference);

        Salle salle4 = new Salle("Salle A2", 20);
        salle4.setDescription("Salle de formation");
        salle4.setBatiment("Bâtiment A");
        salle4.setEtage(2);
        salle4.addEquipement(projecteur);
        salle4.addEquipement(ecran);

        Salle salle5 = new Salle("Salle B2", 40);
        salle5.setDescription("Salle polyvalente");
        salle5.setBatiment("Bâtiment B");
        salle5.setEtage(3);
        salle5.addEquipement(visioconference);

        em.persist(salle1);
        em.persist(salle2);
        em.persist(salle3);
        em.persist(salle4);
        em.persist(salle5);

        Reservation res1 = new Reservation(at(1, 9), at(1, 11), "Réunion dequipe");
        res1.setUtilisateur(user1);
        res1.setSalle(salle1);

        Reservation res2 = new Reservation(at(2, 14), at(2, 16), "Entretien");
        res2.setUtilisateur(user2);
        res2.setSalle(salle2);

        Reservation res3 = new Reservation(at(3, 10), at(3, 12), "Presentation client");
        res3.setUtilisateur(user1);
        res3.setSalle(salle3);

        em.persist(res1);
        em.persist(res2);
        em.persist(res3);

        em.getTransaction().commit();
        System.out.println("Données de test initialisés avec succès !");
    }

    private static void testAvailableRooms(SalleService salleService) {
        // Créneau 1: Demain de 9h à 11h (salle1 est réservée)
        LocalDateTime start1 = at(1, 9);
        LocalDateTime end1 = at(1, 11);

        System.out.println("Salles disponibles pour le créneau: " + start1 + " à " + end1);
        List<Salle> availableRooms1 = salleService.findAvailableRooms(start1, end1);

        if (availableRooms1.isEmpty()) {
            System.out.println("Aucune salle disponible pour ce créneau.");
        } else {
            for (Salle salle : availableRooms1) {
                System.out.println("- " + salle.getNom() + " (capacité: " + salle.getCapacite() + ")");
            }
        }

        LocalDateTime start2 = at(5, 14);
        LocalDateTime end2 = at(5, 16);

        System.out.println("\nSalles disponibles pour le créneau: " + start2 + " à " + end2);
        List<Salle> availableRooms2 = salleService.findAvailableRooms(start2, end2);

        if (availableRooms2.isEmpty()) {
            System.out.println("Aucune salle disponible pour ce créneau.");
        } else {
            for (Salle salle : availableRooms2) {
                System.out.println("- " + salle.getNom() + " (capacité: " + salle.getCapacite() + ")");
            }
        }
    }

    private static void testMultiCriteriaSearch(SalleService salleService) {
        Map<String, Object> criteria1 = new HashMap<>();
        criteria1.put("capaciteMin", 30);

        System.out.println("Recherche des salles avec capacité >= 30:");
        List<Salle> result1 = salleService.searchRooms(criteria1);

        for (Salle salle : result1) {
            System.out.println("- " + salle.getNom() + " (capacité: " + salle.getCapacite() + ")");
        }

        Map<String, Object> criteria2 = new HashMap<>();
        criteria2.put("batiment", "Bâtiment A");

        System.out.println("\nRecherche des salles dans le Batiment A:");
        List<Salle> result2 = salleService.searchRooms(criteria2);

        for (Salle salle : result2) {
            System.out.println("- " + salle.getNom() + " (batiment: " + salle.getBatiment() + ")");
        }

        Map<String, Object> criteria3 = new HashMap<>();
        criteria3.put("capaciteMin", 20);
        criteria3.put("capaciteMax", 40);
        criteria3.put("etage", 2);

        System.out.println("\nRecherche des salles avec capacité entre 20 et 40, à l'étage 2:");
        List<Salle> result3 = salleService.searchRooms(criteria3);

        for (Salle salle : result3) {
            System.out.println("- " + salle.getNom() + " (capacité: " + salle.getCapacite() +
                              ", étage: " + salle.getEtage() + ")");
        }
    }

    private static void testPagination(SalleService salleService) {
        int pageSize = 2;

        int totalPages = salleService.getTotalPages(pageSize);
        System.out.println("Nombre total de pages: " + totalPages);

        for (int page = 1; page <= totalPages; page++) {
            System.out.println("\nPage " + page + ":");

            List<Salle> sallesPage = salleService.getPaginatedRooms(page, pageSize);

            for (Salle salle : sallesPage) {
                System.out.println("- " + salle.getNom() + " (capacité: " + salle.getCapacite() +
                                  ", bâtiment: " + salle.getBatiment() + ")");
            }
        }

        long totalItems = salleService.getAllRooms().size();
        List<Salle> firstPageItems = salleService.getPaginatedRooms(1, pageSize);

        PaginationResult<Salle> paginationResult = new PaginationResult<>(
            firstPageItems, 1, pageSize, totalItems
        );

        System.out.println("\nInformations de pagination:");
        System.out.println("Page courante: " + paginationResult.getCurrentPage());
        System.out.println("Taille de la page: " + paginationResult.getPageSize());
        System.out.println("Nombre total de pages: " + paginationResult.getTotalPages());
        System.out.println("Nombre total d'éléments: " + paginationResult.getTotalItems());
        System.out.println("Page suivante disponible: " + paginationResult.hasNext());
        System.out.println("Page précédente disponible: " + paginationResult.hasPrevious());
    }
}

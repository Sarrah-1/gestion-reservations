# TP 5 – Gestion de réservations avancée

**Auteur :** [Nom Prénom]

Requêtes avancées sur les salles avec JPA / Hibernate 5.6.5 et H2 (base en mémoire) :

1. **Salles disponibles par créneau** (JPQL avec sous-requête `NOT IN`)
2. **Recherche multi-critères** (API Criteria : nom, capacité min/max, bâtiment, étage, équipement)
3. **Pagination** (`setFirstResult` / `setMaxResults` + classe `PaginationResult`)

## Technologies

Java 8+, Maven, Hibernate 5.6.5.Final (JPA 2.2), Hibernate Validator 6.2.0, H2 2.1.214, SLF4J 1.7.36, JUnit 4.13.2.

## Structure

```
src/main/java/com/example
├── App.java                      # Classe principale (jeu de données + 3 tests)
├── model/                        # Utilisateur, Salle, Reservation, Equipement
├── repository/                   # SalleRepository + SalleRepositoryImpl
├── service/                      # SalleService + SalleServiceImpl
└── util/PaginationResult.java    # Résultat paginé
src/main/resources/META-INF/persistence.xml
```

## Lancer le projet

**IntelliJ IDEA :** File > Open > dossier contenant `pom.xml` > attendre l'import Maven > clic droit sur `App.java` > *Run 'App.main()'*.

**Ligne de commande :**

```
mvn clean compile exec:java
```

## Captures d'écran à réaliser

Dossier `screenshots/` (à créer) :

| Fichier | Contenu |
|---|---|
| `01-structure-projet.png` | Arborescence du projet dans IntelliJ |
| `02-pom-dependances.png` | Le fichier `pom.xml` |
| `03-persistence-xml.png` | Le fichier `persistence.xml` |
| `04-donnees-test.png` | Console : « Données de test initialisées avec succès ! » |
| `05-salles-disponibles.png` | Console : Test 1, salles disponibles par créneau |
| `06-recherche-multicriteres.png` | Console : Test 2, recherche multi-critères |
| `07-pagination.png` | Console : Test 3, pagination et informations de pagination |
| `08-sql-hibernate.png` | Requêtes SQL générées par Hibernate (NOT IN, LIMIT/OFFSET) |

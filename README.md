# Plus qu'Agenda Back

Application de gestion de réservation de créneaux, développée avec **Quarkus** et MongoDB, exposant une API REST complète pour l'interaction avec un frontend (ex : application web ou mobile).

## Points forts

- **API RESTful** : Toutes les fonctionnalités (authentification, gestion des utilisateurs, gestion des créneaux, profils) sont accessibles via des endpoints REST documentés.
- **Sécurité** : Authentification JWT, gestion des rôles (`user`, `admin`), endpoints protégés.
- **Interaction Frontend** : Pensé pour être consommé par un frontend moderne (React, Angular, Vue, etc.), avec gestion CORS activée.
- **Documentation OpenAPI/Swagger** : Accès à la documentation interactive des endpoints via `/q/swagger-ui`.

## Fonctionnalités principales exposées par l'API

- **Authentification & Sécurité**
  - Inscription, connexion, validation par code envoyé par email
  - Réinitialisation de mot de passe
  - Génération et validation de JWT

- **Gestion des utilisateurs**
  - CRUD utilisateurs (création, activation/désactivation, suppression)
  - Modification du mot de passe
  - Recherche et listing

- **Gestion des créneaux**
  - Création, modification, suppression de créneaux
  - Réservation et annulation
  - Génération automatique de créneaux sur plusieurs jours

- **Gestion des profils**
  - Création et mise à jour du profil utilisateur

## Exemple d'interaction avec le Frontend

Le frontend peut consommer les endpoints suivants :

- `POST /auth/register` : Inscription d'un utilisateur
- `POST /auth/login` : Connexion, envoi d'un code de vérification par email
- `POST /auth/validate-code` : Validation du code, récupération du JWT
- `GET /users` : Liste des utilisateurs (admin)
- `PUT /admin/activate/{username}` : Activation d'un utilisateur (admin)
- `POST /creneaux/create` : Création d'un créneau
- `POST /creneaux/reserve` : Réservation d'un créneau

Toutes les réponses sont au format JSON, facilitant l'intégration avec le frontend.

## Configuration CORS

Pour permettre l'accès depuis le frontend (ex : localhost:8081) :

```properties
quarkus.http.cors=true
quarkus.http.cors.origins=http://localhost:8081
quarkus.http.cors.methods=GET,POST,PUT,DELETE
```


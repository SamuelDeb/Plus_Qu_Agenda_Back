# Plus qu'Agenda

Ce projet est une application de gestion de réservation de créneaux, développée avec [Quarkus](https://quarkus.io/), MongoDB et REST.

## Fonctionnalités

- Gestion des utilisateurs (création, activation, désactivation, suppression)
- Authentification avec génération de code de vérification
- Réinitialisation de mot de passe par email
- Gestion des créneaux (création, réservation, modification, génération automatique)
- Gestion des profils utilisateurs
- API REST sécurisée (rôles `user` et `admin`)

## Prérequis

- Java 21+
- Maven 3.8+
- MongoDB

## Démarrage en mode développement

```sh
./mvnw compile quarkus:dev
```

L’interface Dev UI est disponible sur [http://localhost:8080/q/dev/](http://localhost:8080/q/dev/).

## Packaging et exécution

Pour packager l’application :

```sh
./mvnw package
```

L’application packagée se trouve dans `target/quarkus-app/` et peut être lancée avec :

```sh
java -jar target/quarkus-app/quarkus-run.jar
```

Pour générer un _über-jar_ :

```sh
./mvnw package -Dquarkus.package.jar.type=uber-jar
java -jar target/*-runner.jar
```

## Exécutable natif

Pour compiler en natif :

```sh
./mvnw package -Dnative
```

Ou via un conteneur :

```sh
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

## Endpoints principaux

- `/auth/register` : inscription utilisateur
- `/auth/login` : connexion utilisateur
- `/auth/send-reset-code` : envoi d’un code de réinitialisation
- `/auth/reset-password` : réinitialisation du mot de passe
- `/users` : gestion des utilisateurs (GET, PUT, etc.)
- `/creneaux` : gestion des créneaux
- `/admin` : endpoints réservés aux administrateurs

## Tests

Lancer les tests :

```sh
./mvnw test
```

## Guides Quarkus utiles

- [MongoDB avec Panache](https://quarkus.io/guides/mongodb-panache)
- [RESTEasy Classic](https://quarkus.io/guides/resteasy)

---

**Auteur** : Projet CDA – 2024

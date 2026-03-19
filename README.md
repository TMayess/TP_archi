# TP Architecture Microservices — Plateforme de Réservation de Coworking

## Description

Plateforme de réservation de salles de coworking développée en architecture
microservices avec Spring Boot et Spring Cloud.

## Architecture
```
API Gateway (8090)
│
├── Room Service (8081)
├── Member Service (8082)
└── Reservation Service (8083)

Infrastructure :
├── Config Server (8888)
├── Discovery Server / Eureka (8761)
└── Apache Kafka (9092)
```

## Prérequis

- Java 17 ou 21
- Maven
- Docker + Docker Compose

## Repositories

| Repository | URL |
|---|---|
| Code source | https://github.com/TMayess/TP_archi.git |
| Configuration centralisée | https://github.com/TMayess/config_TP_archi.git |

## Lancement

### 1. Cloner le projet
```bash
git clone https://github.com/TMayess/TP_archi.git
cd TP_archi
```

### 2. Démarrer Kafka avec Docker
```bash
docker-compose up -d
```

### 3. Démarrer les services dans l'ordre
```bash
# 1. Config Server
cd config-server
mvn spring-boot:run

# 2. Discovery Server
cd discovery-server
mvn spring-boot:run

# 3. Room Service
cd room-service
mvn spring-boot:run

# 4. Member Service
cd member-service
mvn spring-boot:run

# 5. Reservation Service
cd reservation-service
mvn spring-boot:run

# 6. API Gateway
cd api-gateway
mvn spring-boot:run
```

## URLs utiles

| Service | URL |
|---|---|
| Eureka Dashboard | http://localhost:8761 |
| API Gateway | http://localhost:8090 |
| Swagger Room Service | http://localhost:8081/swagger-ui/index.html |
| Swagger Member Service | http://localhost:8082/swagger-ui/index.html |
| Swagger Reservation Service | http://localhost:8083/swagger-ui/index.html |

## Endpoints principaux

### Rooms
| Méthode | URL | Description |
|---|---|---|
| GET | /rooms | Lister toutes les salles |
| GET | /rooms/{id} | Obtenir une salle |
| GET | /rooms/available | Lister les salles disponibles |
| GET | /rooms/city/{city} | Lister les salles par ville |
| POST | /rooms | Créer une salle |
| PUT | /rooms/{id} | Modifier une salle |
| DELETE | /rooms/{id} | Supprimer une salle |

### Members
| Méthode | URL | Description |
|---|---|---|
| GET | /members | Lister tous les membres |
| GET | /members/{id} | Obtenir un membre |
| POST | /members | Créer un membre |
| PUT | /members/{id} | Modifier un membre |
| DELETE | /members/{id} | Supprimer un membre |

### Reservations
| Méthode | URL | Description |
|---|---|---|
| GET | /reservations | Lister toutes les réservations |
| GET | /reservations/{id} | Obtenir une réservation |
| POST | /reservations | Créer une réservation |
| PATCH | /reservations/{id}/cancel | Annuler une réservation |
| PATCH | /reservations/{id}/complete | Compléter une réservation |

## Topics Kafka

| Topic | Producteur | Consommateur | Description |
|---|---|---|---|
| room-deleted | Room Service | Reservation Service | Annule les réservations CONFIRMED |
| member-deleted | Member Service | Reservation Service | Supprime les réservations du membre |
| member-suspend | Reservation Service | Member Service | Suspend le membre si quota atteint |
| member-unsuspend | Reservation Service | Member Service | Désuspend le membre si quota libéré |

## Design Pattern

Le **State Pattern** a été mis en place dans le Reservation Service.
Voir [DESIGN_PATTERN.md](./DESIGN_PATTERN.md) pour la justification complète.

## Technologies

- Java 21
- Spring Boot 3.4
- Spring Cloud (Config, Eureka, Gateway)
- Apache Kafka
- H2 Database
- Lombok
- Swagger / springdoc-openapi
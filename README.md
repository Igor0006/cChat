# cChat

A minimal messenger application that supports direct (1-to-1) messages and group chats.  
The system is split into two Spring Boot microservices connected via Apache Kafka:
- **Ingress (Auth & Send) Service** – handles user authentication/authorization and accepts outgoing messages from clients; publishes them to Kafka.
- **Receive Service** – consumes messages from Kafka, persists them, and exposes read/subscribe APIs (REST / WebSocket/STOMP) to clients.

> This project is intentionally small and pragmatic: it focuses on clear separation of concerns, message-broker decoupling, and a working end-to-end flow.
---

## Architecture
```
+---------------+           REST                       +-----------------+
|   Web / CLI   |  --------------------------------->  |  Ingress Service|
|   Client      |   (Auth, Send Message)               | (Auth & Send)   |
+-------+-------+                                      +-----------------+
        |                                                   |
        |                                                 Kafka
        |                                                   v
        |                                         +---------+-----------+
	      | -----------------REST-------------->    |   Receive Service   |
		    |           (Group management)            |                     |
        |  <----------- REST / WS/STOMP ----------| (Persist & Deliver) |
        |         (Fetch history, subscribe)      +---------+-----------+
        |                                                   |
        |                                         +---------v-----------+
        |                                         |   PostgreSQL        |
        |                                         +---------------------+
```
## **Tech Stack**
- **Java / Spring Boot**
    - Spring Web, Spring Security, Spring Messaging (STOMP over WebSocket), Spring Data JPA
    - Spring for Apache Kafka
- **Apache Kafka** (broker + topic for messages)
- **PostgreSQL** 
- **Maven**
- **Docker / Docker Compose**

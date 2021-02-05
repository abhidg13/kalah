
# Kalah Game

This is a *Java RESTful Web Service*  that runs a game of 6-stone Kalah. There is no AI implemented.

## Game Rules

- Game Play:
The player who begins with the first move picks up all the stones in any of his own six pits, and sows the stones on to the right, one in each of the following pits, including his own big pit. No stones are put in the opponents' big pit. If the
player's last stone lands in his own big pit, he gets another turn. This can be repeated several times before it's the other player's turn.

- Capturing Stones:
During the game the pits are emptied on both sides. Always when the last stone lands in an own empty pit, the player captures his own stone and all stones in the
opposite pit (the other player’s pit) and puts them in his own (big or little) pit.

- Game Ends:
The game is over as soon as one of the sides runs out of stones. The player who still has stones in his pits keeps them and puts them in his big pit. The winner of
the game is the player who has the most stones in his big pit.

## Getting Started

### Repo

* https://github.com/abhidg13/kalah

### Prerequisites

* Java 1.8 - Programming language

### Running

```
./mvnw spring-boot:run
```

API endpoint documentation: <http://localhost:8080/swagger-ui.html>

### Running the tests

```
./mvnw test
```

## Built With

* [Spring Boot](https://projects.spring.io/spring-boot/) - The framework used
* [Maven](https://maven.apache.org) - Dependency management
* [JUnit](https://junit.org) - Test framework
* [Swagger](https://swagger.io) - Used to generate API docs & UI

## Author

* [Abhishek Dasgupta](https://github.com/abhidg13)

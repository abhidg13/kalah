package game.kalah.service;

import game.kalah.model.Game;

/**
 * Service interface for Game related user actions.
 */
public interface GameService {

    /**
     * Starts a new game. Initializes the game board..
     */
    Game createGame();

    /**
     * Player pick a pit from their board and take all stones in that pit.
     */
    Game play(String gameId, Integer pitId);

}

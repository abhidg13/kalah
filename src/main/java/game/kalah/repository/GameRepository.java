package game.kalah.repository;

import game.kalah.model.Game;

public interface GameRepository {

    Game find(final String id);

    Game save(final Game game);

}

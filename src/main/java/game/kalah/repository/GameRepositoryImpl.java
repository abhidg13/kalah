package game.kalah.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import game.kalah.model.Game;
import game.kalah.exception.GameNotFoundException;

@Service
public class GameRepositoryImpl implements GameRepository {

    private final Map<String, Game> repository = new HashMap<>();

    @Override
    public Game find(final String id) {
        return this.repository.get(id);
    }

    @Override
    public Game save(final Game game) {
        this.repository.put(game.getId(), game);
        return this.find(game.getId());
    }

}

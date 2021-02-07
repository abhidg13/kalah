package game.kalah.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -2576221153177453295L;
    private static final Logger LOGGER = Logger.getLogger(GameNotFoundException.class.getName());

    public GameNotFoundException(final String id) {
        super("Could not find game " + id);
        LOGGER.log(Level.WARNING, "Game not found. Id - " + id);
    }
}

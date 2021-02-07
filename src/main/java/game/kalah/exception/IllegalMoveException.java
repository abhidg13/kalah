package game.kalah.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

public class IllegalMoveException extends RuntimeException {

    private static final long serialVersionUID = -3407336632879524317L;
    private static final Logger LOGGER = Logger.getLogger(IllegalMoveException.class.getName());

    public IllegalMoveException(final String message, int startPitId) {
        super("Illegal move: " + message);
        LOGGER.log(Level.WARNING, "Illegal Move - " + message + ", Pit Id - " + startPitId);
    }
}

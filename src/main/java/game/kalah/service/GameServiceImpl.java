package game.kalah.service;

import game.kalah.exception.GameNotFoundException;
import game.kalah.model.Board;
import game.kalah.model.Game;
import game.kalah.model.Pit;
import game.kalah.model.Player;
import game.kalah.exception.IllegalMoveException;
import game.kalah.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class GameServiceImpl implements GameService {

    private static final Logger LOGGER = Logger.getLogger(GameServiceImpl.class.getName());

    private final GameRepository repository;

    @Autowired
    public GameServiceImpl(final GameRepository repository) {
        this.repository = repository;
    }

    @Override
    public Game createGame() {
        Game game = this.repository.save(new Game());
        LOGGER.log(Level.INFO, "New game created. Id - {0}", game.getId());
        return game;
    }

    @Override
    public Game play(final String gameId, final Integer pitId) {
        final var game = this.repository.find(gameId);
        if (game == null) {
            throw new GameNotFoundException(gameId);
        }
        LOGGER.log(Level.INFO, "Game Id found - {0}", gameId);
        distributeStones(game, pitId);
        decidePlayerTurn(game, pitId);
        checkGameOver(game);
        return game;
    }

    private void resetBoard(final Game game) {
        game.getBoard().getPits().parallelStream()
                .filter(pit -> (Player.PLAYER_NORTH.getHouseIndex() != pit.getId())
                               && (Player.PLAYER_SOUTH.getHouseIndex() != pit.getId()))
                .forEach(pit -> pit.setStoneCount(0));
    }

    private void distributeStones(final Game game, int pitId) {
        final Pit startPit = game.getBoard().getPit(pitId);
        validateMove(game, pitId);
        LOGGER.log(Level.INFO, "Play pit id - {0}", pitId);
        int stoneToDistribute = startPit.getStoneCount();
        startPit.setStoneCount(0);
        while (stoneToDistribute > 0) {
            final Pit currentPit = game.getBoard().getPit(++pitId);
            if (currentPit.isDistributable(game.getTurn())) {
                currentPit.setStoneCount(currentPit.getStoneCount() + 1);
                stoneToDistribute--;
            }
        }
        lastEmptyPit(game, pitId);
        LOGGER.log(Level.INFO, "Board status - {0}", game.getBoard().getPits());
    }

    private void checkGameOver(final Game game) {
        final int playerNorthPitStoneCount = game.getBoard().getStoneCount(Player.PLAYER_NORTH, false);
        final int playerSouthPitStoneCount = game.getBoard().getStoneCount(Player.PLAYER_SOUTH, false);
        if ((playerNorthPitStoneCount == 0) || (playerSouthPitStoneCount == 0)) {
            final Pit houseNorth = game.getBoard().getPit(Player.PLAYER_NORTH.getHouseIndex());
            final Pit houseSouth = game.getBoard().getPit(Player.PLAYER_SOUTH.getHouseIndex());
            houseNorth.setStoneCount(houseNorth.getStoneCount() + playerNorthPitStoneCount);
            houseSouth.setStoneCount(houseSouth.getStoneCount() + playerSouthPitStoneCount);
            determineWinner(game);
            resetBoard(game);
            LOGGER.log(Level.INFO, "Game over!");
        }
    }

    private void determineWinner(final Game game) {
        final int houseNorthStoneCount = game.getBoard().getStoneCount(Player.PLAYER_NORTH, true);
        final int houseSouthStoneCount = game.getBoard().getStoneCount(Player.PLAYER_SOUTH, true);
        if (houseNorthStoneCount > houseSouthStoneCount) {
            game.setWinner(Player.PLAYER_NORTH);
        } else if (houseNorthStoneCount < houseSouthStoneCount) {
            game.setWinner(Player.PLAYER_SOUTH);
        }
        LOGGER.log(Level.INFO, "Game winner - {0}", game.getWinner());
    }

    private void lastEmptyPit(final Game game, final int endPitId) {
        final Pit endPit = game.getBoard().getPit(endPitId);
        if (!endPit.isHouse() && endPit.getOwner().equals(game.getTurn()) && (endPit.getStoneCount() == 1)) {
            final Pit oppositePit = game.getBoard().getPit(Board.PIT_END_INDEX - endPit.getId());
            if (oppositePit.getStoneCount() > 0) {
                final Pit house = game.getBoard().getPit(endPit.getOwner().getHouseIndex());
                house.setStoneCount(
                        (house.getStoneCount() + oppositePit.getStoneCount()) + endPit.getStoneCount());
                oppositePit.setStoneCount(0);
                endPit.setStoneCount(0);
            }
        }
    }

    private void decidePlayerTurn(final Game game, final int pitId) {
        final Pit pit = game.getBoard().getPit(pitId);
        if (pit.isHouse() && Player.PLAYER_NORTH.equals(pit.getOwner())
            && Player.PLAYER_NORTH.equals(game.getTurn())) {
            game.setTurn(Player.PLAYER_NORTH);
        } else if (pit.isHouse() && Player.PLAYER_SOUTH.equals(pit.getOwner())
                   && Player.PLAYER_SOUTH.equals(game.getTurn())) {
            game.setTurn(Player.PLAYER_SOUTH);
        } else if (Player.PLAYER_NORTH.equals(game.getTurn())) {
            game.setTurn(Player.PLAYER_SOUTH);
        } else {
            game.setTurn(Player.PLAYER_NORTH);
        }
        LOGGER.log(Level.INFO, "Player turn - {0}", game.getTurn());
    }

    private void validateMove(final Game game, final int startPitId) {
        final Pit startPit = game.getBoard().getPit(startPitId);
        if (startPit.isHouse()) {
            throw new IllegalMoveException("Cannot start from house!", startPitId);
        }
        if (Player.PLAYER_NORTH.equals(game.getTurn())
            && !Player.PLAYER_NORTH.equals(startPit.getOwner())) {
            throw new IllegalMoveException("It's Player North's turn!", startPitId);
        }
        if (Player.PLAYER_SOUTH.equals(game.getTurn())
            && !Player.PLAYER_SOUTH.equals(startPit.getOwner())) {
            throw new IllegalMoveException("It's Player South's turn!", startPitId);
        }
        if (startPit.getStoneCount() == 0) {
            throw new IllegalMoveException("Cannot start from empty pit!", startPitId);
        }
        if (game.getTurn() == null) {
            game.setTurn(Player.PLAYER_NORTH.equals(startPit.getOwner()) ? Player.PLAYER_NORTH : Player.PLAYER_SOUTH);
        }
    }
}

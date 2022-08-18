package com.example.myproject1.service;

import com.example.myproject1.model.*;
import com.example.myproject1.repository.GameSessionRepository;
import com.example.myproject1.repository.GuessRepository;
import com.example.myproject1.repository.PlayerRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class GameService {
    private static GameService service;
    private final PlayerRepository playerRepository;
    private final GameSessionRepository gameSessionRepository;
    private final GuessRepository guessRepository;
    private static final int NUM_OF_DISPLAYED_PAGES = 5;
    private static final int NUM_OF_DISPLAYED_RECS = 10;
    private int startId;
    public static GameService getService() {
        if (service == null) {
            service = new GameService();
        }
        return service;
    }
    public GameService() {
        playerRepository = new PlayerRepository();
        gameSessionRepository = new GameSessionRepository();
        guessRepository = new GuessRepository();
        startId = gameSessionRepository.getStartId();
    }

    /**
     * The method registers for a new player.
     * @return a Player model if succeeded, otherwise null.
     */
    public Player registerPlayer(String username, String password, String name) {
        if (!playerRepository.doesPlayerExist(username) && checkValidUser(username, password, name)){
            Player newPlayer = new Player(username, password, name);
            playerRepository.savePlayer(newPlayer);
            return newPlayer;
        }
        return null;
    }

    /**
     * The method is a validation for input information
     * @return TRUE if valid, otherwise FALSE
     */
    private boolean checkValidUser(String username, String password, String name) {
        if (username==null || "".equals(username.strip())) {
            return false;
        }
        if (password==null || "".equals(password.strip())) {
            return false;
        }
        if (name==null || "".equals(password.strip())) {
            return false;
        }
        return isValidCharacter(username) && isValidCharacter(password);//if pass all the tests above
    }

    /**
     * The method check whether the input String contains any special characters
     * @return TRUE if no special characters found, otherwise FALSE
     */
    public boolean isValidCharacter(String input) { // \\w -> [a-zA-Z0-9]
        return Pattern.matches("\\w+", input); // "+" means 1 or more occurrences of valid characters
    }

    /**
     *
     * The method check whether the input String contains only numbers
     * @return TRUE if only numbers, otherwise FALSE
     */
    public boolean areAllNumbers(String input){
        return Pattern.matches("\\d+", input);
    }

    /**
     * The method will pass username and password to playerRepository
     * @return a Player Model
     */
    public Player checkLogin(String username, String password) {
        return playerRepository.checkLogin(username, password);
    }

    /**
     * The method will get the active Game Session of the player. If no active games found, create a new game
     * @return a GameSession Model (can't be null)
     */
    public GameSession getGameSession(String username) { // get old gameSession, if there's no gameSession, create one
        GameSession oldGameSession = gameSessionRepository.getGameSession(username);
        if (oldGameSession == null) { //null -> create a gameSession -> save -> return
            GameSession newGameSession = new GameSession(username, ++startId);
            gameSessionRepository.saveGameSession(newGameSession);
            return newGameSession;
        } else if (oldGameSession.isCompleted()) { //if old game is active but already completed
            oldGameSession.setActive(false);
            updateGameSession(oldGameSession);
            GameSession newGameSession = new GameSession(username, ++startId);
            gameSessionRepository.saveGameSession(newGameSession);
            return newGameSession;
        }
        return oldGameSession;
    }

    /**
     * The method sets endTime for the gameSession and pass the gameSession model to repository to process.
     */
    public void updateGameSession(GameSession gameSession){
        gameSession.setEndTime(LocalDateTime.now()); //update endTime everytime player plays
        gameSessionRepository.updateGameSession(gameSession);
    }

    /**
     * The method create a new Guess Model and pass to repository to process
     */
    public void saveGuess(GameSession gameSession, int guessNumber) {
        String result = checkResult(guessNumber, gameSession.getTargetNumber());
        Guess newGuess = new Guess(gameSession.getId(), guessNumber, result);
        guessRepository.saveGuess(newGuess);
    }

    /**
     * The method return a list of Guess Models by passing gameSession model to repository
     * @return a list of Guess Models
     */
    public List<Guess> getGuessList(GameSession gameSession) {
        List<Guess> guessList = guessRepository.getGuessList(gameSession.getId());
        Collections.reverse(guessList);
        return guessList;
    }

    private String checkResult(int guessNumber, int targetNum) {
        if (guessNumber == targetNum) {
            return Result.BINGO;
        }
        return guessNumber > targetNum ? Result.GREATER_THAN : Result.LESS_THAN;
    }

    /**
     * The method set the completed attribute of gameSession TRUE, then update the gameSession.
     */
    public void finishGame(GameSession gameSession) {
        gameSession.setCompleted(true);
        updateGameSession(gameSession);
    }

    /**
     * The method deactivate all other games and create a new game for player
     */
    public void createNewGame(String username) {
        gameSessionRepository.deactivateCurrentGame(username);
        GameSession newGameSession = new GameSession(username, ++startId);
        gameSessionRepository.saveGameSession(newGameSession);
    }

    /**
     * The method returns a list of Rank Models by calling the repository
     * @return a list of Rank Models
     */
    public List<Rank> getRankList() {
        return gameSessionRepository.getRankList();
    }

    /**
     * The method updates the Player by calling the repository
     */
    public void updatePlayer (Player player) {
        playerRepository.updatePlayer(player);
    }

    /**
     * The method returns a list of History Models by passing username to repository
     */
    public List<History> getPlayerHistory(String username, int currentPage) {
        return gameSessionRepository.getHistory(username, currentPage, NUM_OF_DISPLAYED_RECS);
    }

    /**
     * The method returns the start number of a set of displayed pages
     */
    public int getHistoryStartPage(int currentPage) {
        return ((int) Math.ceil((double) currentPage /NUM_OF_DISPLAYED_PAGES) - 1)*NUM_OF_DISPLAYED_PAGES + 1;
    }
    /**
     * The method returns the maximum of history pages by username
     */
    public int getMaxHistoryPages(String username) {
        return gameSessionRepository.getMaxHistoryPages(username);
    }

    /**
     * The method continues a deactivated game by passing parameters to repository
     */
    public void continueGame(String username, String gameId) {
        gameSessionRepository.continueGameSession(username, gameId);
    }


}

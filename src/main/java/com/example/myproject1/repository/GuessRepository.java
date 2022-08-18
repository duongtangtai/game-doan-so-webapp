package com.example.myproject1.repository;

import com.example.myproject1.model.Guess;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GuessRepository extends AbstractRepository {
    private final List<Guess> guessList;
    public GuessRepository() {
        guessList = new ArrayList<>();
    }

    /**
     * The method returns a list of Guess Models for an input GameSession ID
     * @param gameSessionIdInput input GameSession ID
     * @return a list of Guess Models
     */
    public List<Guess> getGuessList(String gameSessionIdInput) {
        String sqlSt = """
                    select gameSessionId, guessNumber, result, time
                    from guess
                    where gameSessionId=?
                """;
        guessList.clear();
        return processListQuery(sqlSt, preparedStatement -> {
            ResultSet resultSet = fillStatement(preparedStatement, gameSessionIdInput).executeQuery();
            while (resultSet.next()) {
                guessList.add(new Guess()
                        .gameSessionId(resultSet.getString("gameSessionId"))
                        .guessNumber(resultSet.getInt("guessNumber"))
                        .result(resultSet.getString("result"))
                        .time(resultSet.getTimestamp("time").toLocalDateTime()));
            }
            return guessList;
        });
    }

    /**
     * The method saves a Guess Model into database
     */
    public void saveGuess(Guess guess) {
        String sqlSt = "insert into guess values (?,?,?,?)";
        processObjQuery(sqlSt, preparedStatement -> fillStatement(preparedStatement,
               guess.getGameSessionId(),
               guess.getGuessNumber(),
               guess.getResult(),
               Timestamp.valueOf(guess.getTime()))
                .executeUpdate());
    }
}

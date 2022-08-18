package com.example.myproject1.repository;

import com.example.myproject1.model.GameSession;
import com.example.myproject1.model.History;
import com.example.myproject1.model.Rank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameSessionRepository extends AbstractRepository {
    private final List<Rank> rankList;
    private final List<History> historyList;
    public GameSessionRepository() {
        rankList = new ArrayList<>();
        historyList = new ArrayList<>();
    }
    /**
     * This method converts an input ResultSet to a GameSession Model
     * @param resultSet input ResultSet
     * @return GameSession Model
     */
    private GameSession convertRSToGameSession(ResultSet resultSet) throws SQLException {
        return new GameSession()
                .startId(resultSet.getInt("startId"))
                .id(resultSet.getString("id"))
                .targetNumber(resultSet.getInt("targetNumber"))
                .startTime(resultSet.getTimestamp("startTime").toLocalDateTime())
                .endTime(resultSet.getTimestamp("endTime").toLocalDateTime())
                .completed(resultSet.getInt("completed")==1)
                .active(resultSet.getInt("active")==1)
                .username(resultSet.getString("username"));
    }

    /**
     * The method has two functions. One is for inserting a new record, the other is for updating
     * an old one.
     * @param gameSession input GameSession Model
     * @param sqlSt SQL statement
     */
    private void updateOrSaveToDB(GameSession gameSession, String sqlSt) {
        processObjQuery(sqlSt, preparedStatement -> {
            int startId = gameSession.getStartId();
            String id = gameSession.getId();
            int targetNumber = gameSession.getTargetNumber();
            Timestamp startTime = Timestamp.valueOf(gameSession.getStartTime());
            Timestamp endTime = Timestamp.valueOf(gameSession.getEndTime());
            int completed = gameSession.isCompleted() ? 1 : 0;
            int active = gameSession.isActive() ? 1 : 0;
            String username = gameSession.getUsername();
            if (sqlSt.startsWith("insert")) {
                return fillStatement(preparedStatement, startId, id, targetNumber, startTime, endTime,
                        completed, active, username).executeUpdate();
            } else { //update
                return fillStatement(preparedStatement, id, targetNumber, startTime, endTime,
                        completed, active, username, startId).executeUpdate();
            }
        });
    }
    /**
     * The method inserts an input GameSession Model into database
     * @param gameSession an input GameSession Model
     */
    public void saveGameSession(GameSession gameSession) {
        String sqlSt = "insert into game_session values (?,?,?,?,?,?,?,?)";
        updateOrSaveToDB(gameSession, sqlSt);
    }

    /**
     * The method updates an input GameSession Model into database
     * @param gameSession an input GameSession Model
     */
    public void updateGameSession(GameSession gameSession) {
        String sqlSt = """
                update game_session set id=?, targetNumber=?, startTime=?,
                endTime=?, completed=? , active=?, username=? where startId=?
                """;
        updateOrSaveToDB(gameSession, sqlSt);
    }

    /**
     * The method returns a GameSession Model with an input username
     * @param username input username
     * @return a GameSession Model
     */
    public GameSession getGameSession(String username) {
        String sqlSt = """
                    select startId, id, targetNumber, startTime, endTime, completed, active, username
                    from game_session
                    where username=? and active=1
                """;
        return processObjQuery(sqlSt, preparedStatement -> {
           ResultSet resultSet = fillStatement(preparedStatement, username).executeQuery();
           if (resultSet.next()) {
               return convertRSToGameSession(resultSet);
           }
           return null;
        });
    }

    /**
     * The method deactivates all the games of a username
     * @param username an input username
     */
    public void deactivateCurrentGame(String username) {
        String sqlSt = "update game_session set active=0 where username=?";
        processObjQuery(sqlSt, preparedStatement -> fillStatement(preparedStatement, username).executeUpdate());
    }

    /**
     *  The method returns the maximum number of history pages of a player. One page can only contain 10 records
     */
    public int getMaxHistoryPages(String username) {
        String sqlSt = """
                select count(id) as maxGame
                from game_session
                where username = ?
                """;
        return processObjQuery(sqlSt, preparedStatement -> {
           ResultSet resultSet = fillStatement(preparedStatement, username).executeQuery();
           resultSet.next();
           double totalGames = Double.parseDouble(resultSet.getString("maxGame"));
           return totalGames == 0 ? 1 : (int) Math.ceil(totalGames/10);
        });
    }
    /**
     * The method returns a list of History Models
     * @param username input username
     * @return a list of History Models
     */
    public List<History> getHistory(String username, int currentPage, int numOfDisplayedRecs) {
        String sqlSt = """
                    SELECT id, count(gameSessionId) AS tryTimes, startTime, endTime, completed
                    FROM game_session gs
                    LEFT JOIN guess g
                    ON gs.id = g.gameSessionId
                    WHERE username = ?
                    GROUP BY id, startTime, endTime
                    LIMIT ? OFFSET ?
                """;
        historyList.clear();
        return processListQuery(sqlSt, preparedStatement -> {
            ResultSet resultSet = fillStatement(preparedStatement, username,
                    numOfDisplayedRecs, (currentPage-1)*numOfDisplayedRecs).executeQuery();
           while (resultSet.next()) {
               historyList.add(new History()
                       .gameSessionId(resultSet.getString("id"))
                       .tryTimes(Integer.parseInt(resultSet.getString("tryTimes")))
                       .startTime(resultSet.getTimestamp("startTime").toLocalDateTime())
                       .endTime(resultSet.getTimestamp("endTime").toLocalDateTime())
                       .completed(resultSet.getInt("completed") == 1));
           }
           return historyList;
        });
    }

    /**
     * The method will active the GameSession and deactivate the others
     * @param username input username
     * @param gameSessionId input gameSessionId of the user
     */
    public void continueGameSession(String username, String gameSessionId) {
        deactivateCurrentGame(username);
        String sqlSt = "update game_session set active=1 where id=?";
        processObjQuery(sqlSt, preparedStatement -> fillStatement(preparedStatement, gameSessionId).executeUpdate());
    }

    /**
     * The method returns a startId number which is a based number for upcoming created games
     * @return an int value
     */
    public int getStartId() {
        String sqlSt = "select max(startId) as max_id from game_session";
        return processObjQuery(sqlSt, preparedStatement -> {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return Integer.parseInt(Optional.ofNullable(resultSet.getString("max_id")).orElse("0"));
        });
    }

    /**
     * The method returns a list of Rank Models due to player rankings
     * @return a list of Rank Models
     */
    public List<Rank> getRankList() {
        String sqlSt = """
                SELECT table_a.username, max(table_a.id) AS gameSessionId, table_a.tryTimes, min(table_a.totalSeconds) AS totalSeconds
                FROM
                (SELECT username, id, count(*) AS tryTimes, TIMESTAMPDIFF(SECOND, startTime, endTime) AS totalSeconds
                FROM game_session gs JOIN guess g
                ON gs.id = g.gameSessionId
                GROUP BY username, id, TIMESTAMPDIFF(SECOND, startTime, endTime)) AS table_a
                JOIN
                (SELECT username, min(tryTimes) min_tryTimes
                FROM
                (SELECT username, gs.id, count(*) AS tryTimes
                FROM game_session gs JOIN guess g
                ON gs.id = g.gameSessionId
                WHERE gs.completed = 1
                GROUP BY username, gs.id) AS table_c
                GROUP BY username) AS table_b
                ON table_a.username = table_b.username
                AND table_a.tryTimes = table_b.min_tryTimes
                GROUP BY username, tryTimes
                ORDER BY tryTimes ASC, min(totalSeconds) ASC;
                """;
        rankList.clear();
        return processListQuery(sqlSt, preparedStatement -> {
           ResultSet resultSet = preparedStatement.executeQuery();
           while (resultSet.next()) {
               rankList.add(new Rank()
                       .username(resultSet.getString("username"))
                       .gameSessionId(resultSet.getString("gameSessionId"))
                       .tryTimes(Integer.parseInt(resultSet.getString("tryTimes")))
                       .totalSeconds(Integer.parseInt(resultSet.getString("totalSeconds"))));
           }
           return rankList;
        });
    }
}

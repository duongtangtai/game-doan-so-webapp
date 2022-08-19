package com.example.myproject1.repository;

import com.example.myproject1.model.Player;

import java.sql.ResultSet;
public class PlayerRepository extends AbstractRepository{
    /**
     * The method has two functions. One is for inserting a new record. The other is for updating an old one.
     * @param player input Player Model
     * @param sqlSt SQL statement
     */
    private void updateOrSaveToDB(Player player, String sqlSt) {
        processObjQuery(sqlSt, preparedStatement -> {
            String username = player.getUsername();
            String password = player.getPassword();
            String name = player.getName();
            if (sqlSt.startsWith("insert")) {
                return fillStatement(preparedStatement, username, password, name).executeUpdate();
            }//update
            return fillStatement(preparedStatement, password, name, username).executeUpdate();
        });
    }

    /**
     * The method updates an input Player Model into database
     * @param player an input Player Model
     */
    public void updatePlayer(Player player) {
        String sqlSt = "update player set password=?, name=? where username=?";
        updateOrSaveToDB(player, sqlSt);
    }

    /**
     * The method inserts an input Player Model into database
     * @param player an input Player Model
     */
    public void savePlayer(Player player) {
        String sqlSt = "insert into player values (?,?,?)";
        updateOrSaveToDB(player, sqlSt);
    }

    /**
     * The method check if the player exists
     * @param username input username
     * @return TRUE if the player exists. Otherwise FALSE.
     */
    public boolean doesPlayerExist(String username) {
        String sqlSt = """
                    select username, password, name
                    from player
                    where username=?
                """;
        return processObjQuery(sqlSt, preparedStatement -> {
           ResultSet resultSet = fillStatement(preparedStatement, username).executeQuery() ;
           return resultSet.next();
        });
    }

    /**
     * The method check the username and password existence
     * @param username input username
     * @param password input password
     * @return TRUE if exists. Otherwise FALSE
     */
    public Player checkLogin(String username, String password) {
        String sqlSt = """
                    select username, password, name
                    from player
                    where username=? and password=?
                """;
        return processObjQuery(sqlSt, preparedStatement -> {
            ResultSet resultSet = fillStatement(preparedStatement, username, password).executeQuery();
            if (resultSet.next()) {
                return new Player()
                        .username(resultSet.getString("username"))
                        .password(resultSet.getString("password"))
                        .name(resultSet.getString("name"));
            }
            return null;
        });
    }
}

package pl.grzywniak.toxiccraft;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

class Database
{
    private static Connection conn;
    private static Statement stat;

    private static String DRIVER;
    private static File SQLite = new File("plugins/ToxicCraft/Database.db");;

    static void connect()
    {
        try
        {
            if(Config.databaseType.equalsIgnoreCase("MySQL"))
            {
                DRIVER = "com.mysql.jdbc.Driver";
            }
            else
            {
                DRIVER = "org.sqlite.JDBC";
                if (!SQLite.exists())
                {
                    try
                    {
                        SQLite.createNewFile();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            Class.forName(Database.DRIVER);
        }
        catch (ClassNotFoundException e)
        {
            System.err.println("Problem with MySQL Driver...");
            e.printStackTrace();
        }

        try
        {
            String DB_URL;
            if(Config.databaseType.equalsIgnoreCase("MySQL"))
            {
                DB_URL = "jdbc:mysql://" + Config.host + ":" + Config.port + "/" + Config.base + "?autoReconnect=true&user=" + Config.user + "&password=" + Config.pass;
            }
            else
            {
                DB_URL = "jdbc:sqlite:" + SQLite.getPath();
            }


            if(conn == null)
            {
                conn = DriverManager.getConnection(DB_URL);
                stat = conn.createStatement();
            }
        }
        catch(SQLException e)
        {
            System.err.println("Problem with opening connection with Database...");
            e.printStackTrace();
        }
        createTable();
    }

    static void closeConnection()
    {
        try
        {
            if(conn != null)
            {
                conn.close();
            }
        }
        catch (SQLException e)
        {
            System.err.println("Problem with closing connection with database...");
            e.printStackTrace();
        }
    }

    private static void createTable()
    {
        String createTable = "CREATE TABLE IF NOT EXISTS ToxicCraft (id INTEGER AUTO_INCREMENT, username VARCHAR(30), banTime BIGINT(8), UNIQUE (username), PRIMARY KEY(id))";

        try
        {
            stat.execute(createTable);
        }
        catch (SQLException e)
        {
            System.err.println("Problem with create table");
            e.printStackTrace();
        }
    }

    static void addDeathPlayer(String username)
    {
        try
        {
           // stat.execute("INSERT INTO ToxicCraft VALUES (NULL, '" + username +"'," + System.currentTimeMillis() +  ");");
            stat.execute("INSERT INTO ToxicCraft (username, banTime)" +
                    "SELECT * FROM (SELECT '" + username + "', " + System.currentTimeMillis() + ") AS tmp WHERE NOT EXISTS ( SELECT username FROM ToxicCraft WHERE username = '" + username + "') LIMIT 1;");
        }
        catch (SQLException e)
        {
            System.err.println("Problem with adding user to MySQL!");
            e.printStackTrace();
        }
    }

    static void removeDeathPlayer(String username)
    {
        try
        {
            stat.execute("DELETE FROM ToxicCraft WHERE username = '" + username + "';");
        }
        catch (SQLException e)
        {
            System.err.println("Problem with deleting user from database!");
            e.printStackTrace();
        }
    }

    static HashMap<String, Long> getDeathUsers()
    {
        HashMap<String, Long> listOfDeathPlayers = new HashMap<>();

        try
        {
            ResultSet result = stat.executeQuery("SELECT username, banTime FROM ToxicCraft;");

            while(result.next())
            {
                String username = result.getString("username");
                long banTime = result.getLong("banTime");

                listOfDeathPlayers.put(username, banTime);
            }
        }
        catch (SQLException e)
        {
            System.err.println("Problem with getting list of death players!");
            e.printStackTrace();
        }
        return listOfDeathPlayers;
    }
}
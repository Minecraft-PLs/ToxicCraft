package pl.grzywniak.toxiccraft;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

class Config
{
    final private static File directory = new File("plugins/ToxicCraft/");
    final private static File config = new File("plugins/ToxicCraft/Config.yml");
    static String databaseType;
    static String host;
    static String base;
    static String user;
    static String pass;
    static int port;

    static void createDefaultFile()
    {
        if (!directory.exists())
        {
            directory.mkdirs();
        }

        if (!config.exists())
        {
            try
            {
                config.createNewFile();

                Writer writer = new BufferedWriter(new FileWriter(config));

                writer.write("Config:");
                ((BufferedWriter) writer).newLine();
                writer.write("  Database:");
                ((BufferedWriter) writer).newLine();
                writer.write("    #Database types: SQL/MySQL:");
                ((BufferedWriter) writer).newLine();
                writer.write("    type: SQL");
                ((BufferedWriter) writer).newLine();
                writer.write("    host: localhost");
                ((BufferedWriter) writer).newLine();
                writer.write("    base: baseName");
                ((BufferedWriter) writer).newLine();
                writer.write("    user: root");
                ((BufferedWriter) writer).newLine();
                writer.write("    pass: password");
                ((BufferedWriter) writer).newLine();
                writer.write("    port: 3306");
                ((BufferedWriter) writer).newLine();
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        getVariables();
    }

    private static void getVariables()
    {
        FileConfiguration conf = YamlConfiguration.loadConfiguration(config);

        databaseType = conf.getString("Config.Database.type");
        host = conf.getString("Config.Database.host");
        base = conf.getString("Config.Database.base");
        user = conf.getString("Config.Database.user");
        pass = conf.getString("Config.Database.pass");
        port = conf.getInt("Config.Database.port");
    }
}
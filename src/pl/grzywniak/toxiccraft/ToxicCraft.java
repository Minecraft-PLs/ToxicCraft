package pl.grzywniak.toxiccraft;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.plugin.java.JavaPlugin;

public class ToxicCraft extends JavaPlugin
{
    private static ToxicCraft instance;

    @Override
    public void onEnable()
    {
        System.out.println("ToxicCraft by https://grzywniak.pl");
        instance = this;
        Config.createDefaultFile();
        Database.connect();
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);
        DeathSystem.loadDeathPlayers();
        runCheckerOfDeathPlayers();
        setWeather();
        AntyRadiacyEQ.addRecipe();
    }

    static ToxicCraft getInstance()
    {
        return instance;
    }

    private void runCheckerOfDeathPlayers()
    {
        Bukkit.getScheduler().runTaskTimer(getInstance(), DeathSystem::checkDeathPlayers, 0, 20 * 30);
    }

    private void setWeather()
    {
        if(Bukkit.getWorld("world").getGameRuleValue(GameRule.DO_WEATHER_CYCLE))
        {
            Bukkit.getWorld("world").setGameRule(GameRule.DO_WEATHER_CYCLE, false);
        }
        Bukkit.getWorld("world").setStorm(true);
    }

    @Override
    public void onDisable()
    {
        Database.closeConnection();
    }
}
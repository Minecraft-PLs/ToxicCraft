package pl.grzywniak.toxiccraft;

import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class DeathSystem
{
    private static HashMap<String, Long> deathPlayers = new HashMap<>();

    static void loadDeathPlayers()
    {
       deathPlayers = Database.getDeathUsers();
    }

    static void addDeathPlayer(String playerName)
    {
        deathPlayers.put(playerName, System.currentTimeMillis());
        Database.addDeathPlayer(playerName);
        hideNormalPlayers(playerName);
    }

    private static void hideNormalPlayers(String playerName) //TODO check p.hideplayer
    {
        Player p = Bukkit.getPlayer(playerName);
        for(Player players : Bukkit.getOnlinePlayers())
        {
            if(players.getGameMode() != GameMode.SPECTATOR && !players.getName().equals(playerName))
            {
                PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, (((CraftPlayer) players).getHandle()));
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(info);
            }
        }
    }

    private static void showNormalPlayers(Player p)
    {
        for(Player players : Bukkit.getOnlinePlayers())
        {
            if(!players.getName().equals(p.getName()))
            {
                PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, (((CraftPlayer) players).getHandle()));
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(info);
            }
        }
    }

    static void checkDeathPlayers()
    {
        List<String> listToRemoveUsers = new ArrayList<>();

        for(String userName : deathPlayers.keySet())
        {
            if(deathPlayers.get(userName) + 86400000 < System.currentTimeMillis())
            {
                listToRemoveUsers.add(userName);
            }
        }

        for(String userName : listToRemoveUsers)
        {
            deathPlayers.remove(userName);
            Database.removeDeathPlayer(userName);

            if(Bukkit.getOfflinePlayer(userName).isOnline() && Bukkit.getPlayer(userName).getGameMode() == GameMode.SPECTATOR)
            {
                Player p = Bukkit.getPlayer(userName);
                p.teleport(Bukkit.getWorld("world").getSpawnLocation());
                p.setGameMode(GameMode.SURVIVAL);
                showNormalPlayers(p);
                p.getInventory().addItem(new ItemStack(Material.OAK_WOOD, 64), new ItemStack(Material.BREAD, 32), new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.WOODEN_SWORD));

            }
        }
    }

    static String getTimeToUnBan(Player p)
    {
        long deathTimeInSec = deathPlayers.get(p.getName()) + 86400000;
        long timeInSec = (deathTimeInSec - System.currentTimeMillis())/1000;
        String text = "";

        int second = 0, minute = 0, hour = 0, day = 0;

        second = (int) timeInSec % 60;
        minute = (int) (timeInSec / 60) % 60;
        hour = (int) (timeInSec / (60 * 60)) % 24;
        day = (int) ((timeInSec / (60 * 60 * 24)) % 365.5);

        if(day >= 1)
        {
            text = text.concat("§9Dni: §c" + day + "\n");
        }

        if(hour >= 1)
        {
            text = text.concat("§9Godzin: §c" + hour + "\n");
        }

        if(minute >= 1)
        {
            text = text.concat("§9Minut: §c" + minute + "\n");
        }

        if(second > 1)
        {
            text = text.concat("§9Sekund: §c"+ second + "\n");
        }

        return text;
    }

    static boolean isDeathPlayer(String userName)
    {
        return deathPlayers.containsKey(userName);
    }
}
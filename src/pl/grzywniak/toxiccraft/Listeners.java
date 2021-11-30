package pl.grzywniak.toxiccraft;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Listeners implements Listener
{
    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        if(p.getGameMode() == GameMode.SPECTATOR)
        {
            if(!DeathSystem.isDeathPlayer(p.getName()))
            {
                p.teleport(Bukkit.getWorld("world").getSpawnLocation());
                p.setGameMode(GameMode.SURVIVAL);
                p.getInventory().addItem(new ItemStack(Material.OAK_WOOD, 64), new ItemStack(Material.BREAD, 32), new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.WOODEN_SWORD));
            }
            else
            {
                for(Player pl : Bukkit.getOnlinePlayers())
                {
                    p.hidePlayer(ToxicCraft.getInstance(), pl);
                }
            }
        }

        for(Player pl : Bukkit.getOnlinePlayers()) //TODO OPTIMALISE
        {
            if(DeathSystem.isDeathPlayer(pl.getName()) && pl.getName() != p.getName())
            {
                pl.hidePlayer(ToxicCraft.getInstance(), p);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        AntyRadiacyEQ.removeFromListToReduceDurability(e.getPlayer().getName());
    }

    @EventHandler
    public void onFirstLogin(PlayerLoginEvent e)
    {
        Player p = e.getPlayer();

        if(!p.hasPlayedBefore())
        {
             p.getInventory().addItem(new ItemStack(Material.OAK_WOOD, 64), new ItemStack(Material.BREAD, 32), new ItemStack(Material.LEATHER_BOOTS), new ItemStack(Material.WOODEN_SWORD));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMove(final PlayerMoveEvent e)
    {
        final Player p = e.getPlayer();

        if(p.getGameMode() == GameMode.SPECTATOR)
        {
            Location spawnLoc = Bukkit.getWorld("world").getSpawnLocation();
            if(p.getLocation().distance(spawnLoc) > 50)
            {
                p.teleport(spawnLoc);
                p.sendMessage("§cNie możesz oddalać się od spawna będąc martwym");
            }
            return;
        }

        if(p.getGameMode() == GameMode.CREATIVE || !p.getWorld().getName().equals("world"))
        {
            return;
        }

        Location pLoc = p.getLocation();

        //DMG OD WODY
        if(pLoc.getBlock().getType() == Material.WATER)
        {
            if(AntyRadiacyEQ.playerHasFullEQ(p))
            {
                AntyRadiacyEQ.reduceDurability(p);
                return;
            }
            if(p.hasPotionEffect(PotionEffectType.GLOWING))
            {
                p.removePotionEffect(PotionEffectType.GLOWING);
            }
            if(p.hasPotionEffect(PotionEffectType.BLINDNESS))
            {
                p.removePotionEffect(PotionEffectType.BLINDNESS);
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 4));
            p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 4));
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 1200, 0));
            if(p.getHealth() <= 1)
            {
                p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40, 0));
            }

        } //DMG OD DESZCZU
        else if (p.getLocation().getBlockY() + 2 >= p.getWorld().getHighestBlockYAt(p.getLocation()))
        {
            if(AntyRadiacyEQ.playerHasFullEQ(p))
            {
                AntyRadiacyEQ.reduceDurability(p);
                return;
            }
            if(p.hasPotionEffect(PotionEffectType.GLOWING))
            {
                p.removePotionEffect(PotionEffectType.GLOWING);
            }
            p.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 40, 4));
            p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 600, 0));
            if(p.getHealth() <= 1)
            {
                p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 40, 0));
            }
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent e)
    {
        Player p = e.getPlayer();

        if(!DeathSystem.isDeathPlayer(p.getName())) return;

        if(p.getGameMode() == GameMode.SPECTATOR && (e.getMessage().startsWith("/msg") || e.getMessage().startsWith("/tell") || e.getMessage().startsWith("/minecraft:tell") || e.getMessage().startsWith("/minecraft:msg")))
        {
            p.sendMessage("§cNie możesz używać czatu gdy jesteś obserwatorem :/\n§cDo odrodzenia zostało:\n%BAN_TIME%".replace("%BAN_TIME%", DeathSystem.getTimeToUnBan(p)));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSendMessages(AsyncPlayerChatEvent e)
    {
        Player p = e.getPlayer();

        if(!DeathSystem.isDeathPlayer(p.getName())) return;

        if(p.getGameMode() == GameMode.SPECTATOR)
        {
            p.sendMessage("§cNie możesz używać czatu gdy jesteś obserwatorem :/\n§cDo odrodzenia zostało:\n%BAN_TIME%".replace("%BAN_TIME%", DeathSystem.getTimeToUnBan(p)));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        DeathSystem.addDeathPlayer(e.getEntity().getName());
        Location thorLoc = e.getEntity().getLocation();
        thorLoc.setY(thorLoc.getY() + 5);
        e.getEntity().getLocation().getWorld().spawnEntity(thorLoc, EntityType.LIGHTNING);
        e.getEntity().teleport(Bukkit.getWorld("world").getSpawnLocation());
        AntyRadiacyEQ.removeFromListToReduceDurability(e.getEntity().getName());
    }
}

package pl.grzywniak.toxiccraft;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class AntyRadiacyEQ
{
    private static HashMap<String, Integer> durabilityOfEQ = new HashMap<>();

    static boolean playerHasFullEQ(Player p)
    {
        EntityEquipment ee = p.getEquipment();
        if(ee.getHelmet() != null && ee.getHelmet().hasItemMeta() && ee.getChestplate() != null && ee.getChestplate().hasItemMeta() && ee.getLeggings() != null && ee.getLeggings().hasItemMeta() && ee.getBoots() != null && ee.getBoots().hasItemMeta())
        {
            if (ee.getHelmet().getItemMeta().getLore().contains("§aAnty radiacja") && ee.getChestplate().getItemMeta().getLore().contains("§aAnty radiacja")
                    && ee.getLeggings().getItemMeta().getLore().contains("§aAnty radiacja") && ee.getBoots().getItemMeta().getLore().contains("§aAnty radiacja"))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    static void reduceDurability(Player p)
    {
        if(durabilityOfEQ.containsKey(p.getName()))
        {
            if(durabilityOfEQ.get(p.getName()) == 10)
            {
                durabilityOfEQ.put(p.getName(), 0);

                PlayerInventory pi = p.getInventory();

                if(p.hasPotionEffect(PotionEffectType.GLOWING))
                {
                    p.removePotionEffect(PotionEffectType.GLOWING);
                }

                p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 300, 0));
                Damageable helmetDmgMeta = (Damageable)pi.getHelmet().getItemMeta();
                helmetDmgMeta.setDamage(helmetDmgMeta.getDamage() + 1);
                pi.getHelmet().setItemMeta((ItemMeta)helmetDmgMeta);
                if(pi.getHelmet().getType().getMaxDurability() < helmetDmgMeta.getDamage())
                {
                    pi.setHelmet(null);
                }

                Damageable chestplateDmgMeta = (Damageable)pi.getChestplate().getItemMeta();
                chestplateDmgMeta.setDamage(chestplateDmgMeta.getDamage() + 1);
                pi.getChestplate().setItemMeta((ItemMeta)chestplateDmgMeta);
                if(pi.getChestplate().getType().getMaxDurability() < chestplateDmgMeta.getDamage())
                {
                    pi.setChestplate(null);
                }

                Damageable leggingsDmgMeta = (Damageable)pi.getLeggings().getItemMeta();
                leggingsDmgMeta.setDamage(leggingsDmgMeta.getDamage() + 1);
                pi.getLeggings().setItemMeta((ItemMeta)leggingsDmgMeta);
                if(pi.getLeggings().getType().getMaxDurability() < leggingsDmgMeta.getDamage())
                {
                    pi.setLeggings(null);
                }

                Damageable bootsDmgMeta = (Damageable)pi.getBoots().getItemMeta();
                bootsDmgMeta.setDamage(bootsDmgMeta.getDamage() + 1);
                pi.getBoots().setItemMeta((ItemMeta)bootsDmgMeta);
                if(pi.getBoots().getType().getMaxDurability() < bootsDmgMeta.getDamage())
                {
                    pi.setBoots(null);
                }
            }
            else
            {
                durabilityOfEQ.put(p.getName(), durabilityOfEQ.get(p.getName())+1);
            }
        }
        else
        {
            durabilityOfEQ.put(p.getName(), 1);
        }
    }

    public static void removeFromListToReduceDurability(String playerName)
    {
        if(durabilityOfEQ.containsKey(playerName))
        {
            durabilityOfEQ.remove(playerName);
        }
    }

    static void addRecipe()
    {
        List<String> lista = new ArrayList<String>();
        lista.add("§aAnty radiacja");
        lista.add("");
        lista.add("§c§lUWAGA");
        lista.add("§7Ten przedmiot zużywa się");
        lista.add("§7gdy jest narażony na radiację!");

        //HELMET
        ItemStack helmItem = new ItemStack(Material.IRON_HELMET);

        ItemMeta helmMeta = helmItem.getItemMeta();
        helmMeta.setDisplayName(ChatColor.GREEN + "Anty radiacyjny hełm");
        helmMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 9, true);
        helmMeta.setLore(lista);
        helmItem.setItemMeta(helmMeta);

        NamespacedKey helmKey = new NamespacedKey(ToxicCraft.getInstance(), "anty_radiacy_helm");
        ShapedRecipe helmRecipe = new ShapedRecipe(helmKey, helmItem);
        helmRecipe.shape("123", "456", "789");

        helmRecipe.setIngredient('1', Material.GOLDEN_APPLE);
        helmRecipe.setIngredient('2', Material.ENDER_PEARL);
        helmRecipe.setIngredient('3', Material.GOLDEN_APPLE);
        helmRecipe.setIngredient('4', Material.BONE_BLOCK);
        helmRecipe.setIngredient('5', Material.IRON_HELMET);
        helmRecipe.setIngredient('6', Material.BOOK);
        helmRecipe.setIngredient('7', Material.GREEN_DYE);
        helmRecipe.setIngredient('8', Material.YELLOW_DYE);
        helmRecipe.setIngredient('9', Material.RED_DYE);

        //CHESTPLATE
        ItemStack chestplateItem = new ItemStack(Material.IRON_CHESTPLATE);

        ItemMeta chestplateMeta = chestplateItem.getItemMeta();
        chestplateMeta.setDisplayName(ChatColor.GREEN + "Anty radiacyjna zbroja");
        chestplateMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 9, true);
        chestplateMeta.setLore(lista);
        chestplateItem.setItemMeta(chestplateMeta);

        NamespacedKey chestplateKey = new NamespacedKey(ToxicCraft.getInstance(), "anty_radiacy_chestplate");
        ShapedRecipe chestplateRecipe = new ShapedRecipe(chestplateKey, chestplateItem);
        chestplateRecipe.shape("123", "456", "789");

        chestplateRecipe.setIngredient('1', Material.GOLDEN_APPLE);
        chestplateRecipe.setIngredient('2', Material.EMERALD);
        chestplateRecipe.setIngredient('3', Material.GOLDEN_APPLE);
        chestplateRecipe.setIngredient('4', Material.ROTTEN_FLESH);
        chestplateRecipe.setIngredient('5', Material.IRON_CHESTPLATE);
        chestplateRecipe.setIngredient('6', Material.ROTTEN_FLESH);
        chestplateRecipe.setIngredient('7', Material.MUSHROOM_STEW);
        chestplateRecipe.setIngredient('8', Material.BREAD);
        chestplateRecipe.setIngredient('9', Material.MUSHROOM_STEW);

        //LEGGINGS
        ItemStack leggingsItem = new ItemStack(Material.IRON_LEGGINGS);

        ItemMeta leggingsMeta = leggingsItem.getItemMeta();
        leggingsMeta.setDisplayName(ChatColor.GREEN + "Anty radiacyjne nogawice");
        leggingsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 9, true);
        leggingsMeta.setLore(lista);
        leggingsItem.setItemMeta(leggingsMeta);

        NamespacedKey leggingsKey = new NamespacedKey(ToxicCraft.getInstance(), "anty_radiacy_leggings");
        ShapedRecipe leggingsRecipe = new ShapedRecipe(leggingsKey, leggingsItem);
        leggingsRecipe.shape("123", "456", "789");

        leggingsRecipe.setIngredient('1', Material.GOLDEN_APPLE);
        leggingsRecipe.setIngredient('2', Material.NETHER_WART_BLOCK);
        leggingsRecipe.setIngredient('3', Material.GOLDEN_APPLE);
        leggingsRecipe.setIngredient('4', Material.QUARTZ);
        leggingsRecipe.setIngredient('5', Material.IRON_LEGGINGS);
        leggingsRecipe.setIngredient('6', Material.QUARTZ);
        leggingsRecipe.setIngredient('7', Material.NETHER_BRICK);
        leggingsRecipe.setIngredient('8', Material.BLAZE_ROD);
        leggingsRecipe.setIngredient('9', Material.NETHER_BRICK);

        //BOOTS
        ItemStack bootsItem = new ItemStack(Material.IRON_BOOTS);

        ItemMeta bootsMeta = bootsItem.getItemMeta();
        bootsMeta.setDisplayName(ChatColor.GREEN + "Anty radiacyjne buty");
        bootsMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 9, true);
        bootsMeta.setLore(lista);
        bootsItem.setItemMeta(bootsMeta);

        NamespacedKey bootsKey = new NamespacedKey(ToxicCraft.getInstance(), "anty_radiacy_boots");
        ShapedRecipe bootsRecipe = new ShapedRecipe(bootsKey, bootsItem);
        bootsRecipe.shape("123", "456", "789");

        bootsRecipe.setIngredient('1', Material.GOLDEN_APPLE);
        bootsRecipe.setIngredient('2', Material.DIAMOND);
        bootsRecipe.setIngredient('3', Material.GOLDEN_APPLE);
        bootsRecipe.setIngredient('4', Material.LAPIS_LAZULI);
        bootsRecipe.setIngredient('5', Material.IRON_BOOTS);
        bootsRecipe.setIngredient('6', Material.LAPIS_LAZULI);
        bootsRecipe.setIngredient('7', Material.REDSTONE);
        bootsRecipe.setIngredient('8', Material.REDSTONE);
        bootsRecipe.setIngredient('9', Material.REDSTONE);

        Bukkit.addRecipe(helmRecipe);
        Bukkit.addRecipe(chestplateRecipe);
        Bukkit.addRecipe(leggingsRecipe);
        Bukkit.addRecipe(bootsRecipe);
        //TODO make meta to verify this specific item
    }
}
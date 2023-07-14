package com.bozheday.diablery.items;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpdatedEndermanEye implements Listener {
    public static ItemStack _updated_enderman_eye;

    public void UpdatedEndermanEyeItem(){
        WitchBlood wb = new WitchBlood();
        wb.WitchBloodItem();

        ItemStack item = new ItemStack(Material.ENDER_EYE);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("Вещь, позволяющая оказываться в точке взгляда");
        lore.add("Осталось использований:");
        lore.add("50");

        assert meta != null;
        meta.setDisplayName(ChatColor.DARK_PURPLE + "Зачарованное око эндермена" + ChatColor.BOLD);
        meta.addEnchant(Enchantment.DURABILITY, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setLore(lore);

        item.setItemMeta(meta);

        ShapedRecipe recipe = new ShapedRecipe(NamespacedKey.minecraft("updated_enderman_eye"), item);
        recipe.shape("ABA", "BCB", "ABA");
        recipe.setIngredient('A', new RecipeChoice.ExactChoice(wb._witch_blood));
        recipe.setIngredient('B', Material.AMETHYST_SHARD);
        recipe.setIngredient('C', Material.ENDER_EYE);

        _updated_enderman_eye = item;
        Bukkit.addRecipe(recipe);
    }

    @EventHandler
    public void onEyeRightClick(PlayerInteractEvent e){
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            if(Objects.equals(Objects.requireNonNull(e.getItem()).getType(), _updated_enderman_eye.getType())){
                if(Objects.requireNonNull(e.getItem().getItemMeta()).getDisplayName().equals(Objects.requireNonNull(_updated_enderman_eye.getItemMeta()).getDisplayName())){
                    if(e.getItem().getItemMeta().getEnchants().equals(_updated_enderman_eye.getItemMeta().getEnchants())){
                        int durability = Integer.parseInt(Objects.requireNonNull(e.getItem().getItemMeta().getLore()).get(2));
                        Location tbLoc = e.getPlayer().getTargetBlock(null, 50).getLocation();
                        Location loc = new Location(tbLoc.getWorld(), tbLoc.getX(), tbLoc.getY() + 1, tbLoc.getZ(), e.getPlayer().getLocation().getYaw(), e.getPlayer().getLocation().getPitch());
                        durability -= 1;
                        ItemMeta meta = e.getItem().getItemMeta();
                        List<String> lore = new ArrayList<>();
                        lore.add("Вещь, позволяющая оказываться в точке взгляда");
                        lore.add("Осталось использований:");
                        lore.add(Integer.toString(durability));
                        meta.setLore(lore);
                        e.getItem().setItemMeta(meta);

                        e.getPlayer().getWorld().spawnParticle(Particle.PORTAL, e.getPlayer().getLocation(), 50, 0.5, 1, 0.5);
                        e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 35, 1);
                        e.getPlayer().getWorld().spawnParticle(Particle.PORTAL, loc, 50, 0.5, 1, 0.5);
                        e.getPlayer().getWorld().playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 35, 1);
                        e.getPlayer().teleport(loc);

                        if(durability < 1){
                            e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);
                        }

                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}

package com.bozheday.diablery.entities;

import com.bozheday.diablery.items.WitchBlood;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Witch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CustomEntities implements Listener {
    public static Witch WitchMerchant(World world, Location location){
        Witch witch = (Witch) world.spawnEntity(location, EntityType.WITCH);
        witch.setCustomName(ChatColor.LIGHT_PURPLE + "Ludka");
        witch.setCustomNameVisible(true);
        Objects.requireNonNull(witch.getAttribute(Attribute.GENERIC_FOLLOW_RANGE)).setBaseValue(0);

        new BukkitRunnable(){
            public void run(){
                witch.remove();
            }
        }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), 20*60);

        return witch;
    }

    @EventHandler
    public void onWitchMerchantDamage(EntityDamageByEntityEvent e){
        if(e.getEntity().getType().equals(EntityType.WITCH)){
            if(e.getEntity().getCustomName().equals(ChatColor.LIGHT_PURPLE + "Ludka")){
                if(e.getEntity().isCustomNameVisible()){
                    Creature _witch = (Creature) e.getEntity();
                    _witch.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(5);
                }
            }
        }
    }

    @EventHandler
    public void onWitchMerchantClick(PlayerInteractEntityEvent e){
        if(e.getRightClicked().getType().equals(EntityType.WITCH)){
            if(e.getRightClicked().getCustomName().equals(ChatColor.LIGHT_PURPLE + "Ludka")){
                if(e.getRightClicked().isCustomNameVisible()){
                    WitchBlood wb = new WitchBlood();
                    wb.WitchBloodItem();
                    Merchant merchant = Bukkit.createMerchant("Ludka");
                    ItemStack emeralds = new ItemStack(Material.EMERALD, 10);
                    ItemStack amethysts = new ItemStack(Material.AMETHYST_SHARD, 12);

                    List<MerchantRecipe> merchantRecipeList = new ArrayList<>();
                    MerchantRecipe recipe = new MerchantRecipe(wb._witch_blood, 10000);
                    recipe.setExperienceReward(false);
                    recipe.addIngredient(emeralds);
                    recipe.addIngredient(amethysts);
                    merchantRecipeList.add(recipe);

                    merchant.setRecipes(merchantRecipeList);

                    e.getPlayer().openMerchant(merchant, true);
                }
            }
        }
    }
}

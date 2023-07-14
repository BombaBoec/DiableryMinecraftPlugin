package com.bozheday.diablery.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.ArrayList;
import java.util.List;

public class WitchBlood implements Listener {
    public  static ItemStack _witch_blood;

    public void WitchBloodItem(){
        ItemStack item = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add("Реагент, усиливающий магию");

        meta.setDisplayName(ChatColor.RED + "Ведьмина кровь");
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        meta.setLore(lore);
        meta.setColor(Color.RED);

        item.setItemMeta(meta);
        _witch_blood = item;
    }

    @EventHandler
    public void onWitchMurder(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Witch && e.getDamager() instanceof Player){
            Player damager = (Player) e.getDamager();
            LivingEntity damaged = (LivingEntity) e.getEntity();
            if(damaged.getHealth() <= e.getDamage()){
                if(damager.getInventory().getItemInOffHand().getType().equals(Material.GLASS_BOTTLE)){
                    damager.getInventory().addItem(_witch_blood);
                    damager.getInventory().getItemInOffHand().setAmount(damager.getInventory().getItemInOffHand().getAmount() - 1);
                }
            }
        }
    }
}

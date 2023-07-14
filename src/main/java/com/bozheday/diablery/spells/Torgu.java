package com.bozheday.diablery.spells;

import com.bozheday.diablery.effects.EffectUtils;
import com.bozheday.diablery.entities.CustomEntities;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Candle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;


import java.util.*;

public class Torgu implements Listener {
    @EventHandler
    public void onPlayerSpellTorgu(PlayerChatEvent e){
        if(e.getMessage().equals("Torgu")){
            List<Material> witchSummonIngredients = new ArrayList<>();
            Set<Material> transparentList = new HashSet<>();
            List<Location> candles = new ArrayList<>();

            transparentList.add(Material.PURPLE_CANDLE);
            transparentList.add(Material.AIR);

            witchSummonIngredients.add(Material.ENDER_PEARL);
            witchSummonIngredients.add(Material.AMETHYST_SHARD);
            witchSummonIngredients.add(Material.GLASS_BOTTLE);
            witchSummonIngredients.add(Material.REDSTONE);
            witchSummonIngredients.add(Material.SPIDER_EYE);

            Block targetBlock = e.getPlayer().getTargetBlock(transparentList, 5);
            Location bkLoc = new Location(e.getPlayer().getWorld(), targetBlock.getLocation().getX() + 0.5, targetBlock.getLocation().getY() + 1, targetBlock.getLocation().getZ() + 0.5);
            Location effLoc = new Location(e.getPlayer().getWorld(), bkLoc.getX(), bkLoc.getY() + 0.25, bkLoc.getZ());

            candles.add(new Location(e.getPlayer().getWorld(), bkLoc.getX() + 1, bkLoc.getY(), bkLoc.getZ()));
            candles.add(new Location(e.getPlayer().getWorld(), bkLoc.getX() - 1, bkLoc.getY(), bkLoc.getZ()));
            candles.add(new Location(e.getPlayer().getWorld(), bkLoc.getX(), bkLoc.getY(), bkLoc.getZ() + 1));
            candles.add(new Location(e.getPlayer().getWorld(), bkLoc.getX(), bkLoc.getY(), bkLoc.getZ() - 1));

            int candlesLit = 0;
            for(Location candle : candles){
                if(candle.getBlock().getType().equals(Material.PURPLE_CANDLE)){
                    Candle litCandle = (Candle) candle.getBlock().getBlockData();
                    if(litCandle.isLit()){
                        candlesLit++;
                    }
                }
            }

            ArmorStand itemChecker = (ArmorStand) e.getPlayer().getWorld().spawnEntity(bkLoc, EntityType.ARMOR_STAND);
            itemChecker.setInvisible(true);
            itemChecker.setInvulnerable(true);
            itemChecker.setSilent(true);

            List<Entity> entities = itemChecker.getNearbyEntities(0.5,0.5,0.5);
            int i = 0;
            for(Entity entity: entities){
                if(entity instanceof Item){
                    Item item = (Item) entity;
                    if(witchSummonIngredients.contains(item.getItemStack().getType())){
                        i++;
                    }
                }
            }
            if(candlesLit == 4){
                if(i == witchSummonIngredients.size()){
                    for(Entity entity: entities){
                        if(entity instanceof Item){
                            if(witchSummonIngredients.contains(((Item) entity).getItemStack().getType())){
                                entity.remove();
                                witchSummonIngredients.remove(((Item) entity).getItemStack().getType());
                            }
                        }
                    }

                    e.getPlayer().getWorld().playSound(bkLoc, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 35, -5);
                    e.getPlayer().getWorld().playSound(bkLoc, Sound.AMBIENT_CAVE, 35, 1);
                    e.getPlayer().getWorld().spawnParticle(Particle.END_ROD, bkLoc, 10, 0.25, 0.25, 0.25, 0.1);

                    boolean isRain = e.getPlayer().getWorld().hasStorm();
                    if(!isRain){
                        e.getPlayer().getWorld().setStorm(true);
                    }

                    EffectUtils.DownCircleDrawingEffectColorized(1, 24, Color.BLACK, 1, effLoc, 1, 0, 1, 0);

                    new BukkitRunnable(){
                        public void run() {
                            e.getPlayer().getWorld().playSound(bkLoc, Sound.ENTITY_ILLUSIONER_CAST_SPELL, 75, 1);
                        }
                    }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), 20*3);
                    EffectUtils.DownCircleEffectColorized(1, 24, Color.BLACK, 1, effLoc, 1, 0, 20 * 3, 1, 20 * 10);
                    EffectUtils.DownCircleEffectColorized(1, 24, Color.PURPLE, 0.25f, effLoc, 1, 0, 20 * 3, 1, 20 * 10);
                    if(e.getPlayer().getFacing().toString().equals("SOUTH")){
                        EffectUtils.DownPentagrammEffectColorized(effLoc, Color.BLACK, 0.8f, 1, 1, 0, 20 * 3, 1, 20 * 10, 0);
                        EffectUtils.DownPentagrammEffectColorized(effLoc, Color.PURPLE, 0.25f, 1, 1, 0, 20 * 3, 1, 20 * 10, 0);
                    }
                    if(e.getPlayer().getFacing().toString().equals("WEST")){
                        EffectUtils.DownPentagrammEffectColorized(effLoc, Color.BLACK, 0.8f, 1, 1, 0, 20 * 3, 1, 20 * 10, Math.PI/2);
                        EffectUtils.DownPentagrammEffectColorized(effLoc, Color.PURPLE, 0.25f, 1, 1, 0, 20 * 3, 1, 20 * 10, Math.PI/2);
                    }
                    if(e.getPlayer().getFacing().toString().equals("NORTH")){
                        EffectUtils.DownPentagrammEffectColorized(effLoc, Color.BLACK, 0.8f, 1, 1, 0, 20 * 3, 1, 20 * 10, Math.PI);
                        EffectUtils.DownPentagrammEffectColorized(effLoc, Color.PURPLE, 0.25f, 1, 1, 0, 20 * 3, 1, 20 * 10, Math.PI);
                    }
                    if(e.getPlayer().getFacing().toString().equals("EAST")){
                        EffectUtils.DownPentagrammEffectColorized(effLoc, Color.BLACK, 0.8f, 1, 1, 0, 20 * 3, 1, 20 * 10, 3*Math.PI/2);
                        EffectUtils.DownPentagrammEffectColorized(effLoc, Color.PURPLE, 0.25f, 1, 1, 0, 20 * 3, 1, 20 * 10, 3*Math.PI/2);
                    }
                    EffectUtils.LineEffectDrawingColorized(new Location(effLoc.getWorld(), effLoc.getX(), effLoc.getY() + 3.5, effLoc.getZ()), effLoc, Color.BLACK, 3f, 10, 0.1f, 15, 20 * 3, 10, 20 * 10);

                    BukkitRunnable runnable1 = new BukkitRunnable() {
                        double r = 0;

                        public void run() {
                            r += 1;

                            EffectUtils.DownCircleEffectColorizedNoneRun(r, 48, Color.BLACK, 1.5f, effLoc, 1 ,0);
                        }
                    };
                    runnable1.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), 20 * 10, 2);

                    new BukkitRunnable(){
                        public void run(){
                            runnable1.cancel();
                        }
                    }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), 20 * 11);

                    new BukkitRunnable(){
                        public void run(){
                            if(!isRain){
                                e.getPlayer().getWorld().setStorm(false);
                            }
                            e.getPlayer().getWorld().playSound(bkLoc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 65, 1);
                            e.getPlayer().getWorld().spawnParticle(Particle.CLOUD, bkLoc, 65, 0.25, 2, 0.25, 0.05);
                            CustomEntities.WitchMerchant(Objects.requireNonNull(bkLoc.getWorld()), bkLoc);
                        }
                    }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), 20 * 10);
                }
            }
            itemChecker.remove();
        }
    }
}

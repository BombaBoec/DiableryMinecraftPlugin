package com.bozheday.diablery.spells;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Candle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class Incendia implements Listener {
    @EventHandler
    public void onPlayerSpellIncendia(PlayerChatEvent chatEvent){
        Block targetBlock = chatEvent.getPlayer().getTargetBlock(null, 10);
        Location tbLoc = new Location(chatEvent.getPlayer().getWorld(), targetBlock.getX(), targetBlock.getY() + 1, targetBlock.getZ());
        Location ptLoc = new Location(chatEvent.getPlayer().getWorld(), targetBlock.getX() + 0.5, targetBlock.getY(), targetBlock.getZ() + 0.5);

        if(chatEvent.getMessage().equals("Incendia")){
            if(getEntitys(chatEvent.getPlayer()).size() == 0){
                if(!targetBlock.getType().equals(Material.AIR) && tbLoc.getBlock().getType().equals(Material.AIR)){
                    tbLoc.getBlock().setType(Material.FIRE);
                    chatEvent.getPlayer().getWorld().spawnParticle(Particle.LAVA, ptLoc, 50, 0.5, 0.5, 0.5);
                }
                if(targetBlock.getType().equals(Material.CANDLE) || targetBlock.getType().equals(Material.BLACK_CANDLE) || targetBlock.getType().equals(Material.BLUE_CANDLE) || targetBlock.getType().equals(Material.BROWN_CANDLE) || targetBlock.getType().equals(Material.CYAN_CANDLE) || targetBlock.getType().equals(Material.GRAY_CANDLE) || targetBlock.getType().equals(Material.GREEN_CANDLE) || targetBlock.getType().equals(Material.LIGHT_BLUE_CANDLE) || targetBlock.getType().equals(Material.LIGHT_GRAY_CANDLE) || targetBlock.getType().equals(Material.LIME_CANDLE) || targetBlock.getType().equals(Material.MAGENTA_CANDLE) || targetBlock.getType().equals(Material.ORANGE_CANDLE) || targetBlock.getType().equals(Material.PINK_CANDLE) || targetBlock.getType().equals(Material.PURPLE_CANDLE) || targetBlock.getType().equals(Material.RED_CANDLE) || targetBlock.getType().equals(Material.WHITE_CANDLE) || targetBlock.getType().equals(Material.YELLOW_CANDLE)){
                    Candle candle = (Candle) targetBlock.getBlockData();
                    candle.setLit(true);
                    targetBlock.setBlockData(candle);
                    chatEvent.getPlayer().getWorld().spawnParticle(Particle.LAVA, ptLoc, 50, 0.5, 0.5, 0.5);
                }
            }

            for(Entity e: getEntitys(chatEvent.getPlayer())){
                e.setFireTicks(350);
                chatEvent.getPlayer().getWorld().spawnParticle(Particle.LAVA, e.getLocation(), 50, 0.5, 0.5, 0.5);
            }

            chatEvent.getPlayer().getWorld().playSound(chatEvent.getPlayer().getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 35, 1);

            chatEvent.setCancelled(true);
        }
    }

    private boolean getLookingAt(Player player, LivingEntity e)
    {
        Location eye = player.getEyeLocation();
        Vector toEntity = e.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());

        return dot > 0.99D;
    }
    private List<Entity> getEntitys(Player player){
        List<Entity> entitys = new ArrayList<Entity>();
        for(Entity e : player.getNearbyEntities(10, 10, 10)){
            if(e instanceof LivingEntity){
                if(getLookingAt(player, (LivingEntity) e)){
                    entitys.add(e);
                }
            }
        }

        return entitys;
    }
}

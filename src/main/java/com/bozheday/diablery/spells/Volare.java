package com.bozheday.diablery.spells;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Hangable;
import org.bukkit.block.data.Rail;
import org.bukkit.block.data.type.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class Volare implements Listener {
    public static Map<Player, FallingBlock> PlayerCastedVolare = new HashMap<>();
    public static Map<Player, PolarBear> PlayerPolarBear = new HashMap<>();
    public static Map<Player, LivingEntity> PlayerCastedVolareOnLivingEntity = new HashMap<>();

    @EventHandler
    public void onSpellVolare(PlayerChatEvent e){
        Set<Material> transparentList = new HashSet<>();
        transparentList.add(Material.AIR);
        Block targetBlock = e.getPlayer().getTargetBlock(transparentList, 3);
        Location tgBlockLoc = new Location(targetBlock.getWorld(), targetBlock.getLocation().getX(), targetBlock.getLocation().getY() + 0.5, targetBlock.getLocation().getZ());
        List<Entity> entitiesLookingAt = getEntitys(e.getPlayer());

        if(e.getMessage().equals("Volare")){
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 35, 1);
            if(!PlayerCastedVolare.containsKey(e.getPlayer())){
                if(!PlayerCastedVolareOnLivingEntity.containsKey(e.getPlayer())){
                    if(entitiesLookingAt.size() > 0){
                        LivingEntity entity = (LivingEntity) entitiesLookingAt.get(0);
                        if(!(entity instanceof Player)){
                            PlayerCastedVolareOnLivingEntity.put(e.getPlayer(), entity);

                            BukkitRunnable runnable2 = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    e.getPlayer().getWorld().spawnParticle(Particle.END_ROD, entity.getLocation(), 10, 0.25, 0.25, 0.25, 0.01);
                                }
                            };

                            runnable2.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), 0, 10);

                            BukkitRunnable runnable1 = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    setEntityToPlayerLook(e.getPlayer(), entity, 3, 5);

                                    if(entity.isDead()){
                                        PlayerCastedVolareOnLivingEntity.remove(e.getPlayer());
                                        runnable2.cancel();
                                        this.cancel();
                                    }
                                    if(!PlayerCastedVolareOnLivingEntity.containsKey(e.getPlayer())){
                                        e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 35, 1);
                                        setEntityToPlayerLook(e.getPlayer(), entity, 20, 20);
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 3, false, false));
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 3, false, false));
                                        runnable2.cancel();
                                        this.cancel();
                                    }
                                }
                            };

                            runnable1.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), 0, 0);

                            new BukkitRunnable(){
                                @Override
                                public void run() {
                                    runnable2.cancel();
                                    runnable1.cancel();
                                    PlayerCastedVolareOnLivingEntity.remove(e.getPlayer());
                                }
                            }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), 20 * 10);

                            return;
                        }
                    }
                }

                if(isLevitableBlock(targetBlock)){
                    FallingBlock fallingBlock = targetBlock.getWorld().spawnFallingBlock(tgBlockLoc, targetBlock.getType(), (byte) 0);
                    fallingBlock.setCustomName("damageBlock_diablery");
                    fallingBlock.setCustomNameVisible(false);
                    fallingBlock.setGravity(false);
                    PolarBear polarBear = (PolarBear) targetBlock.getWorld().spawnEntity(tgBlockLoc, EntityType.POLAR_BEAR);
                    polarBear.setInvisible(true);
                    polarBear.setSilent(true);
                    polarBear.setCollidable(false);
                    Objects.requireNonNull(polarBear.getAttribute(Attribute.GENERIC_FOLLOW_RANGE)).setBaseValue(0);

                    PlayerCastedVolare.put(e.getPlayer(), fallingBlock);
                    PlayerPolarBear.put(e.getPlayer(), polarBear);

                    BukkitRunnable runnable3 = new BukkitRunnable() {
                        @Override
                        public void run() {
                            e.getPlayer().getWorld().spawnParticle(Particle.END_ROD, fallingBlock.getLocation(), 10, 0.25, 0.25, 0.25, 0.01);
                        }
                    };

                    runnable3.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), 0, 10);

                    BukkitRunnable runnable2 = new BukkitRunnable() {
                        @Override
                        public void run() {
                            List<Entity> nearEntities = fallingBlock.getNearbyEntities(0.25, 0.25, 0.25);

                            for(Entity entity : nearEntities){
                                if(entity instanceof LivingEntity){
                                    if(!fallingBlock.isDead()){
                                        ((LivingEntity) entity).damage(2);
                                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 5, 3, false, false));
                                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5, 3, false, false));

                                        this.cancel();
                                    }
                                }
                            }
                        }
                    };

                    BukkitRunnable runnable1 = new BukkitRunnable() {
                        @Override
                        public void run() {
                            setEntityToPlayerLook(e.getPlayer(), fallingBlock, 3, 5);
                            setEntityToPlayerLook(e.getPlayer(), polarBear, 2.5, 5);

                            if(fallingBlock.isDead()){
                                if(PlayerPolarBear.containsKey(e.getPlayer())){
                                    PlayerPolarBear.get(e.getPlayer()).remove();
                                    PlayerPolarBear.remove(e.getPlayer());
                                }
                                PlayerCastedVolare.remove(e.getPlayer());
                            }

                            if(polarBear.isDead()){
                                e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 35, 1);
                                fallingBlock.setGravity(true);
                                setEntityToPlayerLook(e.getPlayer(), fallingBlock, 10, 10);
                                PlayerCastedVolare.remove(e.getPlayer());
                                PlayerPolarBear.remove(e.getPlayer());
                                runnable2.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), 0, 0);

                                runnable3.cancel();
                                this.cancel();
                            }
                        }
                    };

                    runnable1.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), 0, 0);

                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            runnable3.cancel();
                            runnable1.cancel();
                            if(!fallingBlock.isDead()){
                                fallingBlock.setGravity(true);
                                PlayerCastedVolare.remove(e.getPlayer());
                            }
                            if(!polarBear.isDead()){
                                polarBear.remove();
                                PlayerPolarBear.remove(e.getPlayer());
                            }
                        }
                    }.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), 20 * 15);

                    targetBlock.setType(Material.AIR);

                    e.setCancelled(true);
                }
            }
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        if(PlayerCastedVolare.containsKey(e.getPlayer())){
            if(PlayerPolarBear.containsKey(e.getPlayer())){
                if(e.getAction().equals(Action.LEFT_CLICK_AIR)){
                    PlayerPolarBear.get(e.getPlayer()).remove();
                    PlayerPolarBear.remove(e.getPlayer());
                    PlayerCastedVolare.remove(e.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerHitPolarBear(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player){
            Player player = (Player) e.getDamager();
            if(e.getEntity() instanceof PolarBear){
                PolarBear polarBear = (PolarBear) e.getEntity();
                if(PlayerPolarBear.containsKey(player)){
                    if(PlayerPolarBear.get(player).equals(polarBear)){
                        PlayerPolarBear.get(player).remove();
                        PlayerPolarBear.remove(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerHitVolareEntity(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player){
            Player player = (Player) e.getDamager();
            if(e.getEntity() instanceof LivingEntity){
                LivingEntity livingEntity = (LivingEntity) e.getEntity();
                if(PlayerCastedVolareOnLivingEntity.containsKey(player)){
                    if(PlayerCastedVolareOnLivingEntity.get(player).equals(livingEntity)){
                        PlayerCastedVolareOnLivingEntity.remove(player);
                    }
                }
            }
        }
    }

    public void setEntityToPlayerLook(Player player, Entity entity, double directionModifier, double speed){
        Vector dir = player.getEyeLocation().getDirection().normalize();
        Location loc =  new Location(player.getWorld(), player.getEyeLocation().getX(), player.getEyeLocation().getY() - 0.5, player.getEyeLocation().getZ());
        double x = dir.getX() * directionModifier;
        double y = dir.getY() * directionModifier;
        double z = dir.getZ() * directionModifier;
        loc.add(x, y, z);

        double xVec = (loc.getX() - entity.getLocation().getX()) / speed;
        double yVec = (loc.getY() - entity.getLocation().getY()) / speed;
        double zVec = (loc.getZ() - entity.getLocation().getZ()) / speed;

        entity.setVelocity(new Vector(xVec, yVec, zVec));
    }

    public boolean isLevitableBlock(Block block){
        BlockData blockData = block.getBlockData();

        boolean isLevitable = (blockData instanceof Candle || blockData instanceof AmethystCluster || blockData instanceof Bamboo || blockData instanceof Bed || blockData instanceof CaveVines || blockData instanceof CaveVinesPlant || blockData instanceof Chain || blockData instanceof Chest || blockData instanceof Cocoa || blockData instanceof RedstoneWire || blockData instanceof DecoratedPot || blockData instanceof Door || blockData instanceof EnderChest || blockData instanceof Farmland || blockData instanceof Fence || blockData instanceof Furnace || blockData instanceof Gate || blockData instanceof GlassPane || blockData instanceof GlowLichen || blockData instanceof Hangable || blockData instanceof Hopper || blockData instanceof Rail || blockData instanceof Sapling || blockData instanceof Sign || blockData instanceof TrapDoor || block.getType().equals(Material.WATER) || block.getType().equals(Material.WHEAT) || block.getType().equals(Material.CARROTS) || block.getType().equals(Material.BEETROOTS) || block.getType().equals(Material.POTATOES) || blockData instanceof EndPortalFrame || block.getType().equals(Material.NETHER_PORTAL) || block.getType().equals(Material.BROWN_MUSHROOM) || block.getType().equals(Material.RED_MUSHROOM) || block.getType().equals(Material.CRIMSON_FUNGUS) || block.getType().equals(Material.WARPED_FUNGUS) || block.getType().equals(Material.GRASS) || block.getType().equals(Material.FERN) || block.getType().equals(Material.DEAD_BUSH) || block.getType().equals(Material.DANDELION) || block.getType().equals(Material.POPPY) || block.getType().equals(Material.BLUE_ORCHID) || block.getType().equals(Material.ALLIUM) || block.getType().equals(Material.AZURE_BLUET) || block.getType().equals(Material.RED_TULIP) || block.getType().equals(Material.ORANGE_TULIP) || block.getType().equals(Material.WHITE_TULIP) || block.getType().equals(Material.PINK_TULIP) || block.getType().equals(Material.OXEYE_DAISY) || block.getType().equals(Material.CORNFLOWER) || block.getType().equals(Material.LILY_OF_THE_VALLEY) || block.getType().equals(Material.TORCHFLOWER) || block.getType().equals(Material.WITHER_ROSE) || block.getType().equals(Material.PINK_PETALS) || block.getType().equals(Material.SPORE_BLOSSOM) || block.getType().equals(Material.CRIMSON_ROOTS) || block.getType().equals(Material.WARPED_ROOTS) || block.getType().equals(Material.NETHER_SPROUTS) || block.getType().equals(Material.TALL_GRASS) || block.getType().equals(Material.LARGE_FERN) || block.getType().equals(Material.SUNFLOWER) || block.getType().equals(Material.LILAC) || block.getType().equals(Material.ROSE_BUSH) || block.getType().equals(Material.PEONY) || block.getType().equals(Material.BIG_DRIPLEAF) || block.getType().equals(Material.SMALL_DRIPLEAF) || block.getType().equals(Material.CHORUS_PLANT) || block.getType().equals(Material.CHORUS_FLOWER) || block.getType().equals(Material.LILY_PAD) || block.getType().equals(Material.SEAGRASS) || block.getType().equals(Material.SEA_PICKLE) || block.getType().equals(Material.TALL_SEAGRASS) || block.getType().equals(Material.COBWEB) || block.getType().equals(Material.SCULK_VEIN) || blockData instanceof  HangingSign || block.getType().equals(Material.LAVA) || block.getType().equals(Material.SNOW) || block.getType().equals(Material.AIR));

        return !isLevitable;
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
        for(Entity e : player.getNearbyEntities(3, 3, 3)){
            if(e instanceof LivingEntity){
                if(getLookingAt(player, (LivingEntity) e)){
                    entitys.add(e);
                }
            }
        }

        return entitys;
    }
}

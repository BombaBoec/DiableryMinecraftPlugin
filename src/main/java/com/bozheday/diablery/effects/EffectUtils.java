package com.bozheday.diablery.effects;

import org.bukkit.*;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

import static java.lang.Math.*;

public class EffectUtils {
    public static void DarkCircleUpEffect(Location location, PlayerEvent e){
        new BukkitRunnable(){
            Location loc = location;
            double t = 0;
            double r = 1;
            Particle.DustTransition dustTransition = new Particle.DustTransition(Color.BLACK, Color.PURPLE, 1.5f);
            public void run(){
                t = t + Math.PI/4;
                double x = r*cos(t);
                double y = 0.25*t;
                double z = r*sin(t);
                loc.add(x,y,z);
                e.getPlayer().getWorld().spawnParticle(Particle.REDSTONE, loc, 50, dustTransition);
                loc.subtract(x,y,z);
                if(t > Math.PI*3){
                    this.cancel();
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), 0, 20);
    }

    public static void DownCircleDrawingEffectColorized(double radius, double circleDivision, Color particleColor, float particleSize, Location location, int particleCount, double particleExtra, long runPeriod, long delay){
        new BukkitRunnable(){
            double t = 0;
            Location loc = location;
            Particle.DustOptions dustOptions = new Particle.DustOptions(particleColor, particleSize);

            public void run(){
                t = t + Math.PI/circleDivision;
                double x = radius * cos(t);
                double z = radius * sin(t);
                loc.add(x, 0, z);
                loc.getWorld().spawnParticle(Particle.REDSTONE, loc, particleCount, 0,0,0, particleExtra, dustOptions);
                loc.subtract(x,0, z);

                for(double i = Math.PI/circleDivision; i < t; i = i + Math.PI/circleDivision){
                    x = radius * cos(i);
                    z = radius * sin(i);
                    loc.add(x, 0, z);
                    loc.getWorld().spawnParticle(Particle.REDSTONE, loc, particleCount, 0,0,0, particleExtra, dustOptions);
                    loc.subtract(x,0, z);
                }

                if(t > Math.PI*2){
                    this.cancel();
                }
            }
        }.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), delay, runPeriod);
    }

    public static void DownCircleEffectColorized(double radius, double circleDivision, Color particleColor, float particleSize, Location location, int particleCount, double particleExtra, long delay, long runPeriod, long runCancelDelay){
        BukkitRunnable runnable1 = new BukkitRunnable(){
            Location loc = location;
            Particle.DustOptions dustOptions = new Particle.DustOptions(particleColor, particleSize);

            public void run(){
                for(double t = 0; t < Math.PI*2; t = t + Math.PI/circleDivision){
                    double x = radius * cos(t);
                    double z = radius * sin(t);
                    loc.add(x, 0, z);
                    loc.getWorld().spawnParticle(Particle.REDSTONE, loc, particleCount, 0,0,0, particleExtra, dustOptions);
                    loc.subtract(x, 0, z);
                }
            }
        };

        BukkitRunnable runnable2 = new BukkitRunnable() {
            public void run() {
                runnable1.cancel();
            }
        };

        runnable1.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), delay, runPeriod);
        runnable2.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), runCancelDelay);
    }

    public static void DownCircleEffectColorizedNoneRun(double radius, double circleDivision, Color particleColor, float particleSize, Location location, int particleCount, double particleExtra){
        Location loc = location;
        Particle.DustOptions dustOptions = new Particle.DustOptions(particleColor, particleSize);

        for(double t = 0; t < Math.PI*2; t = t + Math.PI/circleDivision){
            double x = radius * cos(t);
            double z = radius * sin(t);
            loc.add(x, 0, z);
            loc.getWorld().spawnParticle(Particle.REDSTONE, loc, particleCount, 0,0,0, particleExtra, dustOptions);
            loc.subtract(x, 0, z);
        }
    }

    public static void DownPentagrammEffectColorized(Location location, Color particleColor, float particleSize, double radius, int particleCount, float particleExtra, long delay, long runPeriod, long runCancelDelay, double facingModificator){
        BukkitRunnable runnable1 = new BukkitRunnable() {
            Location loc = location;
            double modificator = PI/12;

            public void run() {
                for(double t = 0; t <= Math.PI*2; t = t + Math.PI/4){
                    double x = radius * cos(t + facingModificator);
                    double z = radius * sin(t + facingModificator);

                    if(t == Math.PI/2){
                        Location p1 = new Location(loc.getWorld(), loc.getX() + radius*cos(t + facingModificator), loc.getY(), loc.getZ() + radius*sin(t + facingModificator));
                        Location p2 = new Location(loc.getWorld(), loc.getX() + radius*cos(5*Math.PI/4 + facingModificator), loc.getY(), loc.getZ() + radius*sin(5*Math.PI/4 + facingModificator));
                        Location p3 = new Location(loc.getWorld(), loc.getX() + radius*cos(7*Math.PI/4 + facingModificator), loc.getY(), loc.getZ() + radius*sin(7*Math.PI/4 + facingModificator));

                        EffectUtils.LineEffectColorized(p1, p2, particleColor, particleSize, particleCount, particleExtra, 15);
                        EffectUtils.LineEffectColorized(p1, p3, particleColor, particleSize, particleCount, particleExtra, 15);
                    }
                    if(t == Math.PI){
                        Location p1 = new Location(loc.getWorld(), loc.getX() + radius*cos(t - modificator + facingModificator), loc.getY(), loc.getZ() + radius*sin(t - modificator + facingModificator));
                        Location p2 = new Location(loc.getWorld(), loc.getX() + radius*cos(7*Math.PI/4 + facingModificator), loc.getY(), loc.getZ() + radius*sin(7*Math.PI/4 + facingModificator));
                        Location p3 = new Location(loc.getWorld(), loc.getX() + radius*cos(2*Math.PI + modificator + facingModificator), loc.getY(), loc.getZ() + radius*sin(2*Math.PI + modificator + facingModificator));

                        EffectUtils.LineEffectColorized(p1, p2, particleColor, particleSize, particleCount, particleExtra, 15);
                        EffectUtils.LineEffectColorized(p1, p3, particleColor, particleSize, particleCount, particleExtra, 15);
                    }
                    if(t == 5*Math.PI/4){
                        Location p1 = new Location(loc.getWorld(), loc.getX() + radius*cos(t + facingModificator), loc.getY(), loc.getZ() + radius*sin(t + facingModificator));
                        Location p2 = new Location(loc.getWorld(), loc.getX() + radius*cos(2*Math.PI + modificator + facingModificator), loc.getY(), loc.getZ() + radius*sin(2*Math.PI + modificator + facingModificator));

                        EffectUtils.LineEffectColorized(p1, p2, particleColor, particleSize, particleCount, particleExtra, 15);
                    }
                }
            }
        };

        BukkitRunnable runnable2 = new BukkitRunnable() {
            public void run() {
                runnable1.cancel();
            }
        };

        runnable1.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), delay, runPeriod);
        runnable2.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), runCancelDelay);
    }

    public static void LineEffectColorized(Location point1, Location point2, Color particleColor, float particleSize, int particleCount, float particleExtra, int particleCountOnLine){
        Particle.DustOptions dustOptions = new Particle.DustOptions(particleColor, particleSize);
        double distX = (point2.getX() - point1.getX()) / particleCountOnLine;
        double distY = (point2.getY() - point1.getY()) / particleCountOnLine;
        double distZ = (point2.getZ() - point1.getZ()) / particleCountOnLine;

        for(double i = 0; i < particleCountOnLine; i++){
            double x = distX * i;
            double y = distY * i;
            double z = distZ * i;

            point1.add(x, y, z);
            point1.getWorld().spawnParticle(Particle.REDSTONE, point1, particleCount, 0,0,0, particleExtra, dustOptions);
            point1.subtract(x, y, z);
        }
    }

    public static void LineEffectDrawingColorized(Location point1, Location point2, Color particleColor, float particleSize, int particleCount, float particleExtra, int particleCountOnLine, long delay, long runPeriod, long runCancelDelay){
        Particle.DustOptions dustOptions = new Particle.DustOptions(particleColor, particleSize);
        double distX = (point2.getX() - point1.getX()) / particleCountOnLine;
        double distY = (point2.getY() - point1.getY()) / particleCountOnLine;
        double distZ = (point2.getZ() - point1.getZ()) / particleCountOnLine;

        BukkitRunnable runnable1 = new BukkitRunnable() {
            double i = 0;

            @Override
            public void run() {
                i++;
                double x = distX * i;
                double y = distY * i;
                double z = distZ * i;

                point1.add(x, y, z);
                point1.getWorld().spawnParticle(Particle.REDSTONE, point1, particleCount, 0,0,0, particleExtra, dustOptions);
                point1.subtract(x, y, z);
            }
        };
        BukkitRunnable runnable2 = new BukkitRunnable() {
            @Override
            public void run() {
                runnable1.cancel();
            }
        };
        runnable1.runTaskTimer(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), delay, runPeriod);
        runnable2.runTaskLater(Objects.requireNonNull(Bukkit.getPluginManager().getPlugin("Diablery")), runCancelDelay);
    }
}

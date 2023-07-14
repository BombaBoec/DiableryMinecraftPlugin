package com.bozheday.diablery;

import com.bozheday.diablery.entities.CustomEntities;
import com.bozheday.diablery.items.UpdatedEndermanEye;
import com.bozheday.diablery.items.WitchBlood;
import com.bozheday.diablery.spells.Incendia;
import com.bozheday.diablery.spells.Volare;
import com.bozheday.diablery.spells.Torgu;
import org.bukkit.plugin.java.JavaPlugin;

public final class Diablery extends JavaPlugin {
    public WitchBlood witchBlood = new WitchBlood();
    public UpdatedEndermanEye updatedEndermanEye = new UpdatedEndermanEye();

    @Override
    public void onEnable() {
        updatedEndermanEye.UpdatedEndermanEyeItem();
        witchBlood.WitchBloodItem();

        getServer().getPluginManager().registerEvents(new Incendia(), this);
        getServer().getPluginManager().registerEvents(new WitchBlood(), this);
        getServer().getPluginManager().registerEvents(new UpdatedEndermanEye(), this);
        getServer().getPluginManager().registerEvents(new Torgu(), this);
        getServer().getPluginManager().registerEvents(new CustomEntities(), this);
        getServer().getPluginManager().registerEvents(new Volare(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

package me.squ.ppt;

import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.configuration.file.FileConfiguration;


import java.io.File;


public class PPT extends JavaPlugin {


    private File imagesFolder;
    private int currentSlide = 1;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        ensureImagesFolder();
        getCommand("placeimage").setExecutor(new PlaceImageCommand(this));
        getCommand("placeimage").setTabCompleter(new PlaceImageCommand(this));
        getCommand("ppt").setExecutor(new PptCommand(this));
        getCommand("ppt").setTabCompleter(new PptCommand(this));


        getServer().getPluginManager().registerEvents(new me.squ.ppt.PptToolListener(this), this);


        getLogger().info("PPT plugin enabled.");
    }


    @Override
    public void onDisable() {
        getLogger().info("PPT plugin disabled.");
    }


    public FileConfiguration cfg() {
        return getConfig();
    }


    private void ensureImagesFolder() {
        imagesFolder = new File(getDataFolder(), getConfig().getString("images-folder", "images"));
        if (!imagesFolder.exists()) {
            if (imagesFolder.mkdirs()) {
                getLogger().info("Created images folder: " + imagesFolder.getAbsolutePath());
            }
        }
    }


    public File getImagesFolder() {
        return imagesFolder;
    }
    public int getCurrentSlide() {
        return currentSlide;
    }


    public void setCurrentSlide(int slide) {
        this.currentSlide = slide;
    }
}
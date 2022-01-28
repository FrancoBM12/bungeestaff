package com.francobm.bungeestaff.files;

import com.francobm.bungeestaff.BungeeStaff;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public class FileCreator{
    private final BungeeStaff plugin;
    private final String fileName;
    private final File file;
    private Configuration configuration;

    public FileCreator(BungeeStaff plugin, String fileName, String fileExtension, File folder){
        this.plugin = plugin;
        this.fileName = fileName + (fileName.endsWith(fileExtension) ? "" : fileExtension);
        if(!folder.exists()){
            folder.mkdir();
        }
        file = new File(folder, this.fileName);
        createFile();
    }

    public FileCreator(BungeeStaff plugin, String fileName, String fileExtension) {
        this(plugin, fileName, fileExtension, plugin.getDataFolder());
    }

    public FileCreator(BungeeStaff plugin, String fileName){
        this(plugin, fileName, ".yml");
    }

    private void createFile(){
        try {
            if (!file.exists()) {
                if(file.createNewFile()) {
                    try (InputStream in = plugin.getResourceAsStream(fileName);
                    OutputStream outputStream = new FileOutputStream(file)) {
                        ByteStreams.copy(in,outputStream);
                    }
                }
            }
        }catch (IOException exception){
            exception.printStackTrace();
        }
        this.load();
        this.save();
    }

    private void load(){
        try{
            if(file.createNewFile()){
                plugin.getLogger().info("File Configuration " + fileName + " is created!");
            }
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            plugin.getLogger().info("File Configuration " + fileName + " is loaded!");
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public void reload(){
        load();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void save(){
        try{
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
        }catch (IOException exception){
            exception.printStackTrace();
        }
    }
}

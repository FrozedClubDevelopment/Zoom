package me.ryzeon.core.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigFile extends YamlConfiguration {
    private File file;

    private final JavaPlugin plugin;

    private final String name;

    public File getFile() {
        return this.file;
    }

    public ConfigFile(JavaPlugin plugin, String name) {
        this.file = new File(plugin.getDataFolder(), name);
        this.plugin = plugin;
        this.name = name;
        if (!this.file.exists())
            plugin.saveResource(name, false);
        try {
            load(this.file);
        } catch (IOException |org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        this.file = new File(this.plugin.getDataFolder(), this.name);
        if (!this.file.exists())
            this.plugin.saveResource(this.name, false);
        try {
            load(this.file);
        } catch (IOException|org.bukkit.configuration.InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getInt(String path) {
        return getInt(path, 0);
    }

    public double getDouble(String path) {
        return getDouble(path, 0.0D);
    }

    public boolean getBoolean(String path) {
        return getBoolean(path, false);
    }

    public String getString(String path, boolean ignored) {
        return getString(path, null);
    }

    public String getString(String path) {
        return Color.translate(getString(path, "&bString at path &7'&3" + path + "&7' &bnot found."));
    }

    public List<String> getStringList(String path) {
        return (List<String>)super.getStringList(path).stream().map(Color::translate).collect(Collectors.toList());
    }

    public List<String> getStringList(String path, boolean ignored) {
        if (!contains(path))
            return null;
        return (List<String>)super.getStringList(path).stream().map(Color::translate).collect(Collectors.toList());
    }
}


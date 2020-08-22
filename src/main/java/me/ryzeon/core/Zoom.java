package me.ryzeon.core;

import lombok.Getter;
import lombok.Setter;
import me.ryzeon.core.command.PingCommand;
import me.ryzeon.core.command.gamemode.GamemodeAdventure;
import me.ryzeon.core.command.gamemode.GamemodeCommand;
import me.ryzeon.core.command.gamemode.GamemodeCreative;

import me.ryzeon.core.command.gamemode.GamemodeSurvival;
import me.ryzeon.core.utils.ConfigFile;
import me.ryzeon.core.utils.command.CommandFramework;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter
public final class Zoom extends JavaPlugin {
    @Getter
    private static Zoom instance;

    private CommandFramework commandFramework;

    private ConfigFile messagesconfig;

    private ConfigFile databaseconfig;

    @Override
    public void onEnable() {
        instance = this;
        commandFramework = new CommandFramework(this);
        this.messagesconfig = new ConfigFile(this,"messages.yml");
        this.databaseconfig = new ConfigFile(this,"database.yml");
        Bukkit.getConsoleSender().sendMessage("§e+-------------------+");
        Bukkit.getConsoleSender().sendMessage("§bEssecials");
        Bukkit.getConsoleSender().sendMessage("§eEn test weon");
        loadCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    // Load Commands
    private void loadCommands(){
        commandFramework.registerCommands(new GamemodeCreative());
        commandFramework.registerCommands(new GamemodeSurvival());
        commandFramework.registerCommands(new GamemodeAdventure());
        commandFramework.registerCommands(new GamemodeCommand());
        commandFramework.registerCommands(new PingCommand());
    }
}

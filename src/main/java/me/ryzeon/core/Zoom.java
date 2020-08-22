package me.ryzeon.core;

import lombok.Getter;
import lombok.Setter;
import me.ryzeon.core.manager.database.MongoManager;
import me.ryzeon.core.utils.RegisterHandler;
import me.ryzeon.core.utils.command.CommandFramework;
import me.ryzeon.core.utils.config.FileConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter
public final class Zoom extends JavaPlugin {
    @Getter
    private static Zoom instance;

    private CommandFramework commandFramework;

    private FileConfig messagesconfig;

    private FileConfig databaseconfig;

    private FileConfig commandsconfig;

    private MongoManager mongoManager;

    private String disablemsg = "null";

    @Override
    public void onEnable() {
        instance = this;
        commandFramework = new CommandFramework(this);
        this.messagesconfig = new FileConfig(this,"messages.yml");
        this.databaseconfig = new FileConfig(this,"database.yml");
        this.commandsconfig = new FileConfig(this,"commands.yml");
        this.mongoManager = new MongoManager();
        mongoManager.connect();
        if (!mongoManager.isConnect()) return;
        Bukkit.getConsoleSender().sendMessage("§7-----------------------------");
        Bukkit.getConsoleSender().sendMessage("§6Zoom Core");
        Bukkit.getConsoleSender().sendMessage("§7|-");
        Bukkit.getConsoleSender().sendMessage("§6Author§f: §aRyzeon");
        Bukkit.getConsoleSender().sendMessage("§6Version§f: §av"+getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§7|-");
        Bukkit.getConsoleSender().sendMessage("§aDatabase§f: §a"+mongoManager.isConnect());
        Bukkit.getConsoleSender().sendMessage("§7|-");
        Bukkit.getConsoleSender().sendMessage("§6Dou you want support?");
        Bukkit.getConsoleSender().sendMessage("§fJoin to discord https://discord.gg/FXGQq96");
        Bukkit.getConsoleSender().sendMessage("§7-----------------------------");
        loadCommands();
    }

    @Override
    public void onDisable() {
        shutdownmsg();
    }

    // Load Commands
    private void loadCommands(){
        RegisterHandler.loadCommandsFromPackage(this,"me.ryzeon.core.command");
        // To load commands in file to view commands with permisison && usage
        commandFramework.loadCommandsInFile();
    }
    public void shutdownmsg(){
        if (disablemsg.equalsIgnoreCase("Error in mongodb")){
            Bukkit.getConsoleSender().sendMessage("§7-----------------------------");
            Bukkit.getConsoleSender().sendMessage("§6Zoom Core §7| §cDisabling");
            Bukkit.getConsoleSender().sendMessage("§6Please check your database.yml");
            Bukkit.getConsoleSender().sendMessage("§6Dou you want support?");
            Bukkit.getConsoleSender().sendMessage("§fJoin to discord https://discord.gg/FXGQq96");
            Bukkit.getConsoleSender().sendMessage("§7-----------------------------");
        } else {
            Bukkit.getConsoleSender().sendMessage("§7-----------------------------");
            Bukkit.getConsoleSender().sendMessage("§6Zoom Core §7| §cDisabling");
            Bukkit.getConsoleSender().sendMessage("§6Dou you want support?");
            Bukkit.getConsoleSender().sendMessage("§fJoin to discord https://discord.gg/FXGQq96");
            Bukkit.getConsoleSender().sendMessage("§7-----------------------------");
        }
    }
}

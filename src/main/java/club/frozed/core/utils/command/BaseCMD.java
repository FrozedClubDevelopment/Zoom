package club.frozed.zoom.utils.command;

import club.frozed.zoom.ZoomPlugin;

public abstract class BaseCMD {
    public ZoomPlugin plugin = ZoomPlugin.getInstance();

    public BaseCMD(){
        this.plugin.getCommandFramework().registerCommands(this);
    }
    
    public abstract void onCommand(CommandArgs cmd);
}

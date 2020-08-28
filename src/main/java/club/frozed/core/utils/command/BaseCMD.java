package club.frozed.core.utils.command;

import club.frozed.core.Zoom;

public abstract class BaseCMD {
    public Zoom plugin = Zoom.getInstance();

    public BaseCMD(){
        this.plugin.getCommandFramework().registerCommands(this);
    }
    
    public abstract void onCommand(CommandArgs cmd);
}

package me.ryzeon.core.command;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.Utils;
import me.ryzeon.core.utils.command.*;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatManagerCommand extends BaseCMD {
    @Completer(name = "chat",aliases = {"chatmanager"})
    public List<String> ChatManagerComplete(CommandArgs args) {
        List<String> list = new ArrayList<String>();
        list.add("clear");
        return list;
    }
    @Command(name = "chat",permission = "core.manager.chat",aliases = {"chatmanager"},inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesconfig(),"chat");
        if (args.length == 0){
            p.sendMessage(Color.LINE);
            p.sendMessage("§eUsage /chat <clear>");
            p.sendMessage(Color.LINE);
            return;
        }
        switch (args[0]){
            case "clear":
                for (int i = 0; i < 120; i++){
                    Utils.sendAllMsg("");
                }
                Utils.sendAllMsg(Utils.format(messages.getString("clear"),new Object[]{ p.getDisplayName() }));
                break;
            default:
                break;
        }
    }
}

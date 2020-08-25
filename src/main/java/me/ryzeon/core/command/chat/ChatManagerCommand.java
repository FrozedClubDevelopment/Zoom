package me.ryzeon.core.command.chat;

import me.ryzeon.core.Zoom;
import me.ryzeon.core.manager.chat.ChatManager;
import me.ryzeon.core.utils.Color;
import me.ryzeon.core.utils.NumberUtils;
import me.ryzeon.core.utils.Utils;
import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;
import me.ryzeon.core.utils.command.Completer;
import me.ryzeon.core.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatManagerCommand extends BaseCMD {
    @Completer(name = "chat",aliases = {"chatmanager"})
    public List<String> ChatManagerComplete(CommandArgs args) {
        List<String> list = new ArrayList<String>();
        list.add("clear");
        list.add("mute");
        list.add("unmute");
        list.add("delay");
        return list;
    }
    @Command(name = "chat", permission = "core.manager", aliases = {"chatmanager"}, inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesconfig(), "chat");
        ChatManager chatManager = Zoom.getInstance().getChatManager();
        if (args.length == 0) {
            p.sendMessage(Color.LINE);
            p.sendMessage("§e/chat clear");
            p.sendMessage("§e/chat mute");
            p.sendMessage("§e/chat unmute");
            p.sendMessage("§e/chat delay <amount>");
            p.sendMessage(Color.LINE);
            return;
        }
        switch (args[0]) {
            case "clear":
                for (int i = 0; i < 120; i++) {
                    Utils.sendAllMsg("");
                }
                Utils.sendAllMsg(Color.translate(messages.getString("clear").replace("<target>", p.getDisplayName())));
                break;
            case "mute":
                if (!chatManager.isMute()) {
                    chatManager.setMute(true);
                    Bukkit.broadcastMessage(Color.translate(messages.getString("mute").replace("<player>", p.getName())));
                } else {
                    p.sendMessage(Color.translate(messages.getString("already").replace("<label>", "muted")));
                }
                chatManager.save();
                break;
            case "unmute":
                if (chatManager.isMute()) {
                    chatManager.setMute(false);
                    Bukkit.broadcastMessage(Color.translate(messages.getString("unmute").replace("<player>", p.getName())));
                } else {
                    p.sendMessage(Color.translate(messages.getString("already").replace("<label>", "unmuted")));
                }
                chatManager.save();
                break;
            case "delay":
                if (!NumberUtils.checkNumber(args[1])) {
                    p.sendMessage("§cIt must be a number");
                    return;
                }
                chatManager.setDelay(Integer.parseInt(args[1]));
                Bukkit.broadcastMessage(Color.translate(messages.getString("delay").replace("<delay>", args[1]).replace("<player>", p.getName())));
                break;
            default:
                break;
        }
    }
}

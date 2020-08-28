package club.frozed.zoom.command.chat;

import club.frozed.zoom.ZoomPlugin;
import club.frozed.zoom.manager.chat.ChatManager;
import club.frozed.zoom.utils.Color;
import club.frozed.zoom.utils.NumberUtils;
import club.frozed.zoom.utils.Utils;
import club.frozed.zoom.utils.command.BaseCMD;
import club.frozed.zoom.utils.command.Command;
import club.frozed.zoom.utils.command.CommandArgs;
import club.frozed.zoom.utils.command.Completer;
import club.frozed.zoom.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatManagerCommand extends BaseCMD {
    @Completer(name = "chat", aliases = {"chatmanager"})

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
        ConfigCursor messages = new ConfigCursor(ZoomPlugin.getInstance().getMessagesConfig(), "NETWORK.CHAT-MANAGER");
        ChatManager chatManager = ZoomPlugin.getInstance().getChatManager();

        if (args.length == 0) {
            p.sendMessage(Color.CHAT_BAR);
            p.sendMessage("§e/chat clear");
            p.sendMessage("§e/chat mute");
            p.sendMessage("§e/chat unmute");
            p.sendMessage("§e/chat delay <amount>");
            p.sendMessage(Color.CHAT_BAR);
            return;
        }

        switch (args[0]) {
            case "clear":
                for (int i = 0; i < 120; i++) {
                    Utils.sendAllMsg("");
                }
                Utils.sendAllMsg(Color.translate(messages.getString("CLEAR").replace("<target>", p.getDisplayName())));
                break;
            case "mute":
                if (!chatManager.isMute()) {
                    chatManager.setMute(true);
                    Bukkit.broadcastMessage(Color.translate(messages.getString("MUTE").replace("<player>", p.getName())));
                } else {
                    p.sendMessage(Color.translate(messages.getString("ALREADY").replace("<label>", "muted")));
                }
                chatManager.save();
                break;
            case "unmute":
                if (chatManager.isMute()) {
                    chatManager.setMute(false);
                    Bukkit.broadcastMessage(Color.translate(messages.getString("UNMUTE").replace("<player>", p.getName())));
                } else {
                    p.sendMessage(Color.translate(messages.getString("ALREADY").replace("<label>", "unmuted")));
                }
                chatManager.save();
                break;
            case "delay":
                if (!NumberUtils.checkNumber(args[1])) {
                    p.sendMessage("§cIt must be a number");
                    return;
                }
                chatManager.setDelay(Integer.parseInt(args[1]));
                Bukkit.broadcastMessage(Color.translate(messages.getString("DELAY")
                        .replace("<delay>", args[1])
                        .replace("<player>", p.getName()))
                );
                break;
            default:
                break;
        }
    }
}

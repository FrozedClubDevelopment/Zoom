package club.frozed.core.command.chat;

import club.frozed.core.Zoom;
import club.frozed.core.manager.chat.ChatManager;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.NumberUtils;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.command.Completer;
import club.frozed.core.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChatManagerCommand extends BaseCMD {
    @Completer(name = "chat", aliases = {"chatmanager"})

    public List<String> ChatManagerComplete(CommandArgs args) {
        List<String> list = new ArrayList<>();
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
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "NETWORK.CHAT-MANAGER");
        ChatManager chatManager = Zoom.getInstance().getChatManager();

        if (args.length == 0) {
            p.sendMessage(CC.CHAT_BAR);
            p.sendMessage("§e/chat clear");
            p.sendMessage("§e/chat mute");
            p.sendMessage("§e/chat unmute");
            p.sendMessage("§e/chat delay <amount>");
            p.sendMessage(CC.CHAT_BAR);
            return;
        }

        switch (args[0]) {
            case "clear":
                for (int i = 0; i < 120; i++) {
                    Utils.sendAllMsg("");
                }
                Utils.sendAllMsg(CC.translate(messages.getString("CLEAR").replace("<player>", p.getDisplayName())));
                break;
            case "mute":
                if (!chatManager.isMute()) {
                    chatManager.setMute(true);
                    Bukkit.broadcastMessage(CC.translate(messages.getString("MUTE").replace("<player>", p.getName())));
                } else {
                    p.sendMessage(CC.translate(messages.getString("ALREADY").replace("<label>", "muted")));
                }
                chatManager.save();
                break;
            case "unmute":
                if (chatManager.isMute()) {
                    chatManager.setMute(false);
                    Bukkit.broadcastMessage(CC.translate(messages.getString("UNMUTE").replace("<player>", p.getName())));
                } else {
                    p.sendMessage(CC.translate(messages.getString("ALREADY").replace("<label>", "unmuted")));
                }
                chatManager.save();
                break;
            case "delay":
                if (!NumberUtils.checkNumber(args[1])) {
                    p.sendMessage("§cIt must be a number");
                    return;
                }
                chatManager.setDelay(Integer.parseInt(args[1]));
                Bukkit.broadcastMessage(CC.translate(messages.getString("DELAY")
                        .replace("<delay>", args[1])
                        .replace("<player>", p.getName()))
                );
                chatManager.save();
                break;
            default:
                break;
        }
    }
}

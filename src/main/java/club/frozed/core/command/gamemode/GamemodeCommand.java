package club.frozed.core.command.gamemode;

import club.frozed.core.Zoom;
import club.frozed.core.utils.Color;
import club.frozed.core.utils.command.BaseCMD;
import club.frozed.core.utils.command.Command;
import club.frozed.core.utils.command.CommandArgs;
import club.frozed.core.utils.command.Completer;
import club.frozed.core.utils.config.ConfigCursor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GamemodeCommand extends BaseCMD {

    @Completer(name = "gamemode", aliases = {"gm"})
    public List<String> gamemodeCompleter(CommandArgs args) {
        if (args.length() == 1) {
            List<String> list = new ArrayList<String>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                list.add("survival");
                list.add("creative");
                list.add("adventure");
                list.add(p.getName());
            }
            return list;
        } else if (args.length() > 1) {
            List<String> list = new ArrayList<String>();
            list.add("survival");
            list.add("creative");
            list.add("adventure");
            return list;
        }
        return null;
    }

    @Command(name = "gamemode", permission = "core.gamemode.command", inGameOnly = true, aliases = {"gm"})

    @Override
    public void onCommand(CommandArgs cmd) {
        Player p = cmd.getPlayer();
        String[] args = cmd.getArgs();
        ConfigCursor messages = new ConfigCursor(Zoom.getInstance().getMessagesConfig(), "COMMANDS.GAMEMODE-MESSAGES");
        if (args.length == 0) {
            p.sendMessage("§7§m-----------------------");
            p.sendMessage("§eUsage /gamemode <survival/creative/adventure>");
            p.sendMessage("§7§m-----------------------");
            return;
        }
        if (args.length == 1) {
            String gamemode = args[0];
            if (gamemode.equalsIgnoreCase("survival") || gamemode.equalsIgnoreCase("s") || gamemode.equalsIgnoreCase("0")) {
                if (!p.hasPermission("core.gamemode.survival")) {
                    p.sendMessage("§cYou don't have permissions");
                    return;
                }
                if (p.getGameMode().equals(GameMode.SURVIVAL)) {
                    p.sendMessage("§eYou already in gamemode survival");
                } else {
                    p.setGameMode(GameMode.SURVIVAL);
                    p.sendMessage(Color.translate(messages.getString("DEFAULT").replace("<gamemode>", "survival")));
                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 2F, 2F);
                }
            } else if (gamemode.equalsIgnoreCase("creative") || gamemode.equalsIgnoreCase("c") || gamemode.equalsIgnoreCase("1")) {
                if (!p.hasPermission("core.gamemode.creative")) {
                    p.sendMessage("§cYou don't have permissions");
                    return;
                }
                if (p.getGameMode().equals(GameMode.CREATIVE)) {
                    p.sendMessage("§eYou already in gamemode creative");
                } else {
                    p.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(Color.translate(messages.getString("DEFAULT").replace("<gamemode>", "creative")));
                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 2F, 2F);
                }
            } else if (gamemode.equalsIgnoreCase("adventure") || gamemode.equalsIgnoreCase("a") || gamemode.equalsIgnoreCase("2")) {
                if (!p.hasPermission("core.gamemode.adventure")) {
                    p.sendMessage("§cYou don't have permissions");
                    return;
                }
                if (p.getGameMode().equals(GameMode.ADVENTURE)) {
                    p.sendMessage("§eYou already in gamemode adventure");
                } else {
                    p.setGameMode(GameMode.ADVENTURE);
                    p.sendMessage(Color.translate(messages.getString("DEFAULT").replace("<gamemode>", "adventure")));
                    p.playSound(p.getLocation(), Sound.NOTE_PLING, 2F, 2F);
                }
            } else {
                p.sendMessage("§7§m-----------------------");
                p.sendMessage("§eUsage /gamemode <survival/creative/adventure>");
                p.sendMessage("§7§m-----------------------");
            }
        } else {
            if (!p.hasPermission("core.gamemode.others")) {
                p.sendMessage("§cYou don't have permissions");
                p.playSound(p.getLocation(), Sound.ITEM_BREAK, 2F, 2F);
                return;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                p.sendMessage("§cCouldn't find players");
                p.playSound(p.getLocation(), Sound.ITEM_BREAK, 2F, 2F);
                return;
            }
            if (args.length == 1) {
                p.sendMessage("§7§m-----------------------");
                p.sendMessage("§eUsage /gamemode <survival/creative/adventure>");
                p.sendMessage("§7§m-----------------------");
            }
            String gamemode = args[1];
            if (gamemode.equalsIgnoreCase("survival") || gamemode.equalsIgnoreCase("s") || gamemode.equalsIgnoreCase("0")) {
                if (target.getGameMode().equals(GameMode.SURVIVAL)) {
                    target.sendMessage(ChatColor.YELLOW + target.getName() + " gamemode already in gamemode survival");
                } else {
                    target.setGameMode(GameMode.SURVIVAL);
                    p.sendMessage(Color.translate(messages.getString("OTHER")
                            .replace("<gamemode>", "survival")
                            .replace("<target>", target.getName())));
                    target.playSound(p.getLocation(), Sound.NOTE_PLING, 2F, 2F);
                }
            } else if (gamemode.equalsIgnoreCase("creative") || gamemode.equalsIgnoreCase("c") || gamemode.equalsIgnoreCase("1")) {
                if (target.getGameMode().equals(GameMode.CREATIVE)) {
                    target.sendMessage(ChatColor.YELLOW + target.getName() + " gamemode already in gamemode creative");
                } else {
                    target.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(Color.translate(messages.getString("OTHER")
                            .replace("<gamemode>", "creative")
                            .replace("<target>", target.getName())));
                    target.playSound(p.getLocation(), Sound.NOTE_PLING, 2F, 2F);
                }
            } else if (gamemode.equalsIgnoreCase("adventure") || gamemode.equalsIgnoreCase("a") || gamemode.equalsIgnoreCase("2")) {
                if (target.getGameMode().equals(GameMode.ADVENTURE)) {
                    target.sendMessage(ChatColor.YELLOW + target.getName() + " gamemode already in gamemode adventure");
                } else {
                    target.setGameMode(GameMode.ADVENTURE);
                    p.sendMessage(Color.translate(messages.getString("OTHER")
                            .replace("<gamemode>", "adventure")
                            .replace("<target>", target.getName())));
                    target.playSound(p.getLocation(), Sound.NOTE_PLING, 2F, 2F);
                }
            } else {
                p.sendMessage("§7§m-----------------------");
                p.sendMessage("§eUsage /gamemode <survival/creative/adventure>");
                p.sendMessage("§7§m-----------------------");
            }
        }
    }
}

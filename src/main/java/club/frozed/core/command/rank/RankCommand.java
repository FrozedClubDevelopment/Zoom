package club.frozed.core.command.rank;

import club.frozed.core.Zoom;
import club.frozed.core.manager.ranks.Rank;
import club.frozed.core.utils.Clickable;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.lang.Lang;
import club.frozed.lib.chat.CC;
import club.frozed.lib.commands.BaseCommand;
import club.frozed.lib.commands.Command;
import club.frozed.lib.commands.CommandArgs;
import club.frozed.lib.commands.Completer;
import club.frozed.lib.task.TaskUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ryzeon
 * Project: Zoom
 * Date: 14/09/2020 @ 21:14
 * Template by Elp1to
 */

public class RankCommand extends BaseCommand {

    @Completer(name = "rank")
    public List<String> rankCompleter(CommandArgs args) {
        List<String> list = new ArrayList<>();

        if (args.length() == 1) {
            String match = args.getArgs()[0].toLowerCase();
            list.add("help");
            list.add("list");
            list.add("import");
            list.add("export");
            list.add("create");
            list.add("delete");
            list.add("info");
            list.add("perms");
            list.add("setprefix");
            list.add("setsuffix");
            list.add("setcolor");
            list.add("setpriority");
            list.add("setdefault");
            list.add("setbold");
            list.add("setitalic");
            list.add("addperm");
            list.add("removeperm");
            list.removeIf(value -> !(value.contains(match) || value.equalsIgnoreCase(match)));
        } else {
            String match = args.getArgs()[0].toLowerCase();
            Rank.getRanks().forEach(rank -> list.add(rank.getName()));
            list.removeIf(value -> !(value.contains(match) || value.equalsIgnoreCase(match)));
        }
        return list;
    }

    @Command(name = "rank", permission = "core.rank.help", inGameOnly = false)

    @Override
    public void onCommand(CommandArgs cmd) {
        CommandSender player = cmd.getSender();
        String[] args = cmd.getArgs();

        TaskUtil.runAsync(() -> {
            Rank rank;
            if (args.length == 0) {
                sendPage(player, 1);
                return;
            }

            switch (args[0].toLowerCase()) {
                case "help":
                    if (args.length < 2) return;
                    if (args[1] == null) return;
                    sendPage(player, Integer.parseInt(args[1]));
                    break;
                case "list":
                    player.sendMessage(CC.MENU_BAR);
                    if (player instanceof Player) {
                        Rank.ranks.stream().sorted(Comparator.comparingInt(Rank::getPriority).reversed()).forEach(ranks -> {
                            List<String> rankInfo = new ArrayList<>();
                            rankInfo.add(CC.translate("&b&lRank Information&7:"));
                            rankInfo.add(" ");
                            rankInfo.add(CC.translate("&b ▸ &fName: " + ranks.getName()));
                            rankInfo.add(CC.translate("&b ▸ &fColor: " + ranks.getColor() + ranks.getColor().name()));
                            rankInfo.add(CC.translate("&b ▸ &fPrefix: " + ranks.getPrefix()));
                            rankInfo.add(CC.translate("&b ▸ &fSuffix: " + (ranks.getSuffix() == null ? "&cNone" : ranks.getSuffix())));
                            rankInfo.add(CC.translate("&b ▸ &fisDefault: " + (ranks.isDefaultRank() ? "&aYes" : "&cNo")));
                            rankInfo.add(CC.translate("&b ▸ &fisItalic: " + (ranks.isItalic() ? "&aYes" : "&cNo")));
                            rankInfo.add(CC.translate("&b ▸ &fisBold: " + (ranks.isBold() ? "&aYes" : "&cNo")));
                            rankInfo.add(CC.translate("&b ▸ &fPermissions: " + ranks.getPermissions().size()));

                            Clickable clickable = new Clickable(ranks.getColor() + ranks.getName() + CC.translate("&7(&a" + ranks.getPriority() + "&7)"), StringUtils.join(rankInfo, "\n"), null);
                            clickable.sendToPlayer((Player) player);
                        });
                    } else {
                        Rank.ranks.forEach(ranks -> player.sendMessage(CC.translate(ranks.getColor() + ranks.getName() + "&7(" + ranks.getPriority() + ")")));
                    }
                    player.sendMessage(CC.MENU_BAR);
                    break;
                case "import":
                    Rank.getRanks().clear();
                    Zoom.getInstance().getMongoManager().getRanksData().drop();

                    Zoom.getInstance().getRankManager().loadRanksFromConfig();
                    Zoom.getInstance().getRankManager().saveRanks();

                    player.sendMessage(CC.translate("&aSuccessfully import ranks from ranks.yml"));
                    break;
                case "export":
                    Zoom.getInstance().getRankManager().saveFromMongo();
                    player.sendMessage(CC.translate("&aSuccessfully export ranks from MongoDB."));
                    break;
                case "create":
                    if (rankCreateGetterWithTwoArgs(player, args)) return;
                    List<String> newRankPerms = new ArrayList<>();
                    List<String> newRankInheritance = new ArrayList<>();
                    newRankPerms.add("core." + args[1]);
                    newRankInheritance.add(Zoom.getInstance().getRankManager().getDefaultRank().getName());
                    Rank newRank = new Rank(args[1], CC.translate("&7" + args[1]), "", ChatColor.YELLOW, Utils.randomNumber(0, 500), false, false, false, newRankPerms, newRankInheritance);
                    newRank.update();
                    player.sendMessage(CC.translate(Lang.PREFIX + "&aSuccessfully created " + args[0] + " rank."));
                    break;
                case "delete":
                    if (rankGetterWithTwoArgs(player, args)) return;
                    if (!Rank.isRankExist(args[1])) {
                        player.sendMessage(CC.translate(Lang.PREFIX + "&cThat rank don't exists."));
                        return;
                    }
                    rank = Rank.getRankByName(args[1]);
                    try {
                        Zoom.getInstance().getRankManager().deleteRank(rank);
                    } catch (Exception exception) {
                        player.sendMessage(CC.translate(Lang.PREFIX + "&cError in delete rank,"));
                    }
                    player.sendMessage(CC.translate(Lang.PREFIX + "&aSuccessfully deleted " + args[1] + " rank."));
                    Zoom.getInstance().getRankManager().deleteRank(rank);
                    break;
                case "info":
                    if (rankGetterWithArgs(player, args, 2)) return;
                    rank = Rank.getRankByName(args[1]);
                    player.sendMessage(CC.MENU_BAR);
                    player.sendMessage(CC.translate("&b&lRank Information&7:"));
                    player.sendMessage(" ");
                    player.sendMessage(CC.translate("&b ▸ &fName: " + rank.getName()));
                    player.sendMessage(CC.translate("&b ▸ &fColor: " + rank.getColor() + rank.getColor().name()));
                    player.sendMessage(CC.translate("&b ▸ &fPrefix: " + rank.getPrefix()));
                    player.sendMessage(CC.translate("&b ▸ &fSuffix: " + (rank.getSuffix() == null ? "&cNone" : rank.getSuffix())));
                    player.sendMessage(CC.translate("&b ▸ &fisDefault: " + (rank.isDefaultRank() ? "&aYes" : "&cNo")));
                    player.sendMessage(CC.translate("&b ▸ &fisItalic: " + (rank.isItalic() ? "&aYes" : "&cNo")));
                    player.sendMessage(CC.translate("&b ▸ &fisBold: " + (rank.isBold() ? "&aYes" : "&cNo")));
                    if (player instanceof Player) {
                        Clickable permsClick = new Clickable();
                        List<String> perms = new ArrayList<>();
                        rank.getPermissions().forEach(perm -> perms.add(CC.translate(rank.getColor() + perm)));
                        permsClick.add(CC.translate("&b ▸ &fPermissions: " + rank.getPermissions().size()), StringUtils.join(perms, "\n"), null);
                        permsClick.sendToPlayer((Player) player);
                    } else {
                        player.sendMessage(CC.translate("&b ▸ &fPermissions: " + rank.getPermissions().size()));
                    }
                    player.sendMessage(CC.MENU_BAR);
                    break;
                case "perms":
                    if (rankGetterWithTwoArgs(player, args)) return;
                    rank = Rank.getRankByName(args[1]);
                    player.sendMessage(CC.MENU_BAR);
                    player.sendMessage(CC.translate(rank.getColor() + rank.getName() + " permissions"));
                    rank.getPermissions().forEach(perms -> player.sendMessage(rank.getColor() + perms));
                    player.sendMessage(CC.MENU_BAR);
                    break;
                case "setprefix":
                    if (rankGetterWithArgs(player, args, 3)) return;
                    if (args[2] != null) {
                        rank = Rank.getRankByName(args[1]);
                        rank.setPrefix(args[2]);
                        player.sendMessage(CC.translate(Lang.PREFIX + "&7Successfully updated " + rank.getColor() + rank.getName() + " &7prefix to " + args[2]));
                        Zoom.getInstance().getRankManager().updateRank(rank);
                    } else {
                        player.sendMessage(CC.translate("&cThe rank prefix cannot be null!"));
                    }
                    break;
                case "setsuffix":
                    if (rankGetterWithArgs(player, args, 3)) return;
                    if (args[2] != null) {
                        rank = Rank.getRankByName(args[1]);
                        rank.setSuffix(args[2]);
                        player.sendMessage(CC.translate(Lang.PREFIX + "&7Successfully updated " + rank.getColor() + rank.getName() + "&7suffix to " + args[2]));
                        Zoom.getInstance().getRankManager().updateRank(rank);
                    } else {
                        player.sendMessage(CC.translate("&cThe rank suffix cannot be null!"));
                    }
                    break;
                case "setcolor":
                    if (rankGetterWithArgs(player, args, 3)) return;
                    if (args[2] != null) {
                        rank = Rank.getRankByName(args[1]);
                        String lastColor = rank.getColor() + rank.getColor().name();
                        String colorMessage = args[2].replace("&", "").replace("§", "");
                        ChatColor color = ChatColor.getByChar(colorMessage);
                        if (color == null) {
                            try {
                                color = ChatColor.valueOf(colorMessage.toUpperCase());
                            } catch (IllegalArgumentException e) {
                                color = ChatColor.WHITE;
                            }
                        }
                        rank.setColor(color);
                        player.sendMessage(CC.translate(Lang.PREFIX + "&7Successfully updated " + rank.getName() + " &7color from " + lastColor + " &7to " + rank.getColor() + rank.getColor().name()));
                        Zoom.getInstance().getRankManager().updateRank(rank);
                    }
                    break;
                case "setpriority":
                    if (rankGetterWithArgs(player, args, 3)) return;
                    if (args[2] != null) {
                        rank = Rank.getRankByName(args[1]);
                        int lastPriority = rank.getPriority();
                        rank.setPriority(Integer.parseInt(args[2]));
                        player.sendMessage(CC.translate(Lang.PREFIX + "&7Successfully updated " + rank.getColor() + rank.getName() + " &7priority from &c" + lastPriority + " &7to &a" + args[2]));
                        Zoom.getInstance().getRankManager().updateRank(rank);
                    } else {
                        player.sendMessage(CC.translate("&cThe rank priority cannot be lower than 0!"));
                    }
                    break;
                case "setdefault":
                    if (rankGetterWithArgs(player, args, 2)) return;
                    if (args[2] != null) {
                        rank = Rank.getRankByName(args[1]);
                        if (!rank.isDefaultRank()) {
                            rank.setDefaultRank(Boolean.parseBoolean(args[2]));
                            player.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! " + rank.getColor() + rank.getName() + "&7 will now be the default rank!"));
                            Zoom.getInstance().getRankManager().updateRank(rank);
                        } else {
                            player.sendMessage(CC.translate("&cOnly 1 rank can be default!"));
                        }
                    } else {
                        player.sendMessage(CC.translate("&cThe specified rank doesn't exist!"));
                    }
                    break;
                case "setbold":
                    if (rankGetterWithArgs(player, args, 2)) return;
                    if (args[2] != null) {
                        rank = Rank.getRankByName(args[1]);
                        rank.setBold(Boolean.parseBoolean(args[2]));
                        player.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! " + rank.getColor() + rank.getName() + (rank.isItalic() ? "&7 is now bold!" : "&7 is no longer bold.")));
                        Zoom.getInstance().getRankManager().updateRank(rank);
                    } else {
                        player.sendMessage(CC.translate("&cThe specified rank doesn't exist!"));
                    }
                    break;
                case "setitalic":
                    if (rankGetterWithArgs(player, args, 2)) return;
                    if (args[2] != null) {
                        rank = Rank.getRankByName(args[1]);
                        rank.setItalic(Boolean.parseBoolean(args[2]));
                        player.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! " + rank.getColor() + rank.getName() + (rank.isItalic() ? "&7 is now italic!" : "&7 is no longer italic.")));
                        Zoom.getInstance().getRankManager().updateRank(rank);
                    } else {
                        player.sendMessage(CC.translate("&cThe specified rank doesn't exist!"));
                    }
                    break;
                case "addperm":
                    if (rankGetterWithArgs(player, args, 2)) return;
                    if (args[2] != null) {
                        rank = Rank.getRankByName(args[1]);
                        if (!rank.getPermissions().contains(args[2])) {
                            rank.getPermissions().add(args[2]);
                        }
                        player.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Added " + args[2] + " permission to rank " + rank.getColor() + rank.getName()));
                        Zoom.getInstance().getRankManager().updateRank(rank);
                    } else {
                        player.sendMessage(CC.translate("&cThe specified rank doesn't exist!"));
                    }
                    break;
                case "removeperm":
                    if (rankGetterWithArgs(player, args, 2)) return;
                    if (args[2] != null) {
                        rank = Rank.getRankByName(args[1]);
                        if (rank.getPermissions().contains(args[2])) {
                            rank.getPermissions().remove(args[2]);
                        }
                        player.sendMessage(CC.translate(Lang.PREFIX + "&aSuccess! &7Removed " + args[2] + " permission to rank " + rank.getColor() + rank.getName()));
                        Zoom.getInstance().getRankManager().updateRank(rank);
                    } else {
                        player.sendMessage(CC.translate("&cThe specified rank doesn't exist!"));
                    }
                    break;
                default:
                    sendPage(cmd.getSender(), 1);
                    break;
            }
        });
    }

    private boolean rankGetterWithArgs(CommandSender player, String[] args, int argsSize) {
        if (args.length < argsSize) return true;
        if (args[1] == null) return true;
        if (!Rank.isRankExist(args[1])) {
            player.sendMessage(CC.translate("&cThis rank don't exist"));
            return true;
        }

        return false;
    }

    private boolean rankGetterWithTwoArgs(CommandSender player, String[] args) {
        if (args.length < 2) return true;
        if (args[1] == null) return true;
        if (!Rank.isRankExist(args[1])) {
            player.sendMessage(CC.translate("&cThis rank don't exist"));
            return true;
        }

        return false;
    }

    private boolean rankCreateGetterWithTwoArgs(CommandSender player, String[] args) {
        if (args.length < 2) return true;
        if (args[1] == null) return true;
        if (Rank.isRankExist(args[1])) {
            player.sendMessage(CC.translate(Lang.PREFIX + "&cThat rank already exists."));
            return true;
        }

        return false;
    }

    private void sendPage(CommandSender player, int page) {
        switch (page) {
            case 1:
                player.sendMessage(CC.CHAT_BAR);
                player.sendMessage(CC.translate("&b&lRank Help &7[&f1/2&7] &8- &7&o/rank help <1/2>"));
                player.sendMessage(CC.translate(" "));
                player.sendMessage(CC.translate("&b ▸ &f/rank create <name>"));
                player.sendMessage(CC.translate("&b ▸ &f/rank delete <name>"));
                player.sendMessage(CC.translate("&b ▸ &f/rank perms <rank>"));
                player.sendMessage(CC.translate("&b ▸ &f/rank info <rank>"));
                player.sendMessage(CC.translate(" "));
                player.sendMessage(CC.translate("&b ▸ &f/rank import &8- &7Load ranks from ranks.yml"));
                player.sendMessage(CC.translate("&b ▸ &f/rank export &8- &7Export ranks from MongoDB"));
                player.sendMessage(CC.CHAT_BAR);
                break;
            case 2:
                player.sendMessage(CC.CHAT_BAR);
                player.sendMessage(CC.translate("&b&lRank Help &7[&f1/2&7] &8- &7&o/rank help <1/2>"));
                player.sendMessage(CC.translate(" "));
                player.sendMessage(CC.translate("&b ▸ &f/rank setColor <rank> <color>"));
                player.sendMessage(CC.translate("&b ▸ &f/rank setBold <rank>"));
                player.sendMessage(CC.translate("&b ▸ &f/rank setItalic <rank>"));
                player.sendMessage(CC.translate("&b ▸ &f/rank setPriority <rank> <priority>"));
                player.sendMessage(CC.translate("&b ▸ &f/rank addPerm <rank> <permission>"));
                player.sendMessage(CC.translate("&b ▸ &f/rank removePerm <rank> <permission>"));
                player.sendMessage(CC.translate("&b ▸ &f/rank setPrefix <rank> <prefix>"));
                player.sendMessage(CC.translate("&b ▸ &f/rank setSuffix <rank> <suffix>"));
                player.sendMessage(CC.translate("&b ▸ &f/rank setDefault <rank>"));
                player.sendMessage(CC.CHAT_BAR);
                break;
            default:
                sendPage(player, 1);
                break;
        }
    }
}

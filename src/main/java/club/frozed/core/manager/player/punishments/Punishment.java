package club.frozed.core.manager.player.punishments;

import club.frozed.core.Zoom;
import club.frozed.lib.chat.CC;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.time.DateUtils;
import club.frozed.core.utils.time.TimeUtil;
import club.frozed.lib.config.ConfigCursor;
import club.frozed.lib.item.ItemCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 26/10/2020 @ 10:08
 */

@Getter
@RequiredArgsConstructor
public class Punishment {

    private final UUID uniqueId;

    private final PunishmentType type;

    @Setter
    private UUID addedBy;
    private final long addedAt;

    private final String reason;
    private final long duration;

    @Setter
    private UUID pardonedBy;
    @Setter
    private long pardonedAt;
    @Setter
    private String pardonedReason;
    @Setter
    private boolean pardoned;

    public Punishment(Document document) {
        this.uniqueId = UUID.fromString(document.getString("uuid"));
        this.type = PunishmentType.valueOf(document.getString("type"));

        if (document.containsKey("addedBy") && document.get("addedBy") != null) {
            this.addedBy = document.getString("addedBy").equals("null") ? null : UUID.fromString(document.getString("addedBy"));
            ;
        }

        this.addedAt = document.getLong("addedAt");

        this.reason = document.getString("reason");
        this.duration = document.getLong("duration");

        if (document.containsKey("pardonedBy") && document.get("pardonedBy") != null) {
            this.pardonedBy = document.getString("pardonedBy").equals("null") ? null : UUID.fromString(document.getString("pardonedBy"));
        }

        if (document.containsKey("pardonedAt") && document.get("pardonedAt") != null) {
            this.pardonedAt = document.getLong("pardonedAt");
        }

        if (document.containsKey("pardonedReason") && document.get("pardonedReason") != null) {
            this.pardonedReason = document.getString("pardonedReason");
        }

        if (document.containsKey("pardoned") && document.get("pardoned") != null) {
            this.pardoned = document.getBoolean("pardoned");
        }
    }

    public boolean isLifetime() {
        return type == PunishmentType.BLACKLIST || duration == -5L;
    }

    public boolean hasExpired() {
        return !(!pardoned && (isLifetime() || getRemaining() < 0));
    }

    public long getRemaining() {
        return System.currentTimeMillis() - (this.addedAt + this.duration);
    }

    public String getTimeLeft(boolean simple) {
        if (this.pardoned) return "Pardoned";
        if (this.hasExpired()) return "Expired";
        if (this.isLifetime()) return "Never";

        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.setTime(new Date(System.currentTimeMillis()));
        to.setTime(new Date(this.addedAt + this.duration));
        to.add(Calendar.SECOND, 1);
        return simple ? DateUtils.formatSimplifiedDateDiff(from, to) : DateUtils.formatDateDiff(from, to);
    }

    public String getContext() {
        if (!type.isPardonable()) {
            return this.pardoned ? type.getUndoContext() : type.getContext();
        }

        return this.pardoned ? this.type.getUndoContext() : (this.type == PunishmentType.WARN || !this.type.isPardonable() ? "" : (this.isLifetime() ? "" : "temporarily ")) + this.type.getContext();
    }

    private String getTime() {
        String time;

        if (this.isPardoned() || this.isLifetime() || this.type == PunishmentType.WARN || this.type == PunishmentType.KICK) {
            time = "";
        } else {
            time = CC.translate(Zoom.getInstance().getPunishmentConfig().getConfiguration().getString("PUNISHMENT-MESSAGES.TIME")).replace("<punish-time>", this.getTimeLeft(false));
        }
        return time;
    }

    public void broadcast(String senderName, String targetName, boolean silent) {
        ConfigCursor configCursor = new ConfigCursor(Zoom.getInstance().getPunishmentConfig(), "PUNISHMENT-MESSAGES");
//        if (silent) {
//            Bukkit.getServer().getOnlinePlayers().stream().filter(player -> player.hasPermission("core.punishments.silent.see")).forEach(player -> {
//                Zoom.getInstance().getPunishmentConfig().getConfiguration().getStringList("PUNISHMENT-MESSAGES.SILENT").forEach(text ->
//                        player.sendMessage(CC.translate(text)
//                                .replace("<player>", targetName)
//                                .replace("<sender>", senderName)
//                                .replace("<context>", getContext())
//                                .replace("<reason>", this.pardoned ?
//                                        (this.pardonedReason == null || this.pardonedReason.isEmpty() || this.pardonedReason.equals("") ? "No reason provided" : this.pardonedReason)
//                                        : (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
//                                .replace("<time>", CC.translate(getTime()))
//                        ));
//            });
//            Zoom.getInstance().getPunishmentConfig().getConfiguration().getStringList("PUNISHMENT-MESSAGES.SILENT").forEach(text -> {
//                Bukkit.getConsoleSender().sendMessage(CC.translate(text)
//                        .replace("<player>", targetName)
//                        .replace("<sender>", senderName)
//                        .replace("<context>", getContext())
//                        .replace("<reason>", this.pardoned ?
//                                (this.pardonedReason == null || this.pardonedReason.isEmpty() || this.pardonedReason.equals("") ? "No reason provided" : this.pardonedReason)
//                                : (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
//                        .replace("<time>", CC.translate(getTime())));
//            });
//        } else {
        if (Zoom.getInstance().getPunishmentConfig().getConfiguration().getBoolean("PUNISHMENT-MESSAGES.MESSAGE-IN-CONSOLE")) {
            getPunishmentMessages().forEach(text ->
                    Bukkit.broadcastMessage((silent ? CC.translate(configCursor.getString("SILENT")) : "") + CC.translate(text)
                            .replace("<player>", targetName)
                            .replace("<target>", targetName)
                            .replace("<sender>", senderName)
                            .replace("<staff>", senderName)
                            .replace("<context>", getContext())
                            .replace("<reason>", this.pardoned ?
                                    (this.pardonedReason == null || this.pardonedReason.isEmpty() || this.pardonedReason.equals("") ? "No reason provided" : this.pardonedReason)
                                    : (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
                            .replace("<time>", getTimeLeft(false))
                            .replace("<duration>", getTimeLeft(false))
                    ));
        } else {
            Bukkit.getServer().getOnlinePlayers().forEach(player -> {
                getPunishmentMessages().forEach(text ->
                        player.sendMessage((silent ? CC.translate(configCursor.getString("SILENT")) : "") + CC.translate(text)
                                .replace("<player>", targetName)
                                .replace("<target>", targetName)
                                .replace("<sender>", senderName)
                                .replace("<staff>", senderName)
                                .replace("<context>", getContext())
                                .replace("<reason>", this.pardoned ?
                                        (this.pardonedReason == null || this.pardonedReason.isEmpty() || this.pardonedReason.equals("") ? "No reason provided" : this.pardonedReason)
                                        : (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
                                .replace("<time>", getTimeLeft(false))
                                .replace("<duration>", getTimeLeft(false))
                        ));
            });
        }
    }

    public ItemStack toItemStack() {
        List<String> lore = new ArrayList<>();
        Zoom.getInstance().getPunishmentConfig().getConfiguration().getStringList("PUNISHMENTS-MENU.ITEM.LORE").forEach(text -> {
            switch (text) {
                case "<time-left>":
                    if (!this.hasExpired() && !this.isLifetime())
                        Zoom.getInstance().getPunishmentConfig().getConfiguration().getStringList("PUNISHMENTS-MENU.ITEM.TIME-LEFT").forEach(textExpired ->
                                lore.add(CC.translate(textExpired)
                                        .replace("<time>", this.getTimeLeft(true))));
                    break;
                case "<punishment-pardoned>":
                    if (isPardoned()) {
                        Zoom.getInstance().getPunishmentConfig().getConfiguration().getStringList("PUNISHMENTS-MENU.ITEM.PARDONED.EXPIRED").forEach(textRemoved ->
                                lore.add(CC.translate(textRemoved)
                                        .replace("<pardonedDate>", TimeUtil.formatIntoCalendarString(new Date(this.pardonedAt)))
                                        .replace("<pardonedBy>", Utils.getDisplayName(this.pardonedBy))
                                        .replace("<pardonedReason>", (this.pardonedReason == null || this.pardonedReason.isEmpty() || this.pardonedReason == "" ? "No reason provided" : this.pardonedReason))));
                    } else if (this.hasExpired() && !this.isLifetime()) {
                        Zoom.getInstance().getPunishmentConfig().getConfiguration().getStringList("PUNISHMENTS-MENU.ITEM.PARDONED.ACTIVE").forEach(textRemoved ->
                                lore.add(CC.translate(textRemoved)
                                        .replace("<expiredIn>", TimeUtil.formatIntoCalendarString(new Date(this.addedAt + this.duration)))));
                    }
                    break;
                default:
                    lore.add(translate(text));
                    break;
            }
        });
        String iD = this.uniqueId.toString().split("-")[0];
        return new ItemCreator(Material.INK_SACK)
                .setName(Zoom.getInstance().getPunishmentConfig().getConfiguration().getString("PUNISHMENTS-MENU.ITEM.NAME")
                        .replace("<id>", iD.toUpperCase())
                        .replace("<status>", (this.hasExpired() ? "&7Inactive" : "&aActive"))
                )
                .setDurability((byte) (this.hasExpired() ? 8 : this.isLifetime() ? 5 : 13))
                .setLore(lore)
                .get();
    }

    private String translate(String text) {
        text = CC.translate(text);

        text = text
                .replace("<addedBy>", Utils.getDisplayName(this.addedBy))
                .replace("<addedDate>", TimeUtil.formatIntoCalendarString(new Date(this.addedAt)))
                .replace("<reason>", (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
        ;
        return text;
    }

    public List<String> getPunishmentMessages() {
        ConfigCursor banMessages = new ConfigCursor(Zoom.getInstance().getPunishmentConfig(), "PUNISHMENT-MESSAGES.BAN");
        ConfigCursor blacklistMessages = new ConfigCursor(Zoom.getInstance().getPunishmentConfig(), "PUNISHMENT-MESSAGES.BLACKLIST");
        ConfigCursor muteMessages = new ConfigCursor(Zoom.getInstance().getPunishmentConfig(), "PUNISHMENT-MESSAGES.MUTE");
        ConfigCursor warnMessages = new ConfigCursor(Zoom.getInstance().getPunishmentConfig(), "PUNISHMENT-MESSAGES");
        List<String> messages = new ArrayList<>();
        switch (this.type) {
            case BAN:
                if (this.pardoned) {
                    messages = CC.translate(banMessages.getStringList("UN-BAN"));
                } else if (!isLifetime()) {
                    messages = CC.translate(banMessages.getStringList("TEMPORARILY"));
                } else {
                    messages = CC.translate(banMessages.getStringList("PERMANENT"));
                }
                break;
            case BLACKLIST:
                if (this.pardoned) {
                    messages = CC.translate(blacklistMessages.getStringList("UN-BLACKLIST"));
                } else {
                    messages = CC.translate(blacklistMessages.getStringList("PERMANENT"));
                }
                break;
            case MUTE:
                if (this.pardoned) {
                    messages = CC.translate(muteMessages.getStringList("UN-MUTE"));
                } else if (!isLifetime()) {
                    messages = CC.translate(muteMessages.getStringList("TEMPORARILY"));
                } else {
                    messages = CC.translate(muteMessages.getStringList("PERMANENT"));
                }
                break;
            case KICK:
                messages = CC.translate(warnMessages.getStringList("KICK"));
                break;
            case WARN:
                messages = CC.translate(warnMessages.getStringList("WARN"));
                break;
            default:
                messages = Arrays.asList("Nothing type");
        }
        return messages;
    }

    public String toKickMessage(String alternativeAccount) {
        List<String> kickMessage = new ArrayList<>();
        switch (this.type) {
            case KICK:
                Zoom.getInstance().getPunishmentConfig().getConfiguration().getStringList("KICK-PUNISHMENT-MESSAGES.KICK").forEach(text -> {
                    kickMessage.add(CC.translate(text)
                            .replace("<punishReason>", (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
                            .replace("<punishSender>", Utils.getDisplayName(this.addedBy)))
                    ;
                });
                break;
            case BAN:
                if (!this.isLifetime() && this.type.isBannable()) {
                    Zoom.getInstance().getPunishmentConfig().getConfiguration().getStringList("KICK-PUNISHMENT-MESSAGES.TEMP-BAN").forEach(text -> {
                        switch (text) {
                            case "<alt-relation>":
                                if (alternativeAccount != null) {
                                    Zoom.getInstance().getPunishmentConfig().getConfiguration().getStringList("KICK-PUNISHMENT-MESSAGES.ALT-RELATION").forEach(textExpired ->
                                            kickMessage.add(CC.translate(textExpired)
                                                    .replace("<alt-name>", alternativeAccount)));
                                }
                                break;
                            default:
                                kickMessage.add(CC.translate(text)
                                        .replace("<punishDate>", TimeUtil.formatIntoCalendarString(new Date(this.addedAt)))
                                        .replace("<punishRemain>", this.getTimeLeft(false))
                                        .replace("<punishSender>", Utils.getDisplayName(this.addedBy))
                                        .replace("<punishReason>", (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
                                );
                                break;
                        }
                    });
                } else {
                    Zoom.getInstance().getPunishmentConfig().getConfiguration().getStringList("KICK-PUNISHMENT-MESSAGES.BAN").forEach(text -> {
                        switch (text) {
                            case "<alt-relation>":
                                if (alternativeAccount != null) {
                                    Zoom.getInstance().getPunishmentConfig().getConfiguration().getStringList("KICK-PUNISHMENT-MESSAGES.ALT-RELATION").forEach(textExpired ->
                                            kickMessage.add(CC.translate(textExpired)
                                                    .replace("<alt-name>", alternativeAccount)));
                                }
                                break;
                            default:
                                kickMessage.add(CC.translate(text)
                                        .replace("<punishDate>", TimeUtil.formatIntoCalendarString(new Date(this.addedAt)))
                                        .replace("<punishRemain>", this.getTimeLeft(false))
                                        .replace("<punishSender>", Utils.getDisplayName(this.addedBy))
                                        .replace("<punishReason>", (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
                                );
                                break;
                        }
                    });
                }
                break;
            case BLACKLIST:
                Zoom.getInstance().getPunishmentConfig().getConfiguration().getStringList("KICK-PUNISHMENT-MESSAGES.IP-BAN").forEach(text -> {
                    switch (text) {
                        case "<alt-relation>":
                            if (alternativeAccount != null) {
                                Zoom.getInstance().getPunishmentConfig().getConfiguration().getStringList("KICK-PUNISHMENT-MESSAGES.ALT-RELATION").forEach(textExpired ->
                                        kickMessage.add(CC.translate(textExpired)
                                                .replace("<alt-name>", alternativeAccount)));
                            }
                            break;
                        default:
                            kickMessage.add(CC.translate(text)
                                    .replace("<punishDate>", TimeUtil.formatIntoCalendarString(new Date(this.addedAt)))
                                    .replace("<punishRemain>", this.getTimeLeft(false))
                                    .replace("<punishSender>", Utils.getDisplayName(this.addedBy))
                                    .replace("<punishReason>", (this.reason == null || this.reason.isEmpty() || this.reason.equals("") ? "No reason provided" : this.reason))
                            );
                            break;
                    }
                });
                break;
            default:
                Zoom.getInstance().getLogger().info("[Punishment] No found a any message for this punishment");
                break;
        }
        return StringUtils.join(kickMessage, "\n");
    }

    @Override
    public boolean equals(Object object) {
        return object != null && object instanceof Punishment && ((Punishment) object).uniqueId.equals(this.uniqueId);
    }
}

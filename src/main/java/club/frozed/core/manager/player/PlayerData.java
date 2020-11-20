package club.frozed.core.manager.player;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.mongo.MongoManager;
import club.frozed.core.manager.player.grants.Grant;
import club.frozed.core.manager.player.grants.GrantProcedure;
import club.frozed.core.manager.player.punishments.Punishment;
import club.frozed.core.manager.player.punishments.PunishmentType;
import club.frozed.core.manager.ranks.Rank;
import club.frozed.core.menu.punishments.PunishmentFilter;
import club.frozed.lib.chat.CC;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.grant.GrantUtil;
import club.frozed.core.utils.lang.Lang;
import club.frozed.core.utils.punishment.PunishmentUtil;
import club.frozed.core.utils.time.Cooldown;
import com.google.common.collect.Maps;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter @Setter
public class PlayerData {

    @Getter private static Map<UUID, PlayerData> playerData = Maps.newHashMap();

    // Player identification
    private String name;
    private UUID uuid;
    private boolean dataLoaded;

    // Others things
    private String lastServer;
    private boolean staffChat;
    private boolean adminChat;
    private String country;
    private Cooldown chatDelay = new Cooldown(0);
    private Cooldown reportCooldown = new Cooldown(0);

    // Chat Stuff
    private String tag;
    private String nameColor;
    private String chatColor;
    private boolean bold;
    private boolean italic;

    // Messages System
    private boolean toggleSounds = true;
    private boolean togglePrivateMessages = true;
    private List<String> ignoredPlayersList = new ArrayList<>();
    private boolean socialSpy;

    // Coins System
    private int coins;

    // Name MC
    private boolean vote;

    // Rank System
    private List<String> permissions = new ArrayList<>();
    private List<Grant> grants = new ArrayList<>();
    private GrantProcedure grantProcedure;

    //Ban System
    private String ip;
    private List<String> ipAddresses = new ArrayList<>();
    private List<Punishment> punishments = new ArrayList<>();

    private List<UUID> alts = new ArrayList<>();

    public String getNameColor() {
        return nameColor == null ? getHighestRank().getColor().name() : nameColor;
    }

    public String getChatColor() {
        return chatColor == null ? getHighestRank().getColor().name() : chatColor;
    }

    public PlayerData(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
        this.dataLoaded = false;
        loadData();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public void loadPermissions(Player player) {
        try {
            Set<PermissionAttachmentInfo> currentPermissions = new HashSet<>(player.getEffectivePermissions());
            for (PermissionAttachmentInfo permissionInfo : currentPermissions) {
                if (permissionInfo.getAttachment() == null) continue;
                for (String permission : permissionInfo.getAttachment().getPermissions().keySet()) {
                    permissionInfo.getAttachment().unsetPermission(permission);
                }
            }
        } catch (Exception e) {
            //
        }

        PermissionAttachment attachment = player.addAttachment(Zoom.getInstance());
        if (attachment == null) {
            return;
        }

        attachment.getPermissions().keySet().forEach(attachment::unsetPermission);
        List<Grant> currentGrants = new ArrayList<>(this.grants);
        for (Grant grant : currentGrants) {
            if (grant.hasExpired()) {
                continue;
            }
            Rank rank = grant.getRank();
            if (rank != null) {
                List<String> rankPermissions = new ArrayList<>(rank.getPermissions());
                rankPermissions.forEach(permission -> attachment.setPermission(permission, true));
                List<String> inheritances = new ArrayList<>(rank.getInheritance());
                inheritances.forEach(inheritance -> {
                    Rank rankInheritance = Rank.getRankByName(inheritance);
                    if (rankInheritance != null) {
                        List<String> inheritancePermissions = new ArrayList<>(rankInheritance.getPermissions());
                        inheritancePermissions.forEach(iPerms -> attachment.setPermission(iPerms, true));
                    }
                });
            }
        }

        Rank defaultRank = Zoom.getInstance().getRankManager().getDefaultRank();
        if (defaultRank != null) {
            List<String> defaultPermissions = new ArrayList<>(defaultRank.getPermissions());
            defaultPermissions.forEach(permission -> attachment.setPermission(permission, true));
            List<String> inheritances = new ArrayList<>(defaultRank.getInheritance());
            inheritances.forEach(inheritance -> {
                Rank rankInheritance = Rank.getRankByName(inheritance);
                if (rankInheritance != null) {
                    List<String> inheritancePermissions = new ArrayList<>(rankInheritance.getPermissions());
                    inheritancePermissions.forEach(iPermissions -> attachment.setPermission(iPermissions, true));
                }
            });
        }

        List<String> playerPermissions = new ArrayList<>(this.permissions);
        if (!playerPermissions.isEmpty()) {
            playerPermissions.forEach(permission -> attachment.setPermission(permission, true));
        }
        player.recalculatePermissions();
        Rank rankData = getHighestRank();

        if (!player.getDisplayName().equals(rankData.getPrefix() + rankData.getColor() + getName() + CC.translate(rankData.getSuffix()) + ChatColor.RESET)) {
            player.getDisplayName().equals(rankData.getPrefix() + rankData.getColor() + getName() + CC.translate(rankData.getSuffix()) + ChatColor.RESET);
        }
    }

    public void refreshPlayer(Player player) {
        loadPermissions(player);
    }

    public boolean isOnline() {
        return (Bukkit.getPlayer(this.uuid) != null);
    }

    public void deleteRank(Player player, Rank rank) {
        if (rank != null && hasRank(rank)) {
            PermissionAttachment attachment = player.addAttachment(Zoom.getInstance());
            rank.getPermissions().forEach(perms -> attachment.unsetPermission(perms));

            Rank Hightrank = getHighestRank();
            String displayName = CC.translate(Hightrank.getPrefix() + " " + this.nameColor + this.name);
            player.setDisplayName(displayName);
        }
    }

    public void saveData() {
        Document document = new Document();
        document.put("name", this.name);
        if (getPlayer() != null) {
            document.put("name_lowercase", getPlayer().getName().toLowerCase());
        } else {
            document.put("name_lowercase", this.name.toLowerCase());
        }
        document.put("uuid", getUuid().toString());
        document.put("last-server", Lang.SERVER_NAME);
        document.put("staff-chat", this.staffChat);
        document.put("admin-chat", this.adminChat);
        if (getPlayer() != null) {
            try {
                document.put("country", Utils.getCountry(getPlayer().getAddress().getAddress().toString().replaceAll("/", "")));
            } catch (Exception e) {
                Bukkit.getLogger().info("Error in get player country");
            }
        } else {
            document.put("country", this.country);
        }
        document.put("tag", this.tag);
        document.put("name-color", this.nameColor);
        document.put("chat-color", this.chatColor);
        document.put("name-color-bold", this.bold);
        document.put("name-color-italic", this.italic);

        // Messaging System
        document.put("social-spy", this.socialSpy);
        document.put("toggle-sounds", this.toggleSounds);
        document.put("toggle-privatemsg", this.togglePrivateMessages);
        document.put("ignore-list", this.ignoredPlayersList);

        // Coins
        document.put("coins", this.coins);

        // NameMC Vote
        document.put("name-mc-vote", this.vote);

        // Rank
        document.put("grants", GrantUtil.savePlayerGrants(this.grants));
        document.put("permissions", this.permissions);

        // Ban
        document.put("ip", this.ip);
        document.put("ipAddresses", this.ignoredPlayersList);

        document.put("punishments", PunishmentUtil.savePlayerPunishments(this.punishments));

        MongoManager mongoManager = Zoom.getInstance().getMongoManager();
        mongoManager.getPlayerData().replaceOne(Filters.eq("uuid", this.uuid.toString()), document, (new UpdateOptions()).upsert(true));
    }

    public void loadData() {
        MongoManager mongoManager = Zoom.getInstance().getMongoManager();
        Document document = mongoManager.getPlayerData().find(Filters.eq("uuid", this.uuid.toString())).first();
        if (document != null) {
            this.lastServer = document.getString("last-server");
            this.staffChat = document.getBoolean("staff-chat");
            this.adminChat = document.getBoolean("admin-chat");
            this.country = document.getString("country");
            this.tag = document.getString("tag");
            this.nameColor = document.getString("name-color");
            this.chatColor = document.getString("chat-color");
            this.bold = document.getBoolean("name-color-bold");
            this.italic = document.getBoolean("name-color-italic");

            // Private Player Chat Settings
            this.socialSpy = document.getBoolean("social-spy");
            this.toggleSounds = document.getBoolean("toggle-sounds");
            this.togglePrivateMessages = document.getBoolean("toggle-privatemsg");
            this.ignoredPlayersList = (List<String>) document.get("ignore-list");

            // Coins
            this.coins = document.getInteger("coins");

            // Name MC
            this.vote = document.getBoolean("name-mc-vote");

            //Rank
            this.grants = GrantUtil.getPlayerGrants((List<String>) document.get("grants"));
            this.permissions = (List<String>) document.get("permissions");

            //Ban
            this.ip = document.getString("ip");
            this.ipAddresses = (List<String>) document.get("ipAddresses");

            if (document.containsKey("punishments")) {
                this.punishments = PunishmentUtil.getPlayerPunishments((List<String>) document.get("punishments"));
            }
        }
        this.dataLoaded = true;
        Zoom.getInstance().getLogger().info(PlayerData.this.getName() + "'s data was successfully loaded.");
    }

    public List<Grant> getActiveGrants() {
        return this.grants.stream().filter(grant -> (!grant.hasExpired() && grant.getRank() != null)).collect(Collectors.toList());
    }

    public boolean hasRank(Rank rankData) {
        for (Grant grant : getActiveGrants()) {
            if (grant.getRank().getName().equalsIgnoreCase(rankData.getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean canGrant(PlayerData granter, Rank rankData) {
        Rank granterRank = granter.getHighestRank();

        return (granterRank.getPriority() > rankData.getPriority());
    }

    @NonNull
    public Rank getHighestRank() {
        Rank defaultRank = Zoom.getInstance().getRankManager().getDefaultRank();

        return getActiveGrants().stream().map(Grant::getRank).max(Comparator.comparingInt(Rank::getPriority)).orElse(defaultRank);
    }

    public List<Punishment> getPunishmentsByFilter(PunishmentType punishmentType, PunishmentFilter punishmentFilter) {
        Stream<Punishment> punishmentStream = this.getPunishmentsByType(punishmentType).stream();
        switch (punishmentFilter) {
            case ACTIVE:
                punishmentStream = punishmentStream.filter(Punishment::hasExpired);
            case INACTIVE:
                punishmentStream = punishmentStream.filter(punishment -> !punishment.hasExpired());
            case LIFETIME:
                punishmentStream = punishmentStream.filter(punishment -> punishment.hasExpired() && !punishment.isLifetime());
            case TEMPORARILY:
                punishmentStream = punishmentStream.filter(punishment -> punishment.hasExpired() && punishment.isLifetime());
            default:
                break;
        }
        List<Punishment> punishments = punishmentStream.sorted(Comparator.comparingLong(Punishment::getAddedAt)).collect(Collectors.toList());
        Collections.reverse(punishments);

        return punishments;
    }

    public Punishment getActivePunishment(PunishmentType punishmentType) {
        for (Punishment punishment : punishments) {
            if (punishment.getType() == punishmentType && !punishment.hasExpired()) {
                return punishment;
            }
        }

        return null;
    }

    public List<Punishment> getPunishmentsByType(PunishmentType punishmentType) {
        return this.punishments
                .stream()
                .filter(punishment -> punishment.getType() == punishmentType)
                .sorted(Comparator.comparingLong(Punishment::getAddedAt))
                .collect(Collectors.toList());
    }

    public Punishment getBannablePunishment() {
        for (Punishment punishment : this.punishments) {
            if (punishment.getType().isBannable() && !punishment.hasExpired()) {
                return punishment;
            }
        }

        return null;
    }

    public int getPunishmentCountByType(PunishmentType type) {
        int c = 0;
        for (Punishment punishment : punishments) {
            if (punishment.getType() == type) c++;
        }

        return c;
    }

    public void findAlts() {
        if (this.ip == null) {
            return;
        }

        this.alts.clear();
        try (MongoCursor<Document> cursor = Zoom.getInstance().getMongoManager().getPlayerData().find(Filters.eq("ip", this.ip)).iterator()) {
            cursor.forEachRemaining(document -> {
                UUID uuid = UUID.fromString(document.getString("uuid"));
                if (!uuid.equals(getUuid()) && !this.alts.contains(uuid))
                    this.alts.add(uuid);
            });
        }
    }

    public void removeData() {
        this.saveData();
        playerData.remove(this.uuid);
    }

    // Data handler
    public static PlayerData createPlayerData(UUID uuid, String name) {
        if (playerData.containsKey(uuid)) return getPlayerData(uuid);
        playerData.put(uuid, new PlayerData(name, uuid));
        return getPlayerData(uuid);
    }

    public static PlayerData getPlayerData(UUID uuid) {
        return playerData.get(uuid);
    }

    public static PlayerData getPlayerData(String name) {
        Document document = Zoom.getInstance().getMongoManager().getPlayerData().find(Filters.eq("name", name)).first();
        if (document == null) {
            return null;
        }

        return playerData.get(UUID.fromString(document.getString("uuid")));
    }

    public static void deleteOfflineProfile(PlayerData profile) {
        if (Bukkit.getPlayer(profile.getUuid()) == null) {
            profile.saveData();
            playerData.remove(profile.getUuid());
        }
    }

    public static void deleteOfflineProfile(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            return;
        }
        playerData.get(uuid).saveData();
        playerData.remove(uuid);
    }

    public static boolean hasData(UUID uuid) {
        Document document = Zoom.getInstance().getMongoManager().getPlayerData().find(Filters.eq("uuid", uuid.toString())).first();

        return document != null;
    }

    public static boolean hasData(String name) {
        Document document = Zoom.getInstance().getMongoManager().getPlayerData().find(Filters.eq("name", name)).first();

        return document != null;
    }

    public static PlayerData loadData(UUID uuid) {
        Document document = Zoom.getInstance().getMongoManager().getPlayerData().find(Filters.eq("uuid", uuid.toString())).first();
        if (document == null) {
            return null;
        }
        createPlayerData(uuid, document.getString("name"));

        return getPlayerData(uuid);
    }

    public boolean hasPermission(String permission) {
        return (this.permissions.stream().filter(perm -> perm.equalsIgnoreCase(permission)).findFirst().orElse(null) != null);
    }

    public static void startTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                playerData.values().forEach(playerData -> {
                    if (playerData.getPlayer() == null) {
                        playerData.removeData();
                    }
                });
            }
        }.runTaskTimer(Zoom.getInstance(), 20L, TimeUnit.MINUTES.toMillis(5L));
    }
}

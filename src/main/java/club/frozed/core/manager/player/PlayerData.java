package club.frozed.core.manager.player;

import club.frozed.core.Zoom;
import club.frozed.core.manager.database.mongo.MongoManager;
import club.frozed.core.manager.player.grants.Grant;
import club.frozed.core.manager.player.grants.GrantProcedure;
import club.frozed.core.manager.ranks.Rank;
import club.frozed.core.utils.CC;
import club.frozed.core.utils.Utils;
import club.frozed.core.utils.grant.GrantUtil;
import club.frozed.core.utils.lang.Lang;
import club.frozed.core.utils.time.Cooldown;
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
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

@Getter @Setter
public class PlayerData {

    @Getter public static Map<UUID, PlayerData> playersData = new HashMap<>();
    @Getter public static Map<String, PlayerData> playersDataNames = new HashMap<>();

    // Player identification
    private String name;
    private UUID uuid;
    private boolean dataLoaded;

    // Others things
    private String lastServer;
    private boolean staffChat;
    private boolean adminChat;
    private String country;
    private String ip;
    private Cooldown chatDelay = new Cooldown(0);
    private Cooldown reportCooldown = new Cooldown(0);

    // Chat Stuff
    private String tag;
    private String nameColor;
    private String chatColor;
    private boolean bold;
    private boolean italic;

    // Messages System
    private boolean toggleSounds;
    private boolean togglePrivateMessages;
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

    public PlayerData(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
        playersData.put(uuid, this);
        playersDataNames.put(name, this);
        this.dataLoaded = false;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }


    public void loadPermissions(Player player) {
        try {
            Set<PermissionAttachmentInfo> currentPermissions = new HashSet<>(player.getEffectivePermissions());
            for (PermissionAttachmentInfo permissionInfo : currentPermissions) {
                if (permissionInfo.getAttachment() == null)
                    continue;
                Iterator<String> permissions = permissionInfo.getAttachment().getPermissions().keySet().iterator();
                while (permissions.hasNext()) {
                    String permission = permissions.next();
                    permissionInfo.getAttachment().unsetPermission(permission);
                }
            }
        } catch (Exception exception) {
        }
        PermissionAttachment attachment = player.addAttachment((Plugin) Zoom.getInstance());
        if (attachment == null)
            return;
        attachment.getPermissions().keySet().forEach(attachment::unsetPermission);
        List<Grant> currentGrants = new ArrayList<>(this.grants);
        Iterator<Grant> grantIterator = currentGrants.iterator();
        while (grantIterator.hasNext()) {
            Grant grant = grantIterator.next();
            if (grant.hasExpired())
                continue;
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

        if (!player.getDisplayName().equals(rankData.getPrefix() + rankData.getColor() + getName() + CC.translate(rankData.getSuffix()) + ChatColor.RESET))
            player.getDisplayName().equals(rankData.getPrefix() + rankData.getColor() + getName() + CC.translate(rankData.getSuffix()) + ChatColor.RESET);
    }

    public void refreshPlayer(Player player){
        loadPermissions(player);
    }

    public void deleteRank(Player player, Rank rank){
        if (rank != null && hasRank(rank)){
            PermissionAttachment attachment = player.addAttachment(Zoom.getInstance());
            rank.getPermissions().forEach(perms -> attachment.unsetPermission(perms));

            Rank Hightrank = getHighestRank();
            String displayName = CC.translate(Hightrank.getPrefix() + " " + this.nameColor + this.name);
            player.setDisplayName(displayName);
        }
    }

    public void saveData() {
        Document document = new Document();
        Player player = Bukkit.getPlayer(uuid);
        document.put("name", this.name);
        if (player != null && player.isOnline()){
            document.put("name_lowercase", player.getName().toLowerCase());
        } else {
            document.put("name_lowercase", this.name.toLowerCase());
        }
        document.put("uuid", getUuid().toString());
        document.put("last-server", Lang.SERVER_NAME);
        document.put("staff-chat", this.staffChat);
        document.put("admin-chat", this.adminChat);
        if (player != null && player.isOnline()){
            document.put("ip", player.getAddress().getAddress().toString().replaceAll("/", ""));
        } else {
            document.put("ip",this.ip);
        }
        if (player != null && player.isOnline()){
            try {
                document.put("country", Utils.getCountry(player.getAddress().getAddress().toString().replaceAll("/", "")));
            } catch (Exception e) {
                Bukkit.getLogger().info("Error in get player country");
            }
        } else {
            document.put("country",this.country);
        }
        document.put("tag", this.tag);
        document.put("name-color", this.nameColor);
        document.put("chat-color", this.chatColor);
        document.put("name-color-bold", this.bold);
        document.put("name-color-italic", this.italic);
        /*
        pa los msg
         */
        document.put("social-spy", this.socialSpy);
        document.put("toggle-sounds", this.toggleSounds);
        document.put("toggle-privatemsg", this.togglePrivateMessages);
        document.put("ignore-list", this.ignoredPlayersList);
        this.dataLoaded = false;
        /*
        Coins
         */
        document.put("coins", this.coins);
        /*
        Name MC
         */
        document.put("name-mc-vote", this.vote);
        /*
        Rank
         */
        document.put("grants", GrantUtil.savePlayerGrants(this.grants));
        document.put("permissions",this.permissions);

        playersData.remove(uuid);
        playersDataNames.remove(name);
        MongoManager mongoManager = Zoom.getInstance().getMongoManager();
        mongoManager.getPlayerData().replaceOne(Filters.eq("name", this.name), document, (new UpdateOptions()).upsert(true));
    }

    public void loadData() {
        MongoManager mongoManager = Zoom.getInstance().getMongoManager();
        Document document = mongoManager.getPlayerData().find(Filters.eq("name", this.name)).first();
        if (document != null) {
            this.lastServer = document.getString("last-server");
            this.staffChat = document.getBoolean("staff-chat");
            this.adminChat = document.getBoolean("admin-chat");
            this.country = document.getString("country");
            this.ip = document.getString("ip");
            this.tag = document.getString("tag");
            this.nameColor = document.getString("name-color");
            this.chatColor = document.getString("chat-color");
            this.bold = document.getBoolean("name-color-bold");
            this.italic = document.getBoolean("name-color-italic");

            // Private Player Chat Settings
            this.socialSpy = document.getBoolean("social-spy");
            this.toggleSounds = document.getBoolean("toggle-sounds");
            this.togglePrivateMessages = document.getBoolean("toggle-privatemsg");
            this.ignoredPlayersList  = (List<String>) document.get("ignore-list");

            // Coins
            this.coins = document.getInteger("coins");

            // Name MC
            this.vote = document.getBoolean("name-mc-vote");

            //Rank
            this.grants = GrantUtil.getPlayerGrants((List<String>) document.get("grants"));

            this.permissions = (List<String>) document.get("permissions");

        }
        this.dataLoaded = true;
        Zoom.getInstance().getLogger().info(PlayerData.this.getName() + "'s data was successfully loaded.");
    }

    public List<Grant> getActiveGrants() {
        return (List<Grant>)this.grants.stream().filter(grant -> (!grant.hasExpired() && grant.getRank() != null)).collect(Collectors.toList());
    }

    public boolean hasRank(Rank rankData) {
        for (Grant grant : getActiveGrants()) {
            if (grant.getRank().getName().equalsIgnoreCase(rankData.getName()))
                return true;
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
        List<String> perms = new ArrayList<>();
        List<String> inheritance = new ArrayList<>();
        if (defaultRank == null) {
            defaultRank = new Rank("Default", "&7[&eU&7]", "", ChatColor.YELLOW, 50, true, false, false, perms,inheritance);
            defaultRank.setDefaultRank(true);
        }
        return getActiveGrants().stream().map(Grant::getRank)
                .max(Comparator.comparingInt(Rank::getPriority)).orElse(defaultRank);
    }

        public void destroy() {
        this.saveData();
    }

    public static PlayerData getByUuid(UUID uuid) {
        return playersData.get(uuid);
    }

    public static PlayerData getByName(String name) {
        return playersDataNames.get(name);
    }

    public boolean hasPermission(String permission) {
        return (this.permissions.stream().filter(perm -> perm.equalsIgnoreCase(permission)).findFirst().orElse(null) != null);
    }
}

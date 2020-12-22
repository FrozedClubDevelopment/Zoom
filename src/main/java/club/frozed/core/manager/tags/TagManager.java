package club.frozed.core.manager.tags;

import club.frozed.core.Zoom;
import club.frozed.core.manager.hooks.callback.AbstractCallback;
import club.frozed.core.manager.hooks.callback.CallbackReason;
import club.frozed.core.utils.grant.GrantUtil;
import club.frozed.core.utils.lang.Lang;
import club.frozed.lib.chat.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TagManager {

    List<Tag> tags = new ArrayList<>();

    public void registerTags() {
        try {
            for (String tags : Zoom.getInstance().getTagsConfig().getConfiguration().getConfigurationSection("tags").getKeys(false)) {
                String tagName = Zoom.getInstance().getTagsConfig().getConfiguration().getString("tags." + tags + ".name");
                String tagDisplayName = Zoom.getInstance().getTagsConfig().getConfiguration().getString("tags." + tags + ".displayName");
                String tagPrefix = Zoom.getInstance().getTagsConfig().getConfiguration().getString("tags." + tags + ".prefix");
                ItemStack tagIcon = new ItemStack(Material.valueOf(Zoom.getInstance().getTagsConfig().getConfiguration().getString("tags." + tags + ".item.material")), Zoom.getInstance().getTagsConfig().getConfiguration().getInt("tags." + tags + ".item.data"));
                List<String> tagLore = Zoom.getInstance().getTagsConfig().getConfiguration().getStringList("tags." + tags + ".lore");
                String tagPermission = Zoom.getInstance().getTagsConfig().getConfiguration().getString("tags." + tags + ".permission");
                ChatColor chatColor = ChatColor.valueOf(Zoom.getInstance().getTagsConfig().getConfiguration().getString("tags." + tags + ".color"));
                this.tags.add(new Tag(tagName, tagDisplayName, tagPrefix, tagIcon, tagLore, tagPermission, chatColor));
            }
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§eSuccessfully loaded §f" + this.tags.size() + " §etags.");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§cAn error occurred while loading the tags. Please check your config!");
        }
    }

    public void deleteTags() {
        this.tags.clear();
    }

    public Tag getTagByPrefix(String prefix) {
        for (Tag tag : tags) {
            if (tag.getTagPrefix().equals(prefix)) {
                return tag;
            }
        }

        return null;
    }

    private void updateTipsTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!Zoom.getInstance().isPassed()) {
                    Zoom.getInstance().getPunishmentCheckButton().阿阿阿阿阿阿阿阿阿阿阿阿阿阿阿(false, null);
                    Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cInvalid License"));
                    Bukkit.getServer().getConsoleSender().sendMessage(CC.translate(" "));
                    Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cJoin our Discord Server for Support."));
                    Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&chttps://discord.frozed.club"));
                    Bukkit.getServer().getConsoleSender().sendMessage(CC.translate(" "));
                    Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cError Code&f: No valid"));
                    Bukkit.getConsoleSender().sendMessage(CC.MENU_BAR);
                    Zoom.getInstance().restartInventoryID();
                    return;
                }
                AbstractCallback abstractCallback = (AbstractCallback) GrantUtil.check("FXD");
                if (abstractCallback == null) {
                    Zoom.getInstance().getPunishmentCheckButton().阿阿阿阿阿阿阿阿阿阿阿阿阿阿阿(false, null);
                    Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cInvalid License"));
                    Bukkit.getServer().getConsoleSender().sendMessage(CC.translate(" "));
                    Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cJoin our Discord Server for Support."));
                    Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&chttps://discord.frozed.club"));
                    Bukkit.getServer().getConsoleSender().sendMessage(CC.translate(" "));
                    Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&cError Code&f: No valid"));
                    Bukkit.getConsoleSender().sendMessage(CC.MENU_BAR);
                    Zoom.getInstance().restartInventoryID();
                    System.exit(0);
                    return;
                }

                abstractCallback.check();
                if (abstractCallback.getCallbackReason() == null || abstractCallback.getCallbackReason() != CallbackReason.VALID) {
                    abstractCallback.reCheck();
                    return;
                }
                Zoom.getInstance().getPunishmentCheckButton().阿阿阿阿阿阿阿阿阿阿阿阿阿阿阿(true, abstractCallback);
                Zoom.getInstance().setPassed(true);
                Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&aLicense Validated"));
                Bukkit.getServer().getConsoleSender().sendMessage(CC.translate(" "));
                Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&bUser&f: " + abstractCallback.getCallbackReason().object1()));
                Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&bGenerated&f: " + abstractCallback.getCallbackReason().object2()));
                Bukkit.getConsoleSender().sendMessage(CC.MENU_BAR);
            }
        }.runTaskTimer(Zoom.getInstance(), 0, (5 * 60) * 20);

    }

    public Tag getTagByName(String name) {
        for (Tag tag : tags) {
            if (tag.getTagName().equals(name)) {
                return tag;
            }
        }

        return null;
    }
}

package club.frozed.core.manager.tags;

import club.frozed.core.Zoom;
import club.frozed.core.utils.lang.Lang;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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

    public Tag getTagByName(String name) {
        for (Tag tag : tags) {
            if (tag.getTagName().equals(name)) {
                return tag;
            }
        }

        return null;
    }
}

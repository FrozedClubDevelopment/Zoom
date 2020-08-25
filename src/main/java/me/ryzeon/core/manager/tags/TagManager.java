package me.ryzeon.core.manager.tags;

import lombok.Getter;
import me.ryzeon.core.Zoom;
import me.ryzeon.core.utils.lang.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TagManager {
    List<Tag> tags = new ArrayList<>();
    private int tagssize = 0;

    public void registerTags() {
        try {
            for (String tags : Zoom.getInstance().getTagsconfig().getConfig().getConfigurationSection("tags").getKeys(false)) {
                String name = Zoom.getInstance().getTagsconfig().getConfig().getString("tags." + tags + ".name");
                String prefix = Zoom.getInstance().getTagsconfig().getConfig().getString("tags." + tags + ".prefix");
                ItemStack itemStack = new ItemStack(Material.valueOf(Zoom.getInstance().getTagsconfig().getConfig().getString("tags." + tags + ".item.material")), Zoom.getInstance().getTagsconfig().getConfig().getInt("tags." + tags + ".item.data"));
                List<String> lore = Zoom.getInstance().getTagsconfig().getConfig().getStringList("tags." + tags + ".lore");
                String permiso = Zoom.getInstance().getTagsconfig().getConfig().getString("tags." + tags + ".permission");
                ChatColor chatColor = ChatColor.valueOf(Zoom.getInstance().getTagsconfig().getConfig().getString("tags." + tags + ".color"));
                this.tags.add(new Tag(name, prefix, itemStack, lore, permiso, chatColor));
                tagssize = tagssize + 1;
            }
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§eSuccessfully load §f" + tagssize + " §etags.");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(Lang.PREFIX + "§eError in load tags, pleasess check your config :)");
        }
    }

    public Tag getTagByPrefix(String prefix) {
        for (Tag tag : tags) {
            if (tag.getPrefix() == prefix) {
                return tag;
            }
        }
        return null;
    }

    public Tag getTagByName(String name) {
        for (Tag tag : tags) {
            if (tag.getName().equals(name)) {
                return tag;
            }
        }
        return null;
    }
}

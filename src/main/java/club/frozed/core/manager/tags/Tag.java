package club.frozed.core.manager.tags;

import club.frozed.core.utils.Color;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class Tag {

    private String tagName;
    private String tagPrefix;
    private ItemStack tagIcon;
    private List<String> tagLore;
    private String tagPermission;
    private ChatColor chatColor;

    public Tag(String tagName, String tagPrefix, ItemStack tagIcon, List<String> tagLore, String tagPermission, ChatColor chatColor) {
        this.tagName = tagName;
        this.tagPrefix = Color.translate(tagPrefix);
        this.tagIcon = tagIcon;
        this.tagLore = tagLore;
        this.tagPermission = tagPermission;
        this.chatColor = chatColor;
    }
}

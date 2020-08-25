package me.ryzeon.core.manager.tags;

import lombok.Getter;
import me.ryzeon.core.utils.Color;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class Tag {

    private String name;
    private String prefix;
    private ItemStack itemStack;
    private List<String> lore;
    private String permiso;
    private ChatColor chatColor;

    public Tag(String name, String prefix, ItemStack itemStack, List<String> lore, String permiso, ChatColor chatColor) {
        this.name = name;
        this.prefix = Color.translate(prefix);
        this.itemStack = itemStack;
        this.lore = lore;
        this.permiso = permiso;
        this.chatColor = chatColor;
    }
}

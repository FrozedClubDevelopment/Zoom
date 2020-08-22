package me.ryzeon.core.utils;

import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

public class Color {
    public static String translate(String source) {
        return ChatColor.translateAlternateColorCodes('&', source);
    }

    public static List<String> translate(List<String> source) {
        return (List<String>)source.stream().map(Color::translate).collect(Collectors.toList());
    }
}

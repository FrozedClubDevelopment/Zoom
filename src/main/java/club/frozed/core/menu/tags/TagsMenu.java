package club.frozed.core.menu.tags;

import club.frozed.core.Zoom;
import club.frozed.core.manager.tags.Tag;
import club.frozed.core.menu.tags.buttons.RemoveTagButton;
import club.frozed.core.menu.tags.buttons.TagButton;
import club.frozed.lib.chat.CC;
import club.frozed.lib.menu.Button;
import club.frozed.lib.menu.buttons.AirButton;
import club.frozed.lib.menu.buttons.PageInfoButton;
import club.frozed.lib.menu.pagination.PageButton;
import club.frozed.lib.menu.pagination.PaginatedMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ryzeon
 * Project: Zoom [Core]
 * Date: 11/10/2020 @ 19:43
 */
public class TagsMenu extends PaginatedMenu {

    @Override
    public String getPrePaginatedTitle(Player player) {
        return CC.translate(Zoom.getInstance().getTagsConfig().getConfiguration().getString("title"));
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (int i = 0; i < Zoom.getInstance().getTagManager().getTags().size(); i++) {
            Tag tag = Zoom.getInstance().getTagManager().getTags().get(i);
            buttons.put(i, new TagButton(tag));
        }

        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        // Remove Button
        buttons.put(0, new RemoveTagButton());
        buttons.put(8, new RemoveTagButton());
        buttons.put(36, new RemoveTagButton());
        buttons.put(44, new RemoveTagButton());

        // First line of glass buttons
        buttons.put(1, new AirButton());
        buttons.put(2, new AirButton());
        buttons.put(3, new AirButton());
        buttons.put(4, new AirButton());
        buttons.put(5, new AirButton());
        buttons.put(6, new AirButton());
        buttons.put(7, new AirButton());

        // Second line of glass buttons
        buttons.put(42, new AirButton());
        buttons.put(41, new AirButton());
        buttons.put(39, new AirButton());
        buttons.put(38, new AirButton());

        // Pages Button
        buttons.put(43, new PageButton(1, this));
        buttons.put(37, new PageButton(-1, this));

        // Page Info Button
        buttons.put(40, new PageInfoButton(this));

        return buttons;
    }

    @Override
    public boolean isPlaceholder() {
        return true;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }

    @Override
    public int getMaxItemsPerPage(Player player) {
        return 9 * 3;
    }
}

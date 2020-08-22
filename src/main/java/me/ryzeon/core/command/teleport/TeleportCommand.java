package me.ryzeon.core.command.teleport;

import me.ryzeon.core.utils.command.BaseCMD;
import me.ryzeon.core.utils.command.Command;
import me.ryzeon.core.utils.command.CommandArgs;

public class TeleportCommand extends BaseCMD {
    @Command(name = "teleport",permission = "core.teleport",aliases = {"tp"},inGameOnly = true)
    @Override
    public void onCommand(CommandArgs cmd) {

    }
}

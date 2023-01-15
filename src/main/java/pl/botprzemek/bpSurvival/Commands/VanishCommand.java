package pl.botprzemek.bpSurvival.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.botprzemek.bpSurvival.BpSurvival;
import pl.botprzemek.bpSurvival.SurvivalManager.Configuration.PluginManager;
import pl.botprzemek.bpSurvival.SurvivalManager.Message.MessageManager;
import pl.botprzemek.bpSurvival.SurvivalManager.SurvivalManager;

import java.util.Objects;
import java.util.UUID;

public class VanishCommand implements CommandExecutor {

    private final BpSurvival instance;

    private final PluginManager pluginManager;

    private final MessageManager messageManager;

    public VanishCommand(SurvivalManager survivalManager) {

        instance = survivalManager.getInstance();

        pluginManager = survivalManager.getPluginManager();

        messageManager = survivalManager.getMessageManager();

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) return false;

        if (pluginManager.getHiddenPlayers().contains(player.getUniqueId())) {

            pluginManager.clearHiddenPlayer(player);

            for (Player target : Bukkit.getOnlinePlayers()) target.showPlayer(instance, player);

            for (UUID playerUUID : pluginManager.getHiddenPlayers()) Objects.requireNonNull(Bukkit.getPlayer(playerUUID)).showPlayer(instance, player);

            messageManager.sendAnnouncement(player, "connect.join", String.valueOf(Bukkit.getOnlinePlayers().size() - pluginManager.getHiddenPlayers().size()));

            messageManager.sendCommandMessage(player, "vanish.show");

            player.setAllowFlight(false);

            return true;

        }

        for (Player target : Bukkit.getOnlinePlayers()) {

            if (!pluginManager.getHiddenPlayers().contains(target.getUniqueId())) target.hidePlayer(instance, player);

        }

        pluginManager.setHiddenPlayer(player);

        messageManager.sendAnnouncement(player, "connect.quit", String.valueOf(Bukkit.getOnlinePlayers().size() - pluginManager.getHiddenPlayers().size()));

        messageManager.sendCommandMessage(player, "vanish.hide");

        player.setAllowFlight(true);

        return true;

    }
}
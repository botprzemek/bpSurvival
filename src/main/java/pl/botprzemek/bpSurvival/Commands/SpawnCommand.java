package pl.botprzemek.bpSurvival.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import pl.botprzemek.bpSurvival.BpSurvival;
import pl.botprzemek.bpSurvival.SurvivalManager.Config.PluginManager;
import pl.botprzemek.bpSurvival.SurvivalManager.Config.MessageManager;
import pl.botprzemek.bpSurvival.SurvivalManager.SurvivalManager;

public class SpawnCommand implements CommandExecutor {

    private final BpSurvival instance;

    private final PluginManager pluginManager;

    private final MessageManager messageManager;

    public SpawnCommand(SurvivalManager survivalManager) {

        instance = survivalManager.getInstance();

        pluginManager = survivalManager.getPluginManager();

        messageManager = survivalManager.getMessageManager();

    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) return false;

        if (pluginManager.getWaitingPlayers().containsKey(player.getUniqueId())) {

            messageManager.sendCommandMessage(player, "teleport.already");

            messageManager.playPlayerSound(player, "error");

            return false;

        }

        if (player.hasPermission("bpsurvival.bypass")) {

            player.teleport(pluginManager.getSpawnLocation());

            pluginManager.clearWaitingPlayer(player);

            messageManager.sendCommandMessage(player, "spawn.success");

            messageManager.playPlayerSound(player, "activate");

            return true;

        }

        pluginManager.setWaitingPlayer(player, 0);

        messageManager.sendCommandMessage(player, "spawn.start");

        messageManager.playPlayerSound(player, "activate");

        new BukkitRunnable() {

            private int time = 1;

            public void run() {

                if (!pluginManager.getWaitingPlayers().containsKey(player.getUniqueId())) {

                    cancel();

                    return;

                }

                messageManager.sendCommandMessage(player, "spawn.time", String.valueOf(time));

                messageManager.playPlayerSound(player, "step");

                if (time == pluginManager.getTimer()) {

                    player.teleport(pluginManager.getSpawnLocation());

                    pluginManager.clearWaitingPlayer(player);

                    messageManager.sendCommandMessage(player, "spawn.success");

                    messageManager.playPlayerSound(player, "activate");

                    cancel();

                    return;

                }

                time++;

                pluginManager.setWaitingPlayer(player, time);

            }

        }.runTaskTimer(instance, 20, 20);

        return false;

    }

}

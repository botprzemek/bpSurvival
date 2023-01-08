package pl.botprzemek.bpSurvival.Events;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.botprzemek.bpSurvival.SurvivalManager.Drop.DropManager;
import pl.botprzemek.bpSurvival.SurvivalManager.Drop.Items;
import pl.botprzemek.bpSurvival.SurvivalManager.Profile.Profile;
import pl.botprzemek.bpSurvival.SurvivalManager.Profile.ProfileManager;
import pl.botprzemek.bpSurvival.SurvivalManager.Profile.Settings;
import pl.botprzemek.bpSurvival.SurvivalManager.SurvivalManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BlockDestroyEvent implements Listener {

    private final ProfileManager profileManager;

    private final DropManager dropManager;

    public BlockDestroyEvent(SurvivalManager survivalManager) {

        profileManager = survivalManager.getProfileManager();

        dropManager = survivalManager.getDropManager();

    }

    @EventHandler
    public void onBlockDrop(BlockDropItemEvent event) {

        event.setCancelled(true);

        Player player = event.getPlayer();

        if (!player.getGameMode().equals(GameMode.SURVIVAL)) return;

        Profile profile = profileManager.getProfile(player);

        Settings settings = profile.getSettings();

        List<ItemStack> drops = new ArrayList<>();

        if (settings.isMinedBlock()) drops.add(event.getItems().get(0).getItemStack());

        Location location = event.getBlock().getLocation();

        ThreadLocalRandom random = ThreadLocalRandom.current();

        Items items = dropManager.getItems().get(random.nextInt(dropManager.getItems().size()));

        if (items.shouldDrop(random)) drops.add(items.createItem(random, settings.getMultiplier()));

        Inventory inventory = player.getInventory();

        World world = location.getWorld();

        if (!settings.isToInventory()) {

            for (ItemStack item : drops) { world.dropItemNaturally(location, item); }

            profile.setExp(profile.getExp() + settings.getMultiplier());

            int level = profile.getLevel();

            if (profile.getExp() < 50 * level) return;

            profile.setLevel(level + 1);

            profile.setExp(0);

            return;

        }

        for (ItemStack item : drops) {

            if (inventory.firstEmpty() != -1) inventory.addItem(item);

            else world.dropItem(location, item);

        }

        profile.setExp(profile.getExp() + settings.getMultiplier());

        int level = profile.getLevel();

        if (profile.getExp() < 50 * level) return;

        profile.setLevel(level + 1);

        profile.setExp(0);

    }

}
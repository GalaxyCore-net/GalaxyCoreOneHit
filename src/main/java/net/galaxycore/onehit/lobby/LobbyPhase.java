package net.galaxycore.onehit.lobby;

import lombok.Getter;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.onehit.utils.I18NUtils;
import net.galaxycore.onehit.utils.ObjectHelpers;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class LobbyPhase {
    private final HashMap<UUID, Long> boosters = new HashMap<>();

    public void setItems(Player player) {
        Inventory inventory = player.getInventory();

        // -> booster cleanup
        if (boosters.containsKey(player.getUniqueId()) && boosters.get(player.getUniqueId()) <= System.currentTimeMillis())
            boosters.remove(player.getUniqueId());

        // -> check if inventory is correct
        boolean correct = true;

        //noinspection RedundantIfStatement
        if (inventory.getItem(0) != null) correct = false;
        if (boosters.containsKey(player.getUniqueId())) {
            if (inventory.getItem(1) == null || ObjectHelpers.objectOrDefault(inventory.getItem(1), new ItemStack(Material.WRITTEN_BOOK)).getType() != Material.FIRE_CHARGE)
                correct = false;
        } else if (inventory.getItem(1) != null) correct = false;
        if (inventory.getItem(2) != null) correct = false;
        if (inventory.getItem(3) != null) correct = false;
        if (inventory.getItem(4) != null) correct = false;
        if (inventory.getItem(5) != null) correct = false;
        if (inventory.getItem(6) != null) correct = false;
        if (inventory.getItem(7) != null) correct = false;
        if (inventory.getItem(8) == null || ObjectHelpers.objectOrDefault(inventory.getItem(8), new ItemStack(Material.WRITTEN_BOOK)).getType() != Material.NETHER_STAR)
            correct = false;

        if (correct) // its correct
            return;

        // -> reset inventory
        // create items
        ItemStack booster = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta boosterItemMeta = booster.getItemMeta();
        boosterItemMeta.displayName(Component.text(I18NUtils.get(player, "booster")));
        booster.setItemMeta(boosterItemMeta);

        ItemStack settings = new ItemStack(Material.NETHER_STAR);
        ItemMeta settingsItemMeta = settings.getItemMeta();
        settingsItemMeta.displayName(Component.text(I18NUtils.get(player, "settings")));
        settings.setItemMeta(settingsItemMeta);

        // set inv
        inventory.clear();

        if (boosters.containsKey(player.getUniqueId()))
            inventory.setItem(1, booster);
        inventory.setItem(8, settings);
    }
}

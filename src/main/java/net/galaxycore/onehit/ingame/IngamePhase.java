package net.galaxycore.onehit.ingame;

import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.onehit.utils.ObjectHelpers;
import net.galaxycore.onehit.utils.SpawnHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class IngamePhase {
    public void setItems(Player player) {
        if (!(player.getInventory().getItem(0) == null && player.getInventory().getItem(1) == null)) return;
        if (ObjectHelpers.objectOrDefault(player.getInventory().getItem(0), new ItemStack(Material.FIRE)).getType() == Material.DIAMOND_SWORD &&
                ObjectHelpers.objectOrDefault(player.getInventory().getItem(1), new ItemStack(Material.FIRE)).getType() == Material.BOW)
            return;

        Inventory inventory = player.getInventory();
        inventory.clear();

        ItemStack sword = makeItem(Material.DIAMOND_SWORD, I18N.getByPlayer(player, "onehit.sword"));
        ItemStack bow = makeItem(Material.BOW, I18N.getByPlayer(player, "onehit.bow"));
        ItemStack arrow = makeItem(Material.ARROW, I18N.getByPlayer(player, "onehit.arrow"));

        inventory.setItem(0, sword);
        inventory.setItem(1, bow);
        inventory.setItem(8, arrow);

        player.updateInventory();
    }

    @SuppressWarnings("deprecation")
    public ItemStack makeItem(Material material, String displayName, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);
        return item;
    }
}

package net.galaxycore.onehit.lobby;

import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.galaxycorecore.configuration.internationalisation.I18N;
import net.galaxycore.onehit.OneHit;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SettingsMenu extends Menu {
    public SettingsMenu(Player player) {
        super(PlayerMenuUtility.getPlayerMenuUtility(player));
    }

    @Override
    public String getMenuName() {
        return I18N.getByPlayer(playerMenuUtility.getOwner(), "onehit.settings");
    }

    @Override
    public int getSlots() {
        return 9;
    }

    @Override
    public void handleMenu(InventoryClickEvent inventoryClickEvent) {
        if (inventoryClickEvent.getCurrentItem() == null) return;
        if (inventoryClickEvent.getCurrentItem().getType() == Material.FIRE_CHARGE && !OneHit.getInstance().getLobbyPhase().getBoosters().containsKey(playerMenuUtility.getOwner().getUniqueId())) {
            playerMenuUtility.getOwner().closeInventory();
            OneHit.getInstance().getLobbyPhase().getBoosters().put(playerMenuUtility.getOwner().getUniqueId(), System.currentTimeMillis() + 300000); // 5 Min into the future
            OneHit.getInstance().getLobbyPhase().setItems(playerMenuUtility.getOwner());
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack booster = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta boosterItemMeta = booster.getItemMeta();
        boosterItemMeta.displayName(Component.text(I18N.getByPlayer(playerMenuUtility.getOwner(), "onehit.settings.buy") + I18N.getByPlayer(playerMenuUtility.getOwner(), "onehit.booster")));
        boosterItemMeta.lore(List.of(
                Component.text(I18N.getByPlayer(playerMenuUtility.getOwner(), "onehit.settings.booster.lore")),
                OneHit.getInstance().getLobbyPhase().getBoosters().containsKey(playerMenuUtility.getOwner().getUniqueId()) ?
                        Component.text(I18N.getByPlayer(playerMenuUtility.getOwner(), "onehit.settings.alreadybought")) :
                        Component.text("")
        ));
        booster.setItemMeta(boosterItemMeta);

        inventory.setItem(4, booster);

        setFillerGlass();
    }
}

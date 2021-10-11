package net.galaxycore.onehit.lobby;

import me.kodysimpson.menumanagersystem.menusystem.Menu;
import me.kodysimpson.menumanagersystem.menusystem.PlayerMenuUtility;
import net.galaxycore.onehit.OneHit;
import net.galaxycore.onehit.bindings.CoinsBinding;
import net.galaxycore.onehit.listeners.MessageSetLoader;
import net.galaxycore.onehit.utils.I18NUtils;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.DataMutateResult;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
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
        return I18NUtils.get(playerMenuUtility.getOwner(), "settings");
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
            CoinsBinding coinsBinding = new CoinsBinding(playerMenuUtility.getOwner());
            if (coinsBinding.getCoins() < Integer.parseInt(OneHit.getInstance().getConfigNamespace().get("price.booster"))) {
                playerMenuUtility.getOwner().sendMessage(Component.text(I18NUtils.get(playerMenuUtility.getOwner(), "nomoney")));
                return;
            }
            coinsBinding.decrease(Integer.parseInt(OneHit.getInstance().getConfigNamespace().get("price.booster")), "ONEHIT_BUY_BOOSTER");
            OneHit.getInstance().getLobbyPhase().getBoosters().put(playerMenuUtility.getOwner().getUniqueId(), System.currentTimeMillis() + 300000); // 5 Min into the future
            OneHit.getInstance().getLobbyPhase().setItems(playerMenuUtility.getOwner());
        }
        if (inventoryClickEvent.getCurrentItem().getType() == Material.BOOK) {
            playerMenuUtility.getOwner().closeInventory();
            CoinsBinding coinsBinding = new CoinsBinding(playerMenuUtility.getOwner());
            if ((inventoryClickEvent.getSlot() - 5 != 0) && !playerMenuUtility.getOwner().hasPermission("onehit.messageset." + (inventoryClickEvent.getSlot() - 5))) {
                int price = Integer.parseInt(OneHit.getInstance().getConfigNamespace().get("price.messageset." + (inventoryClickEvent.getSlot() - 6)));
                if ((inventoryClickEvent.getSlot() - 5 == 0) || (coinsBinding.getCoins() < price)) {
                    playerMenuUtility.getOwner().sendMessage(Component.text(I18NUtils.get(playerMenuUtility.getOwner(), "nomoney")));
                    return;
                }
                coinsBinding.decrease(Integer.parseInt(OneHit.getInstance().getConfigNamespace().get("price.messageset." + (inventoryClickEvent.getSlot() - 6))), "ONEHIT_BUY_MSG_" + (inventoryClickEvent.getSlot() - 5));
                String permission = "onehit.messageset." + (inventoryClickEvent.getSlot() - 5);
                LuckPerms luckPerms = LuckPermsProvider.get();
                Node node = Node.builder(permission).build();
                luckPerms.getUserManager().modifyUser(playerMenuUtility.getOwner().getUniqueId(), (User user) -> user.data().add(node));
                playerMenuUtility.getOwner().sendMessage(Component.text(I18NUtils.get(playerMenuUtility.getOwner(), "settings.setchosen")));
            }
            MessageSetLoader.set(playerMenuUtility.getOwner(), inventoryClickEvent.getSlot() - 5);
            MessageSetLoader.reloadPlayer(playerMenuUtility.getOwner());
        }
    }

    @Override
    public void setMenuItems() {
        ItemStack booster = new ItemStack(Material.FIRE_CHARGE);
        ItemMeta boosterItemMeta = booster.getItemMeta();
        boosterItemMeta.displayName(Component.text(I18NUtils.get(playerMenuUtility.getOwner(), "settings.buy") + I18NUtils.get(playerMenuUtility.getOwner(), "booster")));
        boosterItemMeta.lore(List.of(
                Component.text(I18NUtils.get(playerMenuUtility.getOwner(), "settings.booster.lore")),
                OneHit.getInstance().getLobbyPhase().getBoosters().containsKey(playerMenuUtility.getOwner().getUniqueId()) ?
                        Component.text(I18NUtils.get(playerMenuUtility.getOwner(), "settings.alreadybought")) :
                        Component.text(I18NUtils.get(playerMenuUtility.getOwner(), "settings.booster.price"))
        ));
        booster.setItemMeta(boosterItemMeta);

        int price1 = Integer.parseInt(OneHit.getInstance().getConfigNamespace().get("price.messageset.0"));
        int price2 = Integer.parseInt(OneHit.getInstance().getConfigNamespace().get("price.messageset.1"));
        int price3 = Integer.parseInt(OneHit.getInstance().getConfigNamespace().get("price.messageset.2"));

        ItemStack messageSet0 = makeItem(MessageSetLoader.get(playerMenuUtility.getOwner()) == 0 ? Material.ENCHANTED_BOOK : Material.BOOK, I18NUtils.get(playerMenuUtility.getOwner(), "settings.messageset.0.name"));
        ItemStack messageSet1 = makeItem(MessageSetLoader.get(playerMenuUtility.getOwner()) == 1 ? Material.ENCHANTED_BOOK : Material.BOOK, (playerMenuUtility.getOwner().hasPermission("onehit.messageset.1") ? "" : I18NUtils.get(playerMenuUtility.getOwner(), "settings.buy")) + I18NUtils.get(playerMenuUtility.getOwner(), "settings.messageset.1.name"), (playerMenuUtility.getOwner().hasPermission("onehit.messageset.1") ? I18NUtils.get(playerMenuUtility.getOwner(), "settings.alreadybought") : "§7" + price1 + " " + I18NUtils.get(playerMenuUtility.getOwner(), "settings.coins")));
        ItemStack messageSet2 = makeItem(MessageSetLoader.get(playerMenuUtility.getOwner()) == 2 ? Material.ENCHANTED_BOOK : Material.BOOK, (playerMenuUtility.getOwner().hasPermission("onehit.messageset.2") ? "" : I18NUtils.get(playerMenuUtility.getOwner(), "settings.buy")) + I18NUtils.get(playerMenuUtility.getOwner(), "settings.messageset.2.name"), (playerMenuUtility.getOwner().hasPermission("onehit.messageset.2") ? I18NUtils.get(playerMenuUtility.getOwner(), "settings.alreadybought") : "§7" + price2 + " " + I18NUtils.get(playerMenuUtility.getOwner(), "settings.coins")));

        inventory.setItem(2, booster);
        inventory.setItem(5, messageSet0);
        inventory.setItem(6, messageSet1);
        inventory.setItem(7, messageSet2);

        if (playerMenuUtility.getOwner().hasPermission("onehit.teammsg")) {
            ItemStack messageSet3 = makeItem(MessageSetLoader.get(playerMenuUtility.getOwner()) == 3 ? Material.ENCHANTED_BOOK : Material.BOOK, (playerMenuUtility.getOwner().hasPermission("onehit.messageset.3") ? "" : I18NUtils.get(playerMenuUtility.getOwner(), "settings.buy")) + I18NUtils.get(playerMenuUtility.getOwner(), "settings.messageset.3.name"), (playerMenuUtility.getOwner().hasPermission("onehit.messageset.3") ? I18NUtils.get(playerMenuUtility.getOwner(), "settings.alreadybought") : "§7" + price3 + " " + I18NUtils.get(playerMenuUtility.getOwner(), "settings.coins")));
            inventory.setItem(8, messageSet3);
        }

        setFillerGlass();
    }
}

package de.mari_023.fabric.ae2wtlib.wct;

import appeng.api.implementations.blockentities.IViewCellStorage;
import appeng.api.inventories.ISegmentedInventory;
import appeng.api.inventories.InternalInventory;
import appeng.core.definitions.AEItems;
import appeng.menu.ISubMenu;
import appeng.parts.reporting.CraftingTerminalPart;
import appeng.util.inv.AppEngInternalInventory;
import appeng.util.inv.InternalInventoryHost;
import de.mari_023.fabric.ae2wtlib.terminal.WTMenuHost;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class WCTMenuHost extends WTMenuHost implements IViewCellStorage, ISegmentedInventory, InternalInventoryHost {
    private final AppEngInternalInventory craftingGrid = new AppEngInternalInventory(this, 9);

    public WCTMenuHost(final Player ep, @Nullable Integer inventorySlot, final ItemStack is, BiConsumer<Player, ISubMenu> returnToMainMenu) {
        super(ep, inventorySlot, is, returnToMainMenu);
        craftingGrid.readFromNBT(getItemStack().getOrCreateTag(), "craftingGrid");
    }

    @Override
    public MenuType<?> getType() {
        return WCTMenu.TYPE;
    }

    @Override
    public ItemStack getMainMenuIcon() {
        return new ItemStack(AEItems.WIRELESS_CRAFTING_TERMINAL);
    }

    @Nullable
    @Override
    public InternalInventory getSubInventory(ResourceLocation id) {
        if(id.equals(CraftingTerminalPart.INV_CRAFTING)) return craftingGrid;
        else return null;
    }

    @Override
    public void saveChanges() {
        craftingGrid.writeToNBT(getItemStack().getOrCreateTag(), "craftingGrid");
    }

    @Override
    public void onChangeInventory(InternalInventory inv, int slot) {
    }
}
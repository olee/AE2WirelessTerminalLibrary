package de.mari_023.fabric.ae2wtlib.terminal;

import appeng.api.inventories.InternalInventory;
import appeng.items.storage.ViewCellItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;

public class ViewCellInventory implements InternalInventory {

    private static final int viewCellCount = 5;
    private final ItemStack hostStack;

    public ViewCellInventory(ItemStack host) {
        hostStack = host;
    }

    @Override
    public int size() {
        return viewCellCount;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return ItemWT.getSavedSlot(hostStack, "viewCell" + i);
    }

    @Override
    public void setItemDirect(int i, @NotNull ItemStack itemStack) {
        ItemWT.setSavedSlot(hostStack, itemStack, "viewCell" + i);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack itemStack) {
        return slot < viewCellCount && (itemStack.getItem() instanceof ViewCellItem || itemStack.isEmpty());
    }

    public List<ItemStack> getViewCells() {
        List<ItemStack> viewCells = new ArrayList<>();
        for(int i = 0; i < viewCellCount; i++) viewCells.add(getStackInSlot(i));
        return viewCells;
    }
}
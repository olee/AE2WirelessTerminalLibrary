package de.mari_023.fabric.ae2wtlib.wet;

import appeng.api.config.SecurityPermissions;
import appeng.api.networking.IGridNode;
import appeng.menu.SlotSemantic;
import appeng.menu.implementations.MenuTypeBuilder;
import appeng.menu.me.items.PatternTermMenu;
import appeng.menu.slot.AppEngSlot;
import de.mari_023.fabric.ae2wtlib.terminal.WTInventory;
import de.mari_023.fabric.ae2wtlib.terminal.IWTInvHolder;
import de.mari_023.fabric.ae2wtlib.terminal.ItemWT;
import de.mari_023.fabric.ae2wtlib.wut.ItemWUT;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;

import java.util.List;

public class WETMenu extends PatternTermMenu implements IWTInvHolder {

    public static final ScreenHandlerType<WETMenu> TYPE = MenuTypeBuilder.create(WETMenu::new, WETMenuHost.class).requirePermission(SecurityPermissions.CRAFT).build("wireless_pattern_encoding_terminal");

    private final WETMenuHost WETGUIObject;

    public WETMenu(int id, final PlayerInventory ip, final WETMenuHost gui) {
        super(TYPE, id, ip, gui, false);
        WETGUIObject = gui;

        final int slotIndex =  WETGUIObject.getSlot();
        if(slotIndex < 100 && slotIndex != 40) lockPlayerInventorySlot(slotIndex);
        createPlayerInventorySlots(ip);
        addSlot(new AppEngSlot(new WTInventory(getPlayerInventory(), WETGUIObject.getItemStack(), this), WTInventory.INFINITY_BOOSTER_CARD), SlotSemantic.BIOMETRIC_CARD);

        if(isClient()) {//FIXME set craftingMode and substitute serverside
            setCraftingMode(ItemWT.getBoolean(WETGUIObject.getItemStack(), "craftingMode"));
            setSubstitute(ItemWT.getBoolean(WETGUIObject.getItemStack(), "substitute"));
            setSubstituteFluids(ItemWT.getBoolean(WETGUIObject.getItemStack(), "substitute_fluids"));
        }
    }

    public boolean isCraftingMode() {
        return WETGUIObject.isCraftingRecipe();
    }

    public void setCraftingMode(boolean craftingMode) {
        super.setCraftingMode(craftingMode);
        WETGUIObject.setCraftingRecipe(craftingMode);
    }

    public boolean isSubstitute() {
        return WETGUIObject.isSubstitution();
    }

    public void setSubstitute(boolean substitute) {
        super.setSubstitute(substitute);
        WETGUIObject.setSubstitution(substitute);
    }

    public boolean isSubstituteFluids() {
        return WETGUIObject.isFluidSubstitution();
    }

    public void setSubstituteFluids(boolean substituteFluids) {
        super.setSubstituteFluids(substituteFluids);
        WETGUIObject.setFluidSubstitution(substituteFluids);
    }

    @Override
    public IGridNode getNetworkNode() {
        return WETGUIObject.getActionableNode();
    }

    public boolean isWUT() {
        return WETGUIObject.getItemStack().getItem() instanceof ItemWUT;
    }

    @Override
    public List<ItemStack> getViewCells() {
        return WETGUIObject.getViewCellStorage().getViewCells();
    }
}
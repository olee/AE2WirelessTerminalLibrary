package de.mari_023.fabric.ae2wtlib.wct.magnet_card;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.KeyCounter;
import de.mari_023.fabric.ae2wtlib.AE2wtlib;
import de.mari_023.fabric.ae2wtlib.AE2wtlibConfig;
import de.mari_023.fabric.ae2wtlib.terminal.ItemWT;
import de.mari_023.fabric.ae2wtlib.wct.CraftingTerminalHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MagnetHandler {
    public void doMagnet(MinecraftServer server) {
        List<ServerPlayer> playerList = server.getPlayerList().getPlayers();
        for(ServerPlayer player : playerList) {
            if(ItemMagnetCard.isActiveMagnet(CraftingTerminalHandler.getCraftingTerminalHandler(player).getCraftingTerminal())) {
                List<ItemEntity> entityItems = player.getLevel().getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(AE2wtlibConfig.INSTANCE.magnetCardRange()), EntitySelector.ENTITY_STILL_ALIVE);
                boolean sneaking = !player.isShiftKeyDown();
                for(ItemEntity entityItemNearby : entityItems) if(sneaking) entityItemNearby.playerTouch(player);
            }
            sendRestockAble(player);
        }
    }

    public void sendRestockAble(ServerPlayer player) {
        try {
            CraftingTerminalHandler handler = CraftingTerminalHandler.getCraftingTerminalHandler(player);
            if(player.isCreative() || !ItemWT.getBoolean(handler.getCraftingTerminal(), "restock") || !handler.inRange())
                return;
            HashMap<Item, Long> items = new HashMap<>();

            if(handler.getItemStorageChannel() == null) return;
            KeyCounter storageList = handler.getItemStorageChannel().getAvailableStacks();

            for(int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if(stack.isEmpty()) continue;
                if(!items.containsKey(stack.getItem())) {
                    AEItemKey key = AEItemKey.of(stack);
                    if(key == null) items.put(stack.getItem(), 0L);
                    else items.put(stack.getItem(), storageList.get(key));
                }
            }

            FriendlyByteBuf buf = PacketByteBufs.create();
            for(Map.Entry<Item, Long> entry : items.entrySet()) {
                buf.writeItem(new ItemStack(entry.getKey()));
                buf.writeLong(entry.getValue());
            }
            ServerPlayNetworking.send(player, new ResourceLocation(AE2wtlib.MOD_NAME, "restock_amounts"), buf);
        } catch(NullPointerException ignored) {}
    }
}
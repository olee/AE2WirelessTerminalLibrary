package de.mari_023.fabric.ae2wtlib.client;

import de.mari_023.fabric.ae2wtlib.AE2wtlib;
import de.mari_023.fabric.ae2wtlib.AE2wtlibConfig;
import de.mari_023.fabric.ae2wtlib.trinket.TrinketsHelper;
import de.mari_023.fabric.ae2wtlib.wct.CraftingTerminalHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.HashMap;

public class NetworkingClient {
    public static void registerClient() {
        ClientPlayNetworking.registerGlobalReceiver(new ResourceLocation(AE2wtlib.MOD_NAME, "update_restock"), (client, handler, buf, responseSender) -> {
            buf.retain();
            client.execute(() -> {
                if(client.player == null) return;
                client.player.getInventory().getItem(buf.readInt()).setCount(buf.readInt());
                buf.release();
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(new ResourceLocation(AE2wtlib.MOD_NAME, "update_wut"), (client, handler, buf, responseSender) -> {
            buf.retain();
            client.execute(() -> {//TODO use locator
                if(client.player == null) return;
                int slot = buf.readInt();
                ItemStack is;
                CompoundTag tag = buf.readNbt();
                if(slot >= 100 && slot < 200 && AE2wtlibConfig.INSTANCE.allowTrinket())
                    is = TrinketsHelper.getTrinketsInventory(client.player).getStackInSlot(slot - 100);
                else is = client.player.getInventory().getItem(slot);
                is.setTag(tag);
                buf.release();
                CraftingTerminalHandler.getCraftingTerminalHandler(client.player).invalidateCache();
            });
        });
        ClientPlayNetworking.registerGlobalReceiver(new ResourceLocation(AE2wtlib.MOD_NAME, "restock_amounts"), (client, handler, buf, responseSender) -> {
            buf.retain();
            client.execute(() -> {
                if(client.player == null) return;
                CraftingTerminalHandler ctHandler = CraftingTerminalHandler.getCraftingTerminalHandler(client.player);
                HashMap<Item, Long> items = new HashMap<>();
                while(buf.isReadable()) items.put(buf.readItem().getItem(), buf.readLong());
                ctHandler.setRestockAbleItems(items);
                buf.release();
            });
        });
    }
}

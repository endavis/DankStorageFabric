package tfar.dankstorage.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import tfar.dankstorage.DankStorage;
import tfar.dankstorage.network.server.*;
import tfar.dankstorage.utils.PacketBufferEX;

public class DankPacketHandler {

    public static final ResourceLocation toggle_pickup = new ResourceLocation(DankStorage.MODID, "toggle_pickup");
    public static final ResourceLocation tag_mode = new ResourceLocation(DankStorage.MODID, "tag_mode");
    public static final ResourceLocation sort = new ResourceLocation(DankStorage.MODID, "sort");
    public static final ResourceLocation lock_slot = new ResourceLocation(DankStorage.MODID, "lock_slot");
    public static final ResourceLocation sync_slot = new ResourceLocation(DankStorage.MODID, "sync_slot");
    public static final ResourceLocation sync_container = new ResourceLocation(DankStorage.MODID, "sync_container");
    public static final ResourceLocation pick_block = new ResourceLocation(DankStorage.MODID, "pick_block");
    public static final ResourceLocation toggle_use = new ResourceLocation(DankStorage.MODID, "toggle_use");
    public static final ResourceLocation scroll = new ResourceLocation(DankStorage.MODID, "scroll");
    public static final ResourceLocation sync_data = new ResourceLocation(DankStorage.MODID, "sync_data");
    public static final ResourceLocation set_id = new ResourceLocation(DankStorage.MODID, "set_id");

    public static void registerMessages() {
        ServerPlayNetworking.registerGlobalReceiver(scroll, new C2SMessageScrollSlot());
        ServerPlayNetworking.registerGlobalReceiver(lock_slot, new C2SMessageLockSlot());
        ServerPlayNetworking.registerGlobalReceiver(sort, new C2SMessageSort());
        ServerPlayNetworking.registerGlobalReceiver(tag_mode, new C2SMessageTagMode());
        ServerPlayNetworking.registerGlobalReceiver(toggle_pickup, new C2SMessageTogglePickup());
        ServerPlayNetworking.registerGlobalReceiver(toggle_use, new C2SMessageToggleUseType());
        ServerPlayNetworking.registerGlobalReceiver(pick_block, new C2SMessagePickBlock());
        ServerPlayNetworking.registerGlobalReceiver(set_id, new C2SSetIDPacket());
    }

    public static void sendSyncSlot(ServerPlayer player, int id, int slot, ItemStack stack) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(id);
        buf.writeInt(slot);
        PacketBufferEX.writeExtendedItemStack(buf, stack);
        ServerPlayNetworking.send(player, DankPacketHandler.sync_slot, buf);
    }

    public static void sendSyncContainer(ServerPlayer player,int stateID, int containerID, NonNullList<ItemStack> stacks,ItemStack carried) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(stateID);
        buf.writeInt(containerID);

        buf.writeItem(carried);

        buf.writeShort(stacks.size());

        for (ItemStack stack : stacks) {
            PacketBufferEX.writeExtendedItemStack(buf, stack);
        }

        ServerPlayNetworking.send(player, DankPacketHandler.sync_container, buf);
    }

    public static void sendSelectedItem(ServerPlayer player, int id, ItemStack stack, C2SMessageToggleUseType.UseType useType) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        buf.writeInt(id);
        PacketBufferEX.writeExtendedItemStack(buf, stack);
        buf.writeInt(useType.ordinal());
        ServerPlayNetworking.send(player, DankPacketHandler.sync_data, buf);
    }

}

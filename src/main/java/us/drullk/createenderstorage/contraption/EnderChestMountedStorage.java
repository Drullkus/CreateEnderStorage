package us.drullk.createenderstorage.contraption;

import codechicken.enderstorage.api.Frequency;
import codechicken.enderstorage.config.EnderStorageConfig;
import codechicken.enderstorage.manager.EnderStorageManager;
import codechicken.enderstorage.storage.EnderItemStorage;
import codechicken.enderstorage.tile.TileEnderChest;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorage;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.foundation.utility.CreateLang;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import us.drullk.createenderstorage.CreateEnderStorage;

public class EnderChestMountedStorage extends MountedItemStorage {

    public static final MapCodec<EnderChestMountedStorage> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Frequency.CODEC.fieldOf("chest_frequency").forGetter(EnderChestMountedStorage::getFrequency),
            Codec.INT.fieldOf("rotation").forGetter(EnderChestMountedStorage::getRotation)
    ).apply(inst, EnderChestMountedStorage::new));

    private final Frequency frequency;
    private final int rotation;

    private @Nullable IItemHandler itemHandler;
    private @Nullable EnderItemStorage storage;

    protected EnderChestMountedStorage(Frequency frequency, int rotation) {
        super(CreateEnderStorage.ENDER_CHEST.value());

        this.frequency = frequency;
        this.rotation = rotation;
    }

    public Frequency getFrequency() {
        return this.frequency;
    }

    public int getRotation() {
        return this.rotation;
    }

    public IItemHandler getItemHandler() {
        if (this.itemHandler == null) {
            this.itemHandler = new InvWrapper(this.getStorage());
        }

        return this.itemHandler;
    }

    public EnderItemStorage getStorage() {
        if (this.storage == null) {
            this.storage = EnderStorageManager.instance(false).getStorage(this.frequency, EnderItemStorage.TYPE);
        }

        return this.storage;
    }

    @Override
    public void unmount(Level level, BlockState blockState, BlockPos blockPos, @Nullable BlockEntity blockEntity) {
        if (!(blockEntity instanceof TileEnderChest enderChest)) return;

        if (level instanceof ServerLevel serverLevel) {
            ClientboundBlockUpdatePacket packet = new ClientboundBlockUpdatePacket(blockPos, blockState);
            for (ServerPlayer player : serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(blockPos), false)) {
                player.connection.send(packet);
            }
        }
        // The BlockEntity data set below will synchronize to the client before the blockstate is set in the client's world,
        // requiring that the ClientboundBlockUpdatePacket is manually sent ahead of the usual chunk-batched updates.
        enderChest.rotation = Math.floorMod(this.rotation, 4);
        enderChest.setFreq(this.frequency);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        this.getStorage().setItem(slot, stack);
    }

    @Override
    public int getSlots() {
        // Create collects inventory sizes into MountedItemStorageWrapper which can include when the client is still on the loading chunks screen.
        // Apparently this is too early for Ender Storage mod (EnderChestMountedStorage#getStorage), thus causing Ender Storage to pass a networked Container that gets disposed of after the chunks screen.
        // Fetching the chest sizes from the mod's lookup table via config value instead avoids this initialization problem.
        return EnderItemStorage.sizes[EnderStorageConfig.storageSize];
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.getStorage().getItem(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return this.getItemHandler().insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.getItemHandler().extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.getItemHandler().getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return this.getItemHandler().isItemValid(slot, stack);
    }

    @Override
    public boolean handleInteraction(ServerPlayer player, Contraption contraption, StructureTemplate.StructureBlockInfo info) {
        MutableComponent enderChestName = Component.translatable(info.state().getBlock().getDescriptionId());
        MutableComponent movingName = CreateLang.translateDirect("contraptions.moving_container", enderChestName);
        this.getStorage().openContainer(player, movingName);
        return true;
    }
}

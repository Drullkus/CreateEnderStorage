package us.drullk.createenderstorage.contraption;

import codechicken.enderstorage.api.Frequency;
import codechicken.enderstorage.manager.EnderStorageManager;
import codechicken.enderstorage.storage.EnderLiquidStorage;
import codechicken.enderstorage.tile.TileEnderTank;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.simibubi.create.api.contraption.storage.fluid.MountedFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;
import us.drullk.createenderstorage.CreateEnderStorage;

public class EnderTankMountedStorage extends MountedFluidStorage {

    public static final MapCodec<EnderTankMountedStorage> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            Frequency.CODEC.fieldOf("tank_frequency").forGetter(EnderTankMountedStorage::getFrequency),
            Codec.INT.fieldOf("rotation").forGetter(EnderTankMountedStorage::getRotation)
    ).apply(inst, EnderTankMountedStorage::new));

    private final Frequency frequency;
    private final int rotation;

    private @Nullable EnderLiquidStorage storage;

    protected EnderTankMountedStorage(Frequency frequency, int rotation) {
        super(CreateEnderStorage.ENDER_TANK.value());

        this.frequency = frequency;
        this.rotation = rotation;
    }

    public Frequency getFrequency() {
        return this.frequency;
    }

    public int getRotation() {
        return this.rotation;
    }

    public EnderLiquidStorage getStorage() {
        if (this.storage == null) {
            this.storage = EnderStorageManager.instance(false).getStorage(this.frequency, EnderLiquidStorage.TYPE);
        }

        return this.storage;
    }

    @Override
    public void unmount(Level level, BlockState blockState, BlockPos blockPos, @Nullable BlockEntity blockEntity) {
        if (!(blockEntity instanceof TileEnderTank enderTank)) return;

        if (level instanceof ServerLevel serverLevel) {
            ClientboundBlockUpdatePacket packet = new ClientboundBlockUpdatePacket(blockPos, blockState);
            for (ServerPlayer player : serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(blockPos), false)) {
                player.connection.send(packet);
            }
        }
        // The BlockEntity data set below will synchronize to the client before the blockstate is set in the client's world,
        // requiring that the ClientboundBlockUpdatePacket is manually sent ahead of the usual chunk-batched updates.
        enderTank.rotation = Math.floorMod(this.rotation - 1, 4);
        enderTank.setFreq(this.frequency);
    }

    @Override
    public int getTanks() {
        // Similar fix like as EnderChestMountedStorage#getSlots, except tank-slots don't ever change.
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        return this.getStorage().getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return this.getStorage().getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        return this.getStorage().isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack stack, FluidAction action) {
        return this.getStorage().fill(stack, action);
    }

    @Override
    public FluidStack drain(FluidStack stack, FluidAction action) {
        return this.getStorage().drain(stack, action);
    }

    @Override
    public FluidStack drain(int tank, FluidAction action) {
        return this.getStorage().drain(tank, action);
    }

}

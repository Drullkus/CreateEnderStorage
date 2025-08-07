package us.drullk.createenderstorage.contraption;

import codechicken.enderstorage.api.Frequency;
import codechicken.enderstorage.manager.EnderStorageManager;
import codechicken.enderstorage.storage.EnderLiquidStorage;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.api.contraption.storage.fluid.MountedFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;
import us.drullk.createenderstorage.CreateEnderStorage;

public class EnderTankMountedStorage extends MountedFluidStorage {

    public static final MapCodec<EnderTankMountedStorage> CODEC = Frequency.CODEC.xmap(EnderTankMountedStorage::new, EnderTankMountedStorage::getFrequency).fieldOf("tank_frequency");

    private final Frequency frequency;

    private @Nullable IFluidHandler fluidHandler;

    protected EnderTankMountedStorage(Frequency frequency) {
        super(CreateEnderStorage.ENDER_TANK.value());
        this.frequency = frequency;
    }

    public Frequency getFrequency() {
        return this.frequency;
    }

    public IFluidHandler getFluidHandler() {
        if (this.fluidHandler == null)
            this.fluidHandler = this.getStorage();

        return this.fluidHandler;
    }

    public EnderLiquidStorage getStorage() {
        return EnderStorageManager.instance(false).getStorage(this.frequency, EnderLiquidStorage.TYPE);
    }

    @Override
    public void unmount(Level level, BlockState blockState, BlockPos blockPos, @Nullable BlockEntity blockEntity) {
        // TODO set freq in placed block's entity?
    }

    @Override
    public int getTanks() {
        return 0;
    }

    @Override
    public FluidStack getFluidInTank(int i) {
        return null;
    }

    @Override
    public int getTankCapacity(int i) {
        return 0;
    }

    @Override
    public boolean isFluidValid(int i, FluidStack fluidStack) {
        return false;
    }

    @Override
    public int fill(FluidStack fluidStack, FluidAction fluidAction) {
        return 0;
    }

    @Override
    public FluidStack drain(FluidStack fluidStack, FluidAction fluidAction) {
        return null;
    }

    @Override
    public FluidStack drain(int i, FluidAction fluidAction) {
        return null;
    }

}

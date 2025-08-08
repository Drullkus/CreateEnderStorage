package us.drullk.createenderstorage.contraption;

import codechicken.enderstorage.tile.TileEnderTank;
import com.simibubi.create.api.contraption.storage.fluid.MountedFluidStorageType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class EnderTankMountedStorageType extends MountedFluidStorageType<EnderTankMountedStorage> {

    public EnderTankMountedStorageType() {
        super(EnderTankMountedStorage.CODEC);
    }

    @Override
    public @Nullable EnderTankMountedStorage mount(Level level, BlockState blockState, BlockPos blockPos, @Nullable BlockEntity blockEntity) {
        if (blockEntity instanceof TileEnderTank enderTank)
            return new EnderTankMountedStorage(enderTank.getFrequency(), enderTank.rotation);

        return null;
    }

}

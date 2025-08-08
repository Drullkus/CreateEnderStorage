package us.drullk.createenderstorage.contraption;

import codechicken.enderstorage.tile.TileEnderChest;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import us.drullk.createenderstorage.CreateEnderStorage;

public class EnderChestMountedStorageType extends MountedItemStorageType<EnderChestMountedStorage> {

    public EnderChestMountedStorageType() {
        super(EnderChestMountedStorage.CODEC);

        CreateEnderStorage.LOGGER.info("EnderChestMountedStorageType created");
    }

    @Override
    public @Nullable EnderChestMountedStorage mount(Level level, BlockState blockState, BlockPos blockPos, @Nullable BlockEntity blockEntity) {
        if (blockEntity instanceof TileEnderChest enderChest)
            return new EnderChestMountedStorage(enderChest.getFrequency(), enderChest.rotation);

        return null;
    }

}

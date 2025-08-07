package us.drullk.createenderstorage.contraption;

import codechicken.enderstorage.api.Frequency;
import codechicken.enderstorage.manager.EnderStorageManager;
import codechicken.enderstorage.storage.EnderItemStorage;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorage;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.Nullable;
import us.drullk.createenderstorage.CreateEnderStorage;

public class EnderChestMountedStorage extends MountedItemStorage {

    public static final MapCodec<EnderChestMountedStorage> CODEC = Frequency.CODEC.xmap(EnderChestMountedStorage::new, EnderChestMountedStorage::getFrequency).fieldOf("tank_frequency");

    private final Frequency frequency;

    private @Nullable IItemHandler itemHandler;
    private @Nullable EnderItemStorage storage;

    public EnderChestMountedStorage(Frequency frequency) {
        super(CreateEnderStorage.ENDER_CHEST.value());

        this.frequency = frequency;
    }

    public Frequency getFrequency() {
        return this.frequency;
    }

    public IItemHandler getItemHandler() {
        if (this.itemHandler == null)
            this.itemHandler = new InvWrapper(this.getStorage());

        return this.itemHandler;
    }

    public EnderItemStorage getStorage() {
        if (this.storage == null)
            this.storage = EnderStorageManager.instance(false).getStorage(this.frequency, EnderItemStorage.TYPE);

        return this.storage;
    }

    @Override
    public void unmount(Level level, BlockState blockState, BlockPos blockPos, @Nullable BlockEntity blockEntity) {
        // TODO set freq in placed block's entity?
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        this.getStorage().setItem(slot, stack);
    }

    @Override
    public int getSlots() {
        return this.getStorage().getContainerSize();
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
        // TODO How does Ender Chest open the menu?
        return super.handleInteraction(player, contraption, info);
    }
}

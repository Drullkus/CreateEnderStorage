package us.drullk.createenderstorage;

import com.simibubi.create.api.contraption.storage.fluid.MountedFluidStorageType;
import com.simibubi.create.api.contraption.storage.item.MountedItemStorageType;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.fml.common.Mod;
import us.drullk.createenderstorage.contraption.EnderChestMountedStorageType;
import us.drullk.createenderstorage.contraption.EnderTankMountedStorageType;

@Mod(CreateEnderStorage.MODID)
public class CreateEnderStorage {

    public static final String MODID = "createenderstorage";
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    static final TagKey<Block> ENDER_CHEST_TAG = TagKey.create(Registries.BLOCK, modId("_ender_chest"));
    static final TagKey<Block> ENDER_TANK_TAG = TagKey.create(Registries.BLOCK, modId("_ender_tank"));

    public static final RegistryEntry<MountedItemStorageType<?>, EnderChestMountedStorageType> ENDER_CHEST = REGISTRATE.mountedItemStorage("ender_chest", EnderChestMountedStorageType::new)
            .associateBlockTag(ENDER_CHEST_TAG)
            .register();
    public static final RegistryEntry<MountedFluidStorageType<?>, EnderTankMountedStorageType> ENDER_TANK = REGISTRATE.mountedFluidStorage("ender_tank", EnderTankMountedStorageType::new)
            .associateBlockTag(ENDER_TANK_TAG)
            .register();

    public CreateEnderStorage(IEventBus modBus) {
        REGISTRATE.registerEventListeners(modBus);
    }

    public static ResourceLocation modId(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

}

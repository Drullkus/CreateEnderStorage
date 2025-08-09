package us.drullk.createenderstorage.ponder;

import codechicken.enderstorage.init.EnderStorageModContent;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import us.drullk.createenderstorage.CreateEnderStorage;

@EventBusSubscriber(modid = CreateEnderStorage.MODID, value = Dist.CLIENT)
public class PonderEnderStorage implements PonderPlugin {

    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new PonderEnderStorage());
    }

    @Override
    public String getModId() {
        return CreateEnderStorage.MODID;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> resourceLocationHelper) {
        PonderSceneRegistrationHelper<DeferredHolder<Block, ? extends Block>> helper = resourceLocationHelper.withKeyFunction(DeferredHolder::getId);

        helper.forComponents(EnderStorageModContent.ENDER_CHEST_BLOCK, EnderStorageModContent.ENDER_TANK_BLOCK)
                .addStoryBoard("mounted_ender_chest", EnderStorageScenes::enderChest, ResourceLocation.fromNamespaceAndPath("create", "logistics"));
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        helper.addToTag(ResourceLocation.fromNamespaceAndPath("create", "logistics")).add(EnderStorageModContent.ENDER_CHEST_BLOCK.getId());
    }
}

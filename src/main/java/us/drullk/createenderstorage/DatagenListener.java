package us.drullk.createenderstorage;

import codechicken.enderstorage.init.EnderStorageModContent;
import net.minecraft.core.HolderLookup;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = CreateEnderStorage.MODID)
public class DatagenListener {

    @SubscribeEvent
    public static void onData(GatherDataEvent event) {
        event.addProvider(new BlockTagsProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), CreateEnderStorage.MODID, event.getExistingFileHelper()) {
            @Override
            protected void addTags(HolderLookup.Provider provider) {
                this.tag(CreateEnderStorage.ENDER_CHEST_TAG).add(EnderStorageModContent.ENDER_CHEST_BLOCK.value());
                this.tag(CreateEnderStorage.ENDER_TANK_TAG).add(EnderStorageModContent.ENDER_TANK_BLOCK.value());
            }
        });
    }

}

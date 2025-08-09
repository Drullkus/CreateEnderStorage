package us.drullk.createenderstorage;

import codechicken.enderstorage.init.EnderStorageModContent;
import com.tterrag.registrate.providers.ProviderType;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.HolderLookup;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import us.drullk.createenderstorage.ponder.PonderEnderStorage;

@EventBusSubscriber(modid = CreateEnderStorage.MODID)
public class DatagenListener {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onDataEarly(GatherDataEvent event) {
        CreateEnderStorage.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            PonderIndex.addPlugin(new PonderEnderStorage());
            PonderIndex.getLangAccess().provideLang(CreateEnderStorage.MODID, provider::add);
        });
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onDataLate(GatherDataEvent event) {
        event.addProvider(new BlockTagsProvider(event.getGenerator().getPackOutput(), event.getLookupProvider(), CreateEnderStorage.MODID, event.getExistingFileHelper()) {
            @Override
            protected void addTags(HolderLookup.Provider provider) {
                this.tag(CreateEnderStorage.ENDER_CHEST_TAG).add(EnderStorageModContent.ENDER_CHEST_BLOCK.value());
                this.tag(CreateEnderStorage.ENDER_TANK_TAG).add(EnderStorageModContent.ENDER_TANK_BLOCK.value());
            }
        });
    }

}

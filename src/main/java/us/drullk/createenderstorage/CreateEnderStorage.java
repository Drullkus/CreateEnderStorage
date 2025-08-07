package us.drullk.createenderstorage;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(CreateEnderStorage.MODID)
public class CreateEnderStorage {

    public static final String MODID = "createenderstorage";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateEnderStorage(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

}

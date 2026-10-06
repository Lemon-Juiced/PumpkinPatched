package site.scalarstudios.pumpkinpatched;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import site.scalarstudios.pumpkinpatched.block.PPBlocks;
import site.scalarstudios.pumpkinpatched.item.PPCreativeTabs;
import site.scalarstudios.pumpkinpatched.item.PPDataComponents;
import site.scalarstudios.pumpkinpatched.item.PPItems;

@Mod(PumpkinPatched.MODID)
public class PumpkinPatched {
    public static final String MODID = "pumpkinpatched";

    public PumpkinPatched(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        // Register Data Components, Items and Blocks
        PPDataComponents.register(modEventBus);
        PPItems.register(modEventBus);
        PPBlocks.register(modEventBus);

        // Register Creative Tabs
        PPCreativeTabs.register(modEventBus);
        modEventBus.addListener(PPCreativeTabs::registerTabs);

        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}
}

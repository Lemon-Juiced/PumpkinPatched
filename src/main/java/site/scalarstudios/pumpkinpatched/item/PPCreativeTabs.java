package site.scalarstudios.pumpkinpatched.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import site.scalarstudios.pumpkinpatched.PumpkinPatched;
import site.scalarstudios.pumpkinpatched.block.PPBlocks;

public class PPCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PumpkinPatched.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PP_BLOCKS_TAB = CREATIVE_MODE_TABS.register("pumpkinpatched_blocks", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.pumpkinpatched.blocks"))
            .icon(() -> new ItemStack(Blocks.PUMPKIN.asItem()))
            .build());

    public static void registerTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == PP_BLOCKS_TAB.get()) {
            PPBlocks.BIG_PUMPKIN.forEach(event::accept);
        }
    }

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

package site.scalarstudios.pumpkinpatched.item;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import site.scalarstudios.pumpkinpatched.PumpkinPatched;

public class PPDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, PumpkinPatched.MODID);

    // Which pumpkin a Spooky Stew was made with, set by the stew's crafting recipes
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SpookyStewPumpkin>> SPOOKY_STEW_PUMPKIN = DATA_COMPONENTS.registerComponentType("spooky_stew_pumpkin",
            builder -> builder.persistent(SpookyStewPumpkin.CODEC).networkSynchronized(SpookyStewPumpkin.STREAM_CODEC));

    public static void register(IEventBus eventBus) {
        DATA_COMPONENTS.register(eventBus);
    }
}

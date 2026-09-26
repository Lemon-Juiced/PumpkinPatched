package site.scalarstudios.pumpkinpatched.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import site.scalarstudios.pumpkinpatched.PumpkinPatched;

public class PPItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PumpkinPatched.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

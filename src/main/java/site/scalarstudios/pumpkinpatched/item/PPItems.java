package site.scalarstudios.pumpkinpatched.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import site.scalarstudios.pumpkinpatched.PumpkinPatched;
import site.scalarstudios.pumpkinpatched.block.PPBlocks;


public class PPItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PumpkinPatched.MODID);

    public static final DeferredItem<Item> BAKED_PUMPKIN_SEEDS = ITEMS.registerSimpleItem("baked_pumpkin_seeds", p -> p.food(PPFoods.BAKED_PUMPKIN_SEEDS));
    public static final DeferredItem<Item> PUMPKIN_BREAD = ITEMS.registerSimpleItem("pumpkin_bread", p -> p.food(PPFoods.PUMPKIN_BREAD));

    // Pumpkin Seeds (Places the corresponding pumpkin stem block)
    public static final DeferredItem<BlockItem> PALE_PUMPKIN_SEEDS = ITEMS.registerItem("pale_pumpkin_seeds", p -> new BlockItem(PPBlocks.PALE_PUMPKIN_STEM.get(), p.useItemDescriptionPrefix()));
    public static final DeferredItem<BlockItem> SCULK_PUMPKIN_SEEDS = ITEMS.registerItem("sculk_pumpkin_seeds", p -> new BlockItem(PPBlocks.SCULK_PUMPKIN_STEM.get(), p.useItemDescriptionPrefix()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

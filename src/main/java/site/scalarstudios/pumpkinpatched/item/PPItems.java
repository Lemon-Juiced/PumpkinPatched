package site.scalarstudios.pumpkinpatched.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import site.scalarstudios.pumpkinpatched.PumpkinPatched;
import site.scalarstudios.pumpkinpatched.block.PPBlocks;


public class PPItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PumpkinPatched.MODID);

    public static final DeferredItem<Item> BAKED_PUMPKIN_SEEDS = ITEMS.registerSimpleItem("baked_pumpkin_seeds", p -> p.food(PPFoods.BAKED_PUMPKIN_SEEDS));
    public static final DeferredItem<Item> PUMPKIN_BREAD = ITEMS.registerSimpleItem("pumpkin_bread", p -> p.food(PPFoods.PUMPKIN_BREAD));

    // Spooky Stew (Effect depends on the spooky_stew_pumpkin component its recipe sets, defaults to a normal pumpkin)
    public static final DeferredItem<SpookyStewItem> SPOOKY_STEW = ITEMS.registerItem("spooky_stew", SpookyStewItem::new, p -> p
            .stacksTo(1)
            .food(PPFoods.SPOOKY_STEW)
            .component(PPDataComponents.SPOOKY_STEW_PUMPKIN.get(), SpookyStewPumpkin.PUMPKIN)
            .usingConvertsTo(Items.BOWL));

    // Pumpkin Seeds (Places the corresponding pumpkin stem block)
    public static final DeferredItem<BlockItem> ELDRITCH_PUMPKIN_SEEDS = ITEMS.registerItem("eldritch_pumpkin_seeds", p -> new BlockItem(PPBlocks.ELDRITCH_PUMPKIN_STEM.get(), p.useItemDescriptionPrefix()));
    public static final DeferredItem<BlockItem> EMBER_PUMPKIN_SEEDS = ITEMS.registerItem("ember_pumpkin_seeds", p -> new BlockItem(PPBlocks.EMBER_PUMPKIN_STEM.get(), p.useItemDescriptionPrefix()));
    public static final DeferredItem<BlockItem> PALE_PUMPKIN_SEEDS = ITEMS.registerItem("pale_pumpkin_seeds", p -> new BlockItem(PPBlocks.PALE_PUMPKIN_STEM.get(), p.useItemDescriptionPrefix()));
    public static final DeferredItem<BlockItem> SCULK_PUMPKIN_SEEDS = ITEMS.registerItem("sculk_pumpkin_seeds", p -> new BlockItem(PPBlocks.SCULK_PUMPKIN_STEM.get(), p.useItemDescriptionPrefix()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}

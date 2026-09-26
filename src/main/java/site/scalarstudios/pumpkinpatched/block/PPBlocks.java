package site.scalarstudios.pumpkinpatched.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import site.scalarstudios.pumpkinpatched.PumpkinPatched;
import site.scalarstudios.pumpkinpatched.item.PPItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class PPBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PumpkinPatched.MODID);

    // Big Pumpkin
    public static final ArrayList<String> BIG_PUMPKIN_LAYERS = new ArrayList<>(List.of("top", "middle", "bottom"));
    public static final List<DeferredBlock<BigPumpkinBlock>> BIG_PUMPKIN = registerBigPumpkin();

    /**
     * Registers every segment of the 3x3x3 Big Pumpkin as big_pumpkin_(layer)_(position).
     * Positions 1-9 are the top-down grid with north at the top:
     * 123
     * 456
     * 789
     * Calls registerBlock(name, blockFactory, UnaryOperator) for each segment.
     *
     * @return every segment, ordered top layer to bottom layer, then position 1-9
     */
    private static List<DeferredBlock<BigPumpkinBlock>> registerBigPumpkin() {
        List<DeferredBlock<BigPumpkinBlock>> segments = new ArrayList<>();
        for (int layer = 0; layer < BIG_PUMPKIN_LAYERS.size(); layer++) {
            for (int position = 1; position <= 9; position++) {
                segments.add(registerBlock("big_pumpkin_" + BIG_PUMPKIN_LAYERS.get(layer) + "_" + position, BigPumpkinBlock::new,
                        properties -> properties
                                .mapColor(MapColor.COLOR_ORANGE)
                                .instrument(NoteBlockInstrument.DIDGERIDOO)
                                .strength(1.0F)
                                .sound(SoundType.WOOD)));
            }
        }
        return List.copyOf(segments);
    }

    /**
     * Registers a plain Block with tweaked properties, e.g. properties -> properties.strength(1.5F).
     * Calls the base registerBlock(name, blockFactory, UnaryOperator) with Block::new.
     *
     * @param name the registry name of the block
     * @param properties a function that modifies the default block properties
     * @return the registered block
     */
    private static DeferredBlock<Block> registerBlock(String name, UnaryOperator<BlockBehaviour.Properties> properties) {
        return registerBlock(name, Block::new, properties);
    }

    /**
     * Registers a custom block class using a prebuilt Properties object, e.g. BlockBehaviour.Properties.of()...
     * Wraps the properties in a lambda and calls the base registerBlock(name, blockFactory, UnaryOperator).
     * Slab, stair and wall helpers should call this one. (If included)
     *
     * @param name the registry name of the block
     * @param blockFactory the constructor or factory that creates the block, e.g. SlabBlock::new
     * @param properties the prebuilt block properties
     * @return the registered block
     */
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> blockFactory, BlockBehaviour.Properties properties) {
        return registerBlock(name, blockFactory, ignored -> properties);
    }

    /**
     * Base overload that every other registerBlock ends up calling.
     * Registers a custom block class with tweaked properties via BLOCKS.registerBlock, then calls registerBlockItem.
     *
     * @param name the registry name of the block
     * @param blockFactory the constructor or factory that creates the block
     * @param properties a function that modifies the default block properties
     * @return the registered block
     */
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> blockFactory, UnaryOperator<BlockBehaviour.Properties> properties) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, blockFactory, properties);
        registerBlockItem(toReturn);
        return toReturn;
    }

    /**
     * Creates the matching BlockItem so the block shows up in the inventory.
     * Calls PPItems.ITEMS.registerSimpleBlockItem.
     *
     * @param block the block to create an item for
     */
    private static void registerBlockItem(DeferredBlock<? extends Block> block) {
        PPItems.ITEMS.registerSimpleBlockItem(block);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

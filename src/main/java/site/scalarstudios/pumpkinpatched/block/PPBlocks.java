package site.scalarstudios.pumpkinpatched.block;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.waypoints.Waypoint;
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

    // Pale Pumpkins
    public static final DeferredBlock<PalePumpkinBlock> PALE_PUMPKIN = registerBlock("pale_pumpkin", PalePumpkinBlock::new,
            properties -> properties
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .instrument(NoteBlockInstrument.DIDGERIDOO)
                    .strength(1.0F)
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY));
    public static final DeferredBlock<CarvedPumpkinBlock> CARVED_PALE_PUMPKIN = registerBlock("carved_pale_pumpkin", CarvedPumpkinBlock::new,
            properties -> properties
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .strength(1.0F)
                    .sound(SoundType.WOOD)
                    .isValidSpawn(Blocks::always)
                    .pushReaction(PushReaction.DESTROY),
            itemProperties -> Waypoint.addHideAttribute(itemProperties)
                    .component(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD).setSwappable(false).setCameraOverlay(Identifier.withDefaultNamespace("misc/pumpkinblur")).build()));
    public static final DeferredBlock<CarvedPumpkinBlock> PALE_JACK_O_LANTERN = registerBlock("pale_jack_o_lantern", CarvedPumpkinBlock::new,
            properties -> properties
                    .mapColor(MapColor.COLOR_LIGHT_GRAY)
                    .strength(1.0F)
                    .sound(SoundType.WOOD)
                    .lightLevel(state -> 15)
                    .isValidSpawn(Blocks::always)
                    .pushReaction(PushReaction.DESTROY));

    // Pale Pumpkin Stems (no block items, Pale Pumpkin Seeds in PPItems places the stem)
    public static final DeferredBlock<StemBlock> PALE_PUMPKIN_STEM = BLOCKS.registerBlock("pale_pumpkin_stem",
            properties -> new StemBlock(blockKey("pale_pumpkin"), blockKey("attached_pale_pumpkin_stem"), itemKey("pale_pumpkin_seeds"), BlockTags.SUPPORTS_PUMPKIN_STEM, BlockTags.SUPPORTS_PUMPKIN_STEM_FRUIT, properties),
            properties -> properties
                    .mapColor(MapColor.PLANT)
                    .noCollision()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.HARD_CROP)
                    .pushReaction(PushReaction.DESTROY));
    public static final DeferredBlock<AttachedStemBlock> ATTACHED_PALE_PUMPKIN_STEM = BLOCKS.registerBlock("attached_pale_pumpkin_stem",
            properties -> new AttachedStemBlock(blockKey("pale_pumpkin_stem"), blockKey("pale_pumpkin"), itemKey("pale_pumpkin_seeds"), BlockTags.SUPPORTS_PUMPKIN_STEM, properties),
            properties -> properties
                    .mapColor(MapColor.PLANT)
                    .noCollision()
                    .instabreak()
                    .sound(SoundType.WOOD)
                    .pushReaction(PushReaction.DESTROY));

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
        return registerBlock(name, blockFactory, properties, UnaryOperator.identity());
    }

    /**
     * Registers a custom block class whose block item needs extra item properties, e.g. a carved pumpkin that can be worn.
     * Registers via BLOCKS.registerBlock, then calls registerBlockItem with the item properties.
     *
     * @param name the registry name of the block
     * @param blockFactory the constructor or factory that creates the block
     * @param properties a function that modifies the default block properties
     * @param itemProperties a function that modifies the default item properties of the block item
     * @return the registered block
     */
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends T> blockFactory, UnaryOperator<BlockBehaviour.Properties> properties, UnaryOperator<Item.Properties> itemProperties) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, blockFactory, properties);
        registerBlockItem(toReturn, itemProperties);
        return toReturn;
    }

    /**
     * Creates the matching BlockItem so the block shows up in the inventory.
     * Calls PPItems.ITEMS.registerSimpleBlockItem.
     *
     * @param block the block to create an item for
     * @param itemProperties a function that modifies the default item properties
     */
    private static void registerBlockItem(DeferredBlock<? extends Block> block, UnaryOperator<Item.Properties> itemProperties) {
        PPItems.ITEMS.registerSimpleBlockItem(block, itemProperties);
    }

    /**
     * Creates a registry key for one of this mod's blocks, used where vanilla refers to blocks by key, e.g. StemBlock.
     *
     * @param name the registry name of the block
     * @return the block's registry key
     */
    private static ResourceKey<Block> blockKey(String name) {
        return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(PumpkinPatched.MODID, name));
    }

    /**
     * Creates a registry key for one of this mod's items, used where vanilla refers to items by key, e.g. StemBlock.
     *
     * @param name the registry name of the item
     * @return the item's registry key
     */
    private static ResourceKey<Item> itemKey(String name) {
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(PumpkinPatched.MODID, name));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}

package site.scalarstudios.pumpkinpatched.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import site.scalarstudios.pumpkinpatched.PumpkinPatched;

import java.util.function.Supplier;

/**
 * A copy of the vanilla PumpkinBlock that can carve into any carved pumpkin, used by the Pale and Sculk Pumpkins.
 * Vanilla hardcodes the carved pumpkin and its seed drops, so this copies its shear carving and takes them as arguments instead.
 */
public class CarvablePumpkinBlock extends Block {
    private final Supplier<? extends Block> carvedPumpkin;
    private final ResourceKey<LootTable> carveLootTable;

    /**
     * @param carvedPumpkin the block this turns into when carved, e.g. PPBlocks.CARVED_PALE_PUMPKIN
     * @param carveLootTable the loot table dropped when carved, create it with carveLootTable(name)
     * @param properties the block properties
     */
    public CarvablePumpkinBlock(Supplier<? extends Block> carvedPumpkin, ResourceKey<LootTable> carveLootTable, Properties properties) {
        super(properties);
        this.carvedPumpkin = carvedPumpkin;
        this.carveLootTable = carveLootTable;
    }

    /**
     * Creates the key for a carving loot table at data/pumpkinpatched/loot_table/carve/(name).json.
     *
     * @param name the name of the pumpkin, e.g. pale_pumpkin
     * @return the loot table key
     */
    public static ResourceKey<LootTable> carveLootTable(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(PumpkinPatched.MODID, "carve/" + name));
    }

    /**
     * Carves the pumpkin when used with shears, turning it into the carved pumpkin facing the player.
     * Drops the carve loot table (the pumpkin's seeds) out of the carved face.
     *
     * @param itemStack the item being used on the block
     * @param state the current block state
     * @param level the level the block is in
     * @param pos the position of the block
     * @param player the player using the item
     * @param hand the hand holding the item
     * @param hitResult where the block was clicked
     * @return the result of the interaction
     */
    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!itemStack.canPerformAction(ItemAbilities.SHEARS_CARVE)) {
            return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
        } else if (level instanceof ServerLevel serverLevel) {
            Direction clickedDirection = hitResult.getDirection();
            Direction direction = clickedDirection.getAxis() == Direction.Axis.Y ? player.getDirection().getOpposite() : clickedDirection;
            dropFromBlockInteractLootTable(serverLevel, carveLootTable, state, level.getBlockEntity(pos), itemStack, player, (ignored, seeds) -> {
                ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5 + direction.getStepX() * 0.65, pos.getY() + 0.1, pos.getZ() + 0.5 + direction.getStepZ() * 0.65, seeds);
                RandomSource random = level.getRandom();
                entity.setDeltaMovement(0.05 * direction.getStepX() + random.nextDouble() * 0.02, 0.05, 0.05 * direction.getStepZ() + random.nextDouble() * 0.02);
                level.addFreshEntity(entity);
            });
            level.playSound(null, pos, SoundEvents.PUMPKIN_CARVE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(pos, carvedPumpkin.get().defaultBlockState().setValue(CarvedPumpkinBlock.FACING, direction), 11);
            itemStack.hurtAndBreak(1, player, hand.asEquipmentSlot());
            level.gameEvent(player, GameEvent.SHEAR, pos);
            player.awardStat(Stats.ITEM_USED.get(Items.SHEARS));
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.SUCCESS;
        }
    }
}

package site.scalarstudios.pumpkinpatched.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

/**
 * Spooky Stew, every variant is the same item with a different spooky_stew_pumpkin component.
 * Only adds a tooltip line naming the pumpkin, the effects themselves are applied by SpookyStewPumpkin.
 */
public class SpookyStewItem extends Item {
    public SpookyStewItem(Properties properties) {
        super(properties);
    }

    /**
     * Adds the pumpkin the stew was made with to its tooltip, so the variants can be told apart.
     *
     * @param itemStack the stew
     * @param context the tooltip context
     * @param display which tooltip parts are shown
     * @param builder adds lines to the tooltip
     * @param tooltipFlag whether advanced tooltips or creative mode are on
     */
    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        SpookyStewPumpkin pumpkin = itemStack.get(PPDataComponents.SPOOKY_STEW_PUMPKIN.get());
        if (pumpkin != null) {
            builder.accept(Component.translatable("tooltip.pumpkinpatched.spooky_stew." + pumpkin.getSerializedName()).withStyle(ChatFormatting.GRAY));
        }
    }
}

package site.scalarstudios.pumpkinpatched.item;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.ConsumableListener;
import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

/**
 * The pumpkin a Spooky Stew was made with, stored on the stew as the spooky_stew_pumpkin data component.
 * Works like vanilla SuspiciousStewEffects: being a ConsumableListener, onConsume is called when the stew is eaten.
 * A normal pumpkin always gives Regeneration, every other pumpkin is a 50/50 between its good and bad effect.
 */
public enum SpookyStewPumpkin implements ConsumableListener, StringRepresentable {
    ELDRITCH("eldritch"),
    EMBER("ember"),
    PUMPKIN("pumpkin"),
    PALE("pale"),
    SCULK("sculk");

    public static final Codec<SpookyStewPumpkin> CODEC = StringRepresentable.fromEnum(SpookyStewPumpkin::values);
    public static final StreamCodec<FriendlyByteBuf, SpookyStewPumpkin> STREAM_CODEC = NeoForgeStreamCodecs.enumCodec(SpookyStewPumpkin.class);

    private final String name;

    SpookyStewPumpkin(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    /**
     * Applies the stew's effect when it is eaten. Only runs on the server, so the 50/50 roll happens once.
     *
     * @param level the level the stew was eaten in
     * @param user the entity that ate the stew
     * @param stack the stew being eaten
     * @param consumable the stew's consumable component
     */
    @Override
    public void onConsume(Level level, LivingEntity user, ItemStack stack, Consumable consumable) {
        if (level.isClientSide()) {
            return;
        }
        boolean good = user.getRandom().nextBoolean();
        switch (this) {
            case PUMPKIN -> user.addEffect(new MobEffectInstance(MobEffects.REGENERATION, seconds(5)));
            case ELDRITCH -> {
                if (good) {
                    new TeleportRandomlyConsumeEffect().apply(level, stack, user);
                } else {
                    user.addEffect(new MobEffectInstance(MobEffects.LEVITATION, seconds(10)));
                }
            }
            case EMBER -> {
                if (good) {
                    user.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, seconds(15)));
                } else {
                    user.igniteForSeconds(5);
                }
            }
            case PALE -> user.addEffect(good ? new MobEffectInstance(MobEffects.INVISIBILITY, seconds(10)) : new MobEffectInstance(MobEffects.SLOWNESS, seconds(10)));
            case SCULK -> user.addEffect(good ? new MobEffectInstance(MobEffects.NIGHT_VISION, seconds(15)) : new MobEffectInstance(MobEffects.BLINDNESS, seconds(15)));
        }
    }

    private static int seconds(int seconds) {
        return seconds * 20;
    }
}

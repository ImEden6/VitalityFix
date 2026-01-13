package dev.shadowsoffire.attributeslib.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * MixinSquared mixin that targets the existing LivingEntityMixin from
 * ZenithAttributes.
 * Intercepts the zenith_attributes$onHealEvent handler and applies
 * HEALING_RECEIVED properly.
 */
@Mixin(value = LivingEntity.class, priority = 1500)
public abstract class VitalityHealMixin {

    /**
     * Intercepts the result from the existing zenith_attributes$onHealEvent mixin
     * and applies the HEALING_RECEIVED attribute multiplier.
     */
    @TargetHandler(mixin = "dev.shadowsoffire.attributeslib.mixin.LivingEntityMixin", name = "zenith_attributes$onHealEvent")
    @Inject(method = "@MixinSquared:Handler", at = @At("RETURN"), cancellable = true)
    private void vitalityfix$applyHealingReceived(float value, CallbackInfoReturnable<Float> cir) {
        if (!((Object) this instanceof Player player))
            return;
        if (player.getAttributes().hasAttribute(ALObjects.Attributes.HEALING_RECEIVED)) {
            float factor = (float) player.getAttributeValue(ALObjects.Attributes.HEALING_RECEIVED);
            cir.setReturnValue(cir.getReturnValue() * factor);
        }
    }
}

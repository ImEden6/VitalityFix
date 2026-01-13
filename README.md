# VitalityFix

A fix for the Bursting Vitality potion effect in ZenithAttributes.

## The Bug

The **Bursting Vitality** effect (`VitalityEffect`) is supposed to increase healing received by 20% per level. It works by adding an attribute modifier to `HEALING_RECEIVED`.

However, the healing boost never actually applies due to a bug in `LivingHealEvent`:

```java
// In LivingHealEvent.java
Event<LivingHealEvent> EVENT = EventFactory.createArrayBacked(LivingHealEvent.class,
    (listeners) -> (entity, amount) -> {
        for (LivingHealEvent listener : listeners) {
            return listener.onLivingHeal(entity, amount);  // BUG: Returns on FIRST listener!
        }
        return amount;
    });
```

The event invoker has a `return` statement **inside** the for loop, causing it to return immediately after calling only the first listener. The `AttributeEvents.heal()` listener that applies `HEALING_RECEIVED` is never called if any other listener is registered first.

## The Fix

This mod uses MixinSquared to intercept the existing `zenith_attributes$onHealEvent` mixin handler and applies the `HEALING_RECEIVED` attribute multiplier directly:

```java
@TargetHandler(
    mixin = "dev.shadowsoffire.attributeslib.mixin.LivingEntityMixin",
    name = "zenith_attributes$onHealEvent"
)
@Inject(method = "@MixinSquared:Handler", at = @At("RETURN"), cancellable = true)
private void vitalityfix$applyHealingReceived(float value, CallbackInfoReturnable<Float> cir) {
    if (!((Object) this instanceof Player player)) return;
    if (player.getAttributes().hasAttribute(ALObjects.Attributes.HEALING_RECEIVED)) {
        float factor = (float) player.getAttributeValue(ALObjects.Attributes.HEALING_RECEIVED);
        cir.setReturnValue(cir.getReturnValue() * factor);
    }
}
```

This bypasses the broken event system entirely and applies the healing modifier correctly.

## Installation

1. Requires **ZenithAttributes** to be installed
2. Place `vitalityfix-1.0.0.jar` in your mods folder

## Technical Details

- **Minecraft Version**: 1.20.1
- **Mod Loader**: Fabric
- **Dependencies**: ZenithAttributes, MixinSquared

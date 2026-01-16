# VitalityFix

A fix for the Bursting Vitality potion effect in ZenithAttributes.

## The Bug

The **Bursting Vitality** effect (`VitalityEffect`) is supposed to increase healing received by 20% per level. It works by adding an attribute modifier to `HEALING_RECEIVED`.

However, the healing boost never actually applies due to a bug in `LivingHealEvent`:


## The Fix

This mod uses MixinSquared to intercept the existing `zenith_attributes$onHealEvent` mixin handler and applies the `HEALING_RECEIVED` attribute multiplier directly:

This bypasses the broken event system entirely and applies the healing modifier correctly.


## Requirements

- **Minecraft Version**: 1.20.1
- **Mod Loader**: Fabric
- **Dependencies**: ZenithAttributes, MixinSquared (but not really)

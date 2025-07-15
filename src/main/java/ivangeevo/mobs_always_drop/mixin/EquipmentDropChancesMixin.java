package ivangeevo.mobs_always_drop.mixin;

import ivangeevo.mobs_always_drop.MobsAlwaysDropMod;
import net.minecraft.entity.EquipmentDropChances;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EquipmentDropChances.class)
public abstract class EquipmentDropChancesMixin {

    @Shadow @Final @Mutable public static EquipmentDropChances DEFAULT;

    // Set the base equipment drop chance to 2.0f which apparently translates to 100% chance
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyDefaultDropChances(CallbackInfo ci) {
        if (!MobsAlwaysDropMod.getInstance().settings.isEquipmentDropsEnabled()) {
            return;
        }
        DEFAULT = new EquipmentDropChances(Util.mapEnum(EquipmentSlot.class, slot -> 2.0f));
    }
}

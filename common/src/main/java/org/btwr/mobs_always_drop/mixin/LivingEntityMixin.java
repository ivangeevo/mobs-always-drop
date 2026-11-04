package org.btwr.mobs_always_drop.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.btwr.mobs_always_drop.CommonClass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "isAlwaysExperienceDropper", at = @At("HEAD"), cancellable = true)
    private void setMobsAlwaysDropXP(CallbackInfoReturnable<Boolean> cir) {
        if (!CommonClass.settings.isXpDropsEnabled()) {
            return;
        }

        cir.setReturnValue(true);
    }
}
package ivangeevo.mobs_always_drop.mixin;

import ivangeevo.mobs_always_drop.MobsAlwaysDropMod;
import net.minecraft.entity.*;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin  extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world)
    {
        super(type, world);
    }

    @Inject(method = "shouldAlwaysDropExperience", at = @At("HEAD"), cancellable = true)
    private void setMobsAlwaysDropXP(CallbackInfoReturnable<Boolean> cir) {
        if (!MobsAlwaysDropMod.getInstance().settings.isXpDropsEnabled()) {
            return;
        }
        
        cir.setReturnValue(true);
    }

}
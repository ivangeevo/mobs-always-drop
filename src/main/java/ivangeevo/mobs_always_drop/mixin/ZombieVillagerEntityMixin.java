package ivangeevo.mobs_always_drop.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ZombieVillagerEntity.class)
public abstract class ZombieVillagerEntityMixin extends ZombieEntity
{

    public ZombieVillagerEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyArg(method = "finishConversion",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/conversion/EntityConversionContext;create(Lnet/minecraft/entity/mob/MobEntity;ZZ)Lnet/minecraft/entity/conversion/EntityConversionContext;"),
            index = 1)
    private boolean setKeepEquipment(boolean keepEquipment) {
        return true;
    }


}

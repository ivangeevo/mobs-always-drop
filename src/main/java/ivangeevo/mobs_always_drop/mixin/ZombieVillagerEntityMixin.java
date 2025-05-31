package ivangeevo.mobs_always_drop.mixin;

import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieVillagerEntity.class)
public abstract class ZombieVillagerEntityMixin extends ZombieEntity
{

    public ZombieVillagerEntityMixin(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ZombieVillagerEntity villagerEntity = (ZombieVillagerEntity) (Object)this;
        if (!villagerEntity.isAlive()) {
            for (EquipmentSlot equipmentSlot : this.dropEquipment(stack -> !EnchantmentHelper.hasAnyEnchantmentsWith(stack, EnchantmentEffectComponentTypes.PREVENT_ARMOR_CHANGE))) {
                StackReference stackReference = villagerEntity.getStackReference(equipmentSlot.getEntitySlotId() + 300);
                stackReference.set(this.getEquippedStack(equipmentSlot));
            }
        }

    }

    @ModifyArg(method = "finishConversion",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ZombieVillagerEntity;convertTo(Lnet/minecraft/entity/EntityType;Z)Lnet/minecraft/entity/mob/MobEntity;"),
            index = 1)
    private boolean setKeepEquipment(boolean keepEquipment) {
        return true;
    }


}

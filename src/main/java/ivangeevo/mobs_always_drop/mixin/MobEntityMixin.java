package ivangeevo.mobs_always_drop.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import ivangeevo.mobs_always_drop.MobsAlwaysDropMod;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity
{

    @Shadow private EquipmentDropChances equipmentDropChances;

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    // Removes the !bl boolean check because we set the 100% drop chance in the EquipmentDropChanceMixin
    // Also added a minimum durability drop condition, so there isn't so many "empty damage" items.
    @Inject(
            method = "dropEquipment",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;dropStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/ItemEntity;"
            ),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isDamageable()Z"),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;dropStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/ItemEntity;")
            )
    )
    private void alwaysDropAndCustomDurability(CallbackInfo ci, @Local ItemStack itemStack) {
        if (itemStack.isDamageable()) {
            int minDurabilityDrop = 10;
            itemStack.setDamage(itemStack.getMaxDamage() - minDurabilityDrop - this.random.nextInt(1 + this.random.nextInt(Math.max(itemStack.getMaxDamage() - 3, 1))));
        }
    }
}
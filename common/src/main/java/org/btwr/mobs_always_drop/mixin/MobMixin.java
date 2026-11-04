package org.btwr.mobs_always_drop.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.btwr.mobs_always_drop.CommonClass;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    @Shadow @Final protected float[] armorDropChances;
    @Shadow @Final protected float[] handDropChances;
    @Shadow protected abstract float getEquipmentDropChance(EquipmentSlot slot);

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Change armor and item drop chances to 1.0F (100% drop chance).
     **/
    @Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At("TAIL"))
    private void injectedConstructor(EntityType<? extends Mob> entityType, Level level, CallbackInfo ci) {
        if (!CommonClass.settings.isEquipmentDropsEnabled()) {
            return;
        }

        Arrays.fill(this.armorDropChances, 1.0F);
        Arrays.fill(this.handDropChances, 1.0F);
    }

    /**
     * Removes the (recentlyHit || flag) boolean checks so that it will always drop equipment regardless of cause of death.
     **/
    // Also added a minimum durability drop int condition, so there isn't so many "empty damage" items.
    @Inject(method = "dropCustomDeathLoot(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;Z)V", at = @At("HEAD"), cancellable = true)
    private void injectedDropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit, CallbackInfo ci) {
        super.dropCustomDeathLoot(level, source, recentlyHit);

        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            ItemStack itemStack = this.getItemBySlot(equipmentSlot);
            float f = this.getEquipmentDropChance(equipmentSlot);
            if (f != 0.0F) {
                boolean guaranteed = f > 1.0F;
                Entity attacker = source.getEntity();
                if (attacker instanceof LivingEntity livingEntity) {
                    if (this.level() instanceof ServerLevel serverLevel) {
                        f = EnchantmentHelper.processEquipmentDropChance(serverLevel, livingEntity, source, f);
                    }
                }

                if (!itemStack.isEmpty() && !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP) && this.random.nextFloat() < f) {
                    if (!guaranteed && itemStack.isDamageableItem()) {
                        int minDurabilityDrop = 10;
                        itemStack.setDamageValue(itemStack.getMaxDamage() - minDurabilityDrop - this.random.nextInt(1 + this.random.nextInt(Math.max(itemStack.getMaxDamage() - 3, 1))));
                    }

                    this.spawnAtLocation(itemStack);
                    this.setItemSlot(equipmentSlot, ItemStack.EMPTY);
                }
            }
        }

        ci.cancel();
    }
}
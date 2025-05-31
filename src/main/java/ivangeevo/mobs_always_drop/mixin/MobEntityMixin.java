package ivangeevo.mobs_always_drop.mixin;

import ivangeevo.mobs_always_drop.MobsAlwaysDropMod;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity
{
    @Shadow
    @Final
    protected float[] armorDropChances;
    @Shadow
    @Final
    protected float[] handDropChances;

    @Shadow
    protected abstract float getDropChance(EquipmentSlot slot);

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /**
     * Change armor and item drop chances to 1.0F (100% drop chance).
     **/
    @Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At("TAIL"))
    private void injectedConstructor(EntityType<? extends MobEntity> entityType, World world, CallbackInfo ci) {
        if (!MobsAlwaysDropMod.getInstance().settings.isEquipmentDropsEnabled()) {
            return;
        }

        Arrays.fill(this.armorDropChances, 1.0F);
        Arrays.fill(this.handDropChances, 1.0F);
    }

    /**
     * Removes the (causedByPlayer || bl) boolean checks so that it will always drop equipment regardless of cause of death.
     **/
    // Also added a minimum durability drop int condition, so there isn't so many "empty damage" items.
    @Inject(method = "dropEquipment(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;Z)V", at = @At("HEAD"), cancellable = true)
    private void injectedDropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        super.dropEquipment(world, source, causedByPlayer);
        EquipmentSlot[] var4 = EquipmentSlot.values();

        for (EquipmentSlot equipmentSlot : var4) {
            ItemStack itemStack = this.getEquippedStack(equipmentSlot);
            float f = this.getDropChance(equipmentSlot);
            if (f != 0.0F) {
                boolean bl = f > 1.0F;
                Entity var13 = source.getAttacker();
                if (var13 instanceof LivingEntity livingEntity) {
                    World var14 = this.getWorld();
                    if (var14 instanceof ServerWorld serverWorld) {
                        f = EnchantmentHelper.getEquipmentDropChance(serverWorld, livingEntity, source, f);
                    }
                }

                if (!itemStack.isEmpty() && !EnchantmentHelper.hasAnyEnchantmentsWith(itemStack, EnchantmentEffectComponentTypes.PREVENT_EQUIPMENT_DROP) && this.random.nextFloat() < f) {
                    if (!bl && itemStack.isDamageable()) {
                        int minDurabilityDrop = 10;
                        itemStack.setDamage(itemStack.getMaxDamage() - minDurabilityDrop - this.random.nextInt(1 + this.random.nextInt(Math.max(itemStack.getMaxDamage() - 3, 1))));
                    }

                    this.dropStack(itemStack);
                    this.equipStack(equipmentSlot, ItemStack.EMPTY);
                }
            }
        }

        ci.cancel();
    }
}
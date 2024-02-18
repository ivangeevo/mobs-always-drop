package ivangeevo.mobsalwaysdrop.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity {
    @Shadow private boolean canPickUpLoot;
    @Shadow @Final protected float[] armorDropChances;
    @Shadow @Final protected float[] handDropChances;

    @Shadow protected abstract float getDropChance(EquipmentSlot slot);

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "canPickUpLoot", at = @At("HEAD"), cancellable = true)
    private void setCanPickUpLoot(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.canPickUpLoot = false);
    }

    /** Change armor and item drop chances to 1.0F (100% drop chance). **/
    @Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At("TAIL"))
    private void injectedConstructor(EntityType entityType, World world, CallbackInfo ci) {
        Arrays.fill(this.armorDropChances, 1.0F);
        Arrays.fill(this.handDropChances, 1.0F);
    }

    /** Removes the allowDrops boolean check so that it will always drop equipment regardless of cause of death. **/
    // Also added a minimum durability drop int so there isn't so many "empty damage" items.
    @Inject(method = "dropEquipment", at = @At("HEAD"), cancellable = true)
    private void injectedDropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops, CallbackInfo ci) {
        super.dropEquipment(source, lootingMultiplier, allowDrops);
        for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
            boolean bl;
            ItemStack itemStack = this.getEquippedStack(equipmentSlot);
            float f = this.getDropChance(equipmentSlot);
            boolean bl2 = bl = f > 1.0f;
            if (itemStack.isEmpty() || EnchantmentHelper.hasVanishingCurse(itemStack) || !(Math.max(this.random.nextFloat() - (float)lootingMultiplier * 0.01f, 0.0f) < f)) continue;
            if (!bl && itemStack.isDamageable()) {
                int minDurabilityDrop = 10;
                itemStack.setDamage(itemStack.getMaxDamage() - minDurabilityDrop - this.random.nextInt(1 + this.random.nextInt(Math.max(itemStack.getMaxDamage() - 3, 1))));
            }
            this.dropStack(itemStack);
            this.equipStack(equipmentSlot, ItemStack.EMPTY);
        }
        ci.cancel();
    }


}
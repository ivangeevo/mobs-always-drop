package ivangeevo.mobsalwaysdrop.mixin;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin  extends Entity {


    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @Shadow public abstract int getXpToDrop();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "dropEquipment", at = @At("HEAD"))
    private void injectedDropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops, CallbackInfo ci) {
        // EquipmentSlot.CHEST
        ItemStack chestItemStack = this.getEquippedStack(EquipmentSlot.CHEST);
        if (!chestItemStack.isEmpty()) {
            handleDropItem(chestItemStack, lootingMultiplier, EquipmentSlot.CHEST);
        }

        // EquipmentSlot.MAINHAND
        ItemStack mainhandItemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
        if (!mainhandItemStack.isEmpty()) {
            handleDropItem(mainhandItemStack, lootingMultiplier, EquipmentSlot.MAINHAND);
        }

        // EquipmentSlot.OFFHAND
        ItemStack offhandItemStack = this.getEquippedStack(EquipmentSlot.OFFHAND);
        if (!offhandItemStack.isEmpty()) {
            handleDropItem(offhandItemStack, lootingMultiplier, EquipmentSlot.OFFHAND);
        }

        // EquipmentSlot.HEAD
        ItemStack headItemStack = this.getEquippedStack(EquipmentSlot.HEAD);
        if (!headItemStack.isEmpty()) {
            handleDropItem(headItemStack, lootingMultiplier, EquipmentSlot.HEAD);
        }

        // EquipmentSlot.LEGS
        ItemStack legsItemStack = this.getEquippedStack(EquipmentSlot.LEGS);
        if (!legsItemStack.isEmpty()) {
            handleDropItem(legsItemStack, lootingMultiplier, EquipmentSlot.LEGS);
        }

        // EquipmentSlot.FEET
        ItemStack feetItemStack = this.getEquippedStack(EquipmentSlot.FEET);
        if (!feetItemStack.isEmpty()) {
            handleDropItem(feetItemStack, lootingMultiplier, EquipmentSlot.FEET);
        }

        // You can repeat this for other equipment slots
    }

    private void handleDropItem(ItemStack itemStack, int lootingMultiplier, EquipmentSlot slot) {
        // Set the drop chance to 1.0f to ensure it always drops.
        float f = 1.0f;

        if (itemStack.isDamageable()) {
            int maxDamage = itemStack.getMaxDamage();
            int newDamage = maxDamage - this.random.nextInt(1 + this.random.nextInt(Math.max(maxDamage - 3, 1)));
            itemStack.setDamage(newDamage);
        }

        if (!itemStack.isEmpty() && Math.max(this.random.nextFloat() - (float) lootingMultiplier * 0.01f, 0.0f) < f) {
            this.dropStack(itemStack);
        }
    }
    @Inject(method = "dropXp", at = @At("HEAD"), cancellable = true)
    private void injectedDropXP(CallbackInfo ci){
        if (this.world instanceof ServerWorld) {
            ExperienceOrbEntity.spawn((ServerWorld) this.world, this.getPos(), this.getXpToDrop());
            ci.cancel();
        }
    }
}

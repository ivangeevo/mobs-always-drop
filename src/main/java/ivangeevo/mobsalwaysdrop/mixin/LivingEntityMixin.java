package ivangeevo.mobsalwaysdrop.mixin;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.HorseEntity;
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
        boolean dropEquipment = true;

        // Existing code for dropping equipment...

        // Drop equipment items here
        if (!((LivingEntity)(Object)this instanceof HorseEntity)) {
            dropItem(this.getEquippedStack(EquipmentSlot.CHEST).getItem());
        }

        dropItem(this.getEquippedStack(EquipmentSlot.MAINHAND).getItem());
        dropItem(this.getEquippedStack(EquipmentSlot.OFFHAND).getItem());

        dropItem(this.getEquippedStack(EquipmentSlot.HEAD).getItem());
        dropItem(this.getEquippedStack(EquipmentSlot.LEGS).getItem());
        dropItem(this.getEquippedStack(EquipmentSlot.FEET).getItem());

        // You can repeat this for other equipment slots
    }

    @Inject(method = "dropXp", at = @At("HEAD"), cancellable = true)
    private void injectedDropXP(CallbackInfo ci){
        if (this.world instanceof ServerWorld) {
            ExperienceOrbEntity.spawn((ServerWorld) this.world, this.getPos(), this.getXpToDrop());
            ci.cancel();
        }
    }
}

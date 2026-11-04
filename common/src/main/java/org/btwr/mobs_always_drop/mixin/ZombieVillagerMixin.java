package org.btwr.mobs_always_drop.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieVillager.class)
public abstract class ZombieVillagerMixin extends Zombie {
    public ZombieVillagerMixin(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ZombieVillager self = (ZombieVillager) (Object) this;
        if (!self.isAlive()) {
            for (EquipmentSlot equipmentSlot : this.dropPreservedEquipment(stack -> !EnchantmentHelper.has(stack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE))) {
                SlotAccess slotAccess = self.getSlot(equipmentSlot.getIndex() + 300);
                slotAccess.set(this.getItemBySlot(equipmentSlot));
            }
        }
    }

    @ModifyArg(method = "finishConversion",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/ZombieVillager;convertTo(Lnet/minecraft/world/entity/EntityType;Z)Lnet/minecraft/world/entity/Mob;"),
            index = 1)
    private boolean setKeepEquipment(boolean keepEquipment) {
        return true;
    }
}
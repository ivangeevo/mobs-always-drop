package ivangeevo.mobsalwaysdrop.mixin;

import ivangeevo.mobsalwaysdrop.util.SideModUtils;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends LivingEntity implements SideModUtils {

    @Shadow @Final protected float[] armorDropChances;

    @Shadow @Final protected float[] handDropChances;

    protected MobEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }


    /** Change armor and item drop chances to 0.0F, because we modify them directly in the LivingEntity class. **/
    @Inject(method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V", at = @At("TAIL"))
    private void injectedConstructor(EntityType entityType, World world, CallbackInfo ci) {
        Arrays.fill(this.armorDropChances, 0.0F);
        Arrays.fill(this.handDropChances, 0.0F);
    }


    /** Modifying the chance for mobs spawning with tools. **/
    @Inject(method = "initEquipment", at = @At("HEAD"), cancellable = true)
    private void injectedInitEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {

        float dropChance = 0.15f; float difficultyChance = 0.25f;
        float hardDifficultyChance = 0.1f; float addRandomChanceToDropChance = 0.095f;

    // Commented out logic for lowering spawn chances when BTWR Core is available (old)
        /**
        float dropChance; float difficultyChance;
        float hardDifficultyChance; float addRandomChanceToDropChance;



        if (isBTWRLoaded) {
            dropChance = 0.11f; difficultyChance = 0.1f;
            hardDifficultyChance = 0.05f; addRandomChanceToDropChance = 0.035f;
        } else {
            dropChance = 0.15f; difficultyChance = 0.25f;
            hardDifficultyChance = 0.1f; addRandomChanceToDropChance = 0.095f;
        }
         **/


        if (random.nextFloat() < dropChance * localDifficulty.getClampedLocalDifficulty()) {
            float f;
            int i = random.nextInt(2);
            float f2 = f = this.world.getDifficulty() == Difficulty.HARD ? hardDifficultyChance : difficultyChance;
            if (random.nextFloat() < addRandomChanceToDropChance) {
                ++i;
            }
            if (random.nextFloat() < addRandomChanceToDropChance) {
                ++i;
            }
            if (random.nextFloat() < addRandomChanceToDropChance) {
                ++i;
            }
            boolean bl = true;
            for (EquipmentSlot equipmentSlot : EquipmentSlot.values()) {
                Item item;
                if (equipmentSlot.getType() != EquipmentSlot.Type.ARMOR) continue;
                ItemStack itemStack = this.getEquippedStack(equipmentSlot);
                if (!bl && random.nextFloat() < f) break;
                bl = false;
                if (!itemStack.isEmpty() || (item = MobEntity.getEquipmentForSlot(equipmentSlot, i)) == null) continue;
                this.equipStack(equipmentSlot, new ItemStack(item));
            }
        }
        ci.cancel();
    }

    @Inject(method = "getEquipmentForSlot", at = @At("HEAD"), cancellable = true)
    private static void injectedGetEquip(EquipmentSlot equipmentSlot, int equipmentLevel, CallbackInfoReturnable<Item> cir) {

        if (isBTWRLoaded) {
            switch (equipmentSlot) {
                case HEAD:
                    if (equipmentLevel == 0) {
                        cir.setReturnValue(null);
                    } else if (equipmentLevel == 1) {
                        cir.setReturnValue(null);
                    } else if (equipmentLevel == 2) {
                        cir.setReturnValue(Items.CHAINMAIL_HELMET);
                    } else if (equipmentLevel == 3) {
                        cir.setReturnValue(Items.IRON_HELMET);
                    } else if (equipmentLevel == 4) {
                        cir.setReturnValue(null);
                    }
                case CHEST:
                    if (equipmentLevel == 0) {
                        cir.setReturnValue(null);
                    } else if (equipmentLevel == 1) {
                        cir.setReturnValue(null);
                    } else if (equipmentLevel == 2) {
                        cir.setReturnValue(Items.CHAINMAIL_CHESTPLATE);
                    } else if (equipmentLevel == 3) {
                        cir.setReturnValue(Items.IRON_CHESTPLATE);
                    } else if (equipmentLevel == 4) {
                        cir.setReturnValue(null);
                    }
                case LEGS:
                    if (equipmentLevel == 0) {
                        cir.setReturnValue(null);
                    } else if (equipmentLevel == 1) {
                        cir.setReturnValue(null);
                    } else if (equipmentLevel == 2) {
                        cir.setReturnValue(Items.CHAINMAIL_LEGGINGS);
                    } else if (equipmentLevel == 3) {
                        cir.setReturnValue(Items.IRON_LEGGINGS);
                    } else if (equipmentLevel == 4) {
                        cir.setReturnValue(null);
                    }
                case FEET:
                    if (equipmentLevel == 0) {
                        cir.setReturnValue(null);
                    } else if (equipmentLevel == 1) {
                        cir.setReturnValue(null);
                    } else if (equipmentLevel == 2) {
                        cir.setReturnValue(Items.CHAINMAIL_BOOTS);
                    } else if (equipmentLevel == 3) {
                        cir.setReturnValue(Items.IRON_BOOTS);
                    } else if (equipmentLevel == 4) {
                        cir.setReturnValue(null);
                    }
                default:
                    cir.setReturnValue(null);

            }
        } else {
            switch (equipmentSlot) {
                case HEAD:
                    if (equipmentLevel == 0) {
                        cir.setReturnValue(Items.LEATHER_HELMET);
                    } else if (equipmentLevel == 1) {
                        cir.setReturnValue(Items.GOLDEN_HELMET);
                    } else if (equipmentLevel == 2) {
                        cir.setReturnValue(Items.CHAINMAIL_HELMET);
                    } else if (equipmentLevel == 3) {
                        cir.setReturnValue(Items.IRON_HELMET);
                    } else if (equipmentLevel == 4) {
                        cir.setReturnValue(Items.DIAMOND_HELMET);
                    }
                case CHEST:
                    if (equipmentLevel == 0) {
                        cir.setReturnValue(Items.LEATHER_CHESTPLATE);
                    } else if (equipmentLevel == 1) {
                        cir.setReturnValue(Items.GOLDEN_CHESTPLATE);
                    } else if (equipmentLevel == 2) {
                        cir.setReturnValue(Items.CHAINMAIL_CHESTPLATE);
                    } else if (equipmentLevel == 3) {
                        cir.setReturnValue(Items.IRON_CHESTPLATE);
                    } else if (equipmentLevel == 4) {
                        cir.setReturnValue(Items.DIAMOND_CHESTPLATE);
                    }
                case LEGS:
                    if (equipmentLevel == 0) {
                        cir.setReturnValue(Items.LEATHER_LEGGINGS);
                    } else if (equipmentLevel == 1) {
                        cir.setReturnValue(Items.GOLDEN_LEGGINGS);
                    } else if (equipmentLevel == 2) {
                        cir.setReturnValue(Items.CHAINMAIL_LEGGINGS);
                    } else if (equipmentLevel == 3) {
                        cir.setReturnValue(Items.IRON_LEGGINGS);
                    } else if (equipmentLevel == 4) {
                        cir.setReturnValue(Items.DIAMOND_LEGGINGS);
                    }
                case FEET:
                    if (equipmentLevel == 0) {
                        cir.setReturnValue(Items.LEATHER_BOOTS);
                    } else if (equipmentLevel == 1) {
                        cir.setReturnValue(Items.GOLDEN_BOOTS);
                    } else if (equipmentLevel == 2) {
                        cir.setReturnValue(Items.CHAINMAIL_BOOTS);
                    } else if (equipmentLevel == 3) {
                        cir.setReturnValue(Items.IRON_BOOTS);
                    } else if (equipmentLevel == 4) {
                        cir.setReturnValue(Items.DIAMOND_BOOTS);
                    }
                default:
                    cir.setReturnValue(null);

            }
        }
    }

       


    @Inject(method = "enchantEquipment", at = @At("HEAD"), cancellable = true)
    private void cancelEnchantEquipment(Random random, float power, EquipmentSlot slot, CallbackInfo ci) {
        ItemStack itemStack = this.getEquippedStack(slot);
        float dropChance;

        if (isBTWRLoaded) {
            dropChance = 0.1f;
        } else {
            dropChance = 0.5f;
        }

        if (!itemStack.isEmpty() && random.nextFloat() < dropChance * power) {
            this.equipStack(slot, EnchantmentHelper.enchant(random, itemStack, (int)(5.0f + power * (float)random.nextInt(18)), false));
        }
        ci.cancel();
    }

    @Inject(method = "enchantMainHandItem", at = @At("HEAD"), cancellable = true)
    private void cancelEnchantMainHandItem(Random random, float power, CallbackInfo ci) {
        float dropChance;

        if (isBTWRLoaded) {
            dropChance = 0.25f;
        } else {
            dropChance = 0.15f;
        }
        
        
        if (!this.getMainHandStack().isEmpty() && random.nextFloat() < dropChance * power) {
            this.equipStack(EquipmentSlot.MAINHAND, EnchantmentHelper.enchant(random, this.getMainHandStack(), (int)(5.0f + power * (float)random.nextInt(18)), false));
        }
        ci.cancel();

    }

}
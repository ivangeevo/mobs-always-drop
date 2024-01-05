package ivangeevo.mobsalwaysdrop.mixin;

import ivangeevo.mobsalwaysdrop.util.SideModUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends HostileEntity implements SideModUtils {


    protected ZombieEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    /** Make the Zombie's item pick up chance to be 0% **/
    @ModifyConstant(method = "initialize", constant = @Constant(floatValue = 0.55F))
    private float modifyPickUpChance(float original) { return 0f; }



    /** Modify the chance of mobs spawning with tools **/
    @Inject(method = "initEquipment", at = @At("HEAD"), cancellable = true)
    private void injectedInitEquipment(Random random, LocalDifficulty localDifficulty, CallbackInfo ci) {
        super.initEquipment(random, localDifficulty);
        float f = random.nextFloat();

        // BTWR modified floats
        float difficultyChance;
        float hardDifficultyChance;


        if (isBTWRLoaded) {
            difficultyChance = 0.03f;
            hardDifficultyChance = 0.007f;
        } else {
            difficultyChance = 0.05f;
            hardDifficultyChance = 0.01f;
        }

        // Lower the chance of spawning with tool
        float f2 = this.world.getDifficulty() == Difficulty.HARD ? hardDifficultyChance : difficultyChance;
        if (f < f2) {
            int i = random.nextInt(3);
            if (i == 0) {

                // and specify the custom tools to equip
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            } else {
                this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
            }
        }
        ci.cancel();
    }


}
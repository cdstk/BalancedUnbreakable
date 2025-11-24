package balancedunbreakable.mixin.spartanweaponry;

import balancedunbreakable.EntityAIAttackRangedXBow;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.oblivioussp.spartanweaponry.entity.projectile.EntityBolt;
import com.oblivioussp.spartanweaponry.entity.projectile.EntityBoltSpectral;
import com.oblivioussp.spartanweaponry.entity.projectile.EntityBoltTipped;
import com.oblivioussp.spartanweaponry.init.ItemRegistrySW;
import com.oblivioussp.spartanweaponry.item.ItemBolt;
import com.oblivioussp.spartanweaponry.item.ItemCrossbow;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractSkeleton.class)
public abstract class AbstractEntitySkeleton_Mixin extends EntityMob {

    @Unique
    private final EntityAIAttackRangedXBow<AbstractSkeleton> balancedUnbreakable$aiXBowAttack = new EntityAIAttackRangedXBow<AbstractSkeleton>((AbstractSkeleton)(Object)this, 0.5D, 40, 30.0F);

    public AbstractEntitySkeleton_Mixin(World world) {
        super(world);
    }

    @WrapOperation(
            method = "setCombatTask",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/EntityAITasks;addTask(ILnet/minecraft/entity/ai/EntityAIBase;)V", ordinal = 1)
    )
    private void www(EntityAITasks instance, int priority, EntityAIBase task, Operation<Void> original){
        this.tasks.removeTask(balancedUnbreakable$aiXBowAttack);
        this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0D);
        if(this.getHeldItemMainhand().getItem() instanceof ItemCrossbow){
            int i = 40;

            if (this.world.getDifficulty() != EnumDifficulty.HARD)
            {
                i = 80;
            }

            this.balancedUnbreakable$aiXBowAttack.setAttackCooldown(i);
            this.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(32.0D);
            original.call(instance, priority, balancedUnbreakable$aiXBowAttack);
        }
        else{
            original.call(instance, priority, task);
        }
    }

    @ModifyExpressionValue(
            method = "attackEntityWithRangedAttack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/monster/AbstractSkeleton;getArrow(F)Lnet/minecraft/entity/projectile/EntityArrow;")
    )
    private EntityArrow aaa(EntityArrow original, @Local(argsOnly = true) float distanceFactor){
        if (this.getHeldItemMainhand().getItem() instanceof ItemCrossbow){
            ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);

            if (itemstack.getItem() == ItemRegistrySW.boltSpectral)
            {
                EntityBoltSpectral entityBoltSpectral = new EntityBoltSpectral(this.world, this);
                entityBoltSpectral.setEnchantmentEffectsFromEntity(this, distanceFactor);
                return entityBoltSpectral;
            }
            else if (itemstack.getItem() instanceof ItemBolt)
            {
                EntityBolt entityBolt = ((ItemBolt) itemstack.getItem()).createBolt(this.world, itemstack, this);
                entityBolt.setEnchantmentEffectsFromEntity(this, distanceFactor);
                return entityBolt;
            }
            else
            {
                EntityBoltTipped entityBoltTipped = new EntityBoltTipped(this.world, this);
                entityBoltTipped.setEnchantmentEffectsFromEntity(this, distanceFactor);

                if(itemstack.getItem() == ItemRegistrySW.boltTipped && entityBoltTipped instanceof EntityBoltTipped){
                    entityBoltTipped.setPotionEffect(itemstack);
                }

                return entityBoltTipped;
            }

        }
        return original;
    }

    @WrapOperation(
            method = "attackEntityWithRangedAttack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/EntityArrow;shoot(DDDFF)V")
    )
    private void ddd(EntityArrow instance, double x, double y, double z, float velocity, float inaccuracy, Operation<Void> original){
        if (this.getHeldItemMainhand().getItem() instanceof ItemCrossbow){
            original.call(instance, x, y, z, velocity * 2, inaccuracy / 2);
        }
        original.call(instance, x, y, z, velocity, inaccuracy);
    }
}

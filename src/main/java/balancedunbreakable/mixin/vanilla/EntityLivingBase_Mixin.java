package balancedunbreakable.mixin.vanilla;

import balancedunbreakable.util.StackUtil;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBase_Mixin extends Entity {

    public EntityLivingBase_Mixin(World world) {
        super(world);
    }

    /**
     * Allows Enchantment Whitelist to run when Aggressive Nullification is enabled
     */
    @WrapMethod(
            method = "applyPotionDamageCalculations"
    )
    private float balancedUnbreakable_vanillaEntityLivingBase_applyPotionDamageCalculationsWhitelistEnch(DamageSource damageSource, float damage, Operation<Float> original){
        if(!this.world.isRemote) StackUtil.shouldSkipCheck = true;
        float calcDamage = original.call(damageSource, damage);
        StackUtil.shouldSkipCheck = false;
        return calcDamage;
    }
}

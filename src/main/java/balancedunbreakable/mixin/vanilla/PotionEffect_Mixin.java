package balancedunbreakable.mixin.vanilla;

import balancedunbreakable.handlers.ForgeConfigProvider;
import balancedunbreakable.util.StackUtil;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PotionEffect.class)
public abstract class PotionEffect_Mixin {

    @Shadow @Final private Potion potion;

    @WrapMethod(
            method = "performEffect"
    )
    private void balancedUnbreakable_vanillaPotionEffect_performEffectWhitelistedPotion(EntityLivingBase entityIn, Operation<Void> original){
        if(!entityIn.world.isRemote) StackUtil.shouldSkipCheck = ForgeConfigProvider.isPotionInWhitelist(this.potion);
        original.call(entityIn);
        StackUtil.shouldSkipCheck = false;
    }
}

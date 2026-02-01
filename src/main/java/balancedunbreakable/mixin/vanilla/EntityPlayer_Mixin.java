package balancedunbreakable.mixin.vanilla;

import balancedunbreakable.util.StackUtil;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayer_Mixin {

    @ModifyReturnValue(
            method = "canPlayerEdit",
            at = @At("RETURN")
    )
    private boolean balancedUnbreakable_vanillaEntityLivingBase_canPlayerEditUnusable(boolean canPlayerEdit, @Local(argsOnly = true) ItemStack stack){
        return canPlayerEdit && StackUtil.isUsable(stack);
    }
}

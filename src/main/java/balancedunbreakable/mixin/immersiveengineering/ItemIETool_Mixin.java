package balancedunbreakable.mixin.immersiveengineering;

import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.items.ItemIETool;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemIETool.class)
public abstract class ItemIETool_Mixin {

    @Shadow(remap = false) public abstract int getMaxDamageIE(ItemStack stack);

    @WrapWithCondition(
            method = "damageIETool",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;addStat(Lnet/minecraft/stats/StatBase;)V")
    )
    private boolean balancedUnbreakable_immersiveEngineeringItemIETool_damageIEToolBreakStats(EntityPlayer instance, StatBase statBase, @Local(argsOnly = true) ItemStack stack){
        return false;
    }

    @WrapWithCondition(
            method = "damageIETool",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;shrink(I)V")
    )
    private boolean balancedUnbreakable_immersiveEngineeringItemIETool_damageIEToolKeepStack(ItemStack instance, int p_190918_1_, @Local(argsOnly = true) ItemStack stack){
        ItemNBTHelper.setInt(stack, Lib.NBT_DAMAGE, this.getMaxDamageIE(stack));
        return false;
    }

    @ModifyArg(
            method = "damageIETool",
            at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/util/ItemNBTHelper;setInt(Lnet/minecraft/item/ItemStack;Ljava/lang/String;I)V"),
            index = 2,
            remap = false
    )
    private int balancedUnbreakable_immersiveEngineeringItemIETool_damageIEToolKeepStack(int val, @Local(argsOnly = true) ItemStack stack){
        return Math.min(val, this.getMaxDamageIE(stack));
    }
}

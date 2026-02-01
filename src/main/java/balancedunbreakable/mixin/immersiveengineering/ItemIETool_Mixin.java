package balancedunbreakable.mixin.immersiveengineering;

import balancedunbreakable.util.StackUtil;
import blusunrize.immersiveengineering.api.Lib;
import blusunrize.immersiveengineering.common.items.ItemIETool;
import blusunrize.immersiveengineering.common.util.ItemNBTHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemIETool.class)
public abstract class ItemIETool_Mixin {

    @Shadow(remap = false) public abstract int getMaxDamageIE(ItemStack stack);

    @ModifyReturnValue(
            method = "hasContainerItem",
            at = @At("RETURN"),
            remap = false
    )
    private boolean balancedUnbreakable_immersiveEngineeringItemIETool_hasContainerItemBroken(boolean original, ItemStack stack){
        return original && StackUtil.isUsable(stack);
    }

    @ModifyExpressionValue(
            method = "onItemUse",
            at = @At(value = "FIELD", target = "Lblusunrize/immersiveengineering/common/Config$IEConfig$Tools;cutterDurabiliy:I", remap = false)
    )
    private int balancedUnbreakable_immersiveEngineeringItemIETool_onItemUseZeroDura(int cutterDurabiliy){
        return cutterDurabiliy + 1;
    }

    @Inject(
            method = "onItemUse",
            at = @At(value = "INVOKE", target = "Lblusunrize/immersiveengineering/common/util/ItemNBTHelper;setInt(Lnet/minecraft/item/ItemStack;Ljava/lang/String;I)V", remap = false)
    )
    private void balancedUnbreakable_immersiveEngineeringItemIETool_onItemUsePlayBreakSound(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ, CallbackInfoReturnable<EnumActionResult> cir, @Local(name = "stack") ItemStack stack, @Local(name = "nbtDamage") int nbtDamage){
        if(nbtDamage == this.getMaxDamageIE(stack)) player.renderBrokenItemStack(stack);
    }

    @WrapOperation(
            method = "onItemUse",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;setItemStackToSlot(Lnet/minecraft/inventory/EntityEquipmentSlot;Lnet/minecraft/item/ItemStack;)V")
    )
    private void balancedUnbreakable_immersiveEngineeringItemIETool_onItemUseKeepWirecutter(EntityPlayer instance, EntityEquipmentSlot slotIn, ItemStack stack, Operation<Void> original){
        ItemNBTHelper.setInt(stack, Lib.NBT_DAMAGE, this.getMaxDamageIE(stack));
    }

    @WrapOperation(
            method = "damageIETool",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;addStat(Lnet/minecraft/stats/StatBase;)V")
    )
    private void balancedUnbreakable_immersiveEngineeringItemIETool_damageIEToolBreakStats(EntityPlayer instance, StatBase statBase, Operation<Void> original, @Local(argsOnly = true) ItemStack stack){
        // no op
    }

    @WrapOperation(
            method = "damageIETool",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;shrink(I)V")
    )
    private void balancedUnbreakable_immersiveEngineeringItemIETool_damageIEToolKeepStack(ItemStack instance, int quantity, Operation<Void> original, @Local(argsOnly = true) ItemStack stack){
        ItemNBTHelper.setInt(stack, Lib.NBT_DAMAGE, this.getMaxDamageIE(stack));
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

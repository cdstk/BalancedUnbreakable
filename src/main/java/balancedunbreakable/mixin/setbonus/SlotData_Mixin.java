package balancedunbreakable.mixin.setbonus;

import balancedunbreakable.util.StackUtil;
import com.fantasticsource.setbonus.common.bonusrequirements.setrequirement.SlotData;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SlotData.class)
public abstract class SlotData_Mixin {

    @ModifyReturnValue(
            method = "getStackInSlot",
            at = @At("RETURN"),
            remap = false
    )
    private static ItemStack balancedUnbreakable_setBonusSlotData_getStackInSlotUnusable(ItemStack original){
        return StackUtil.isUsable(original) ? original : ItemStack.EMPTY;
    }
}

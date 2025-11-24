package balancedunbreakable.mixin.vanilla;

import balancedunbreakable.util.StackUtil;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class Item_Mixin {

    @Inject(
            method = "onUsingTick",
            at = @At("TAIL"),
            remap = false
    )
    private void balancedUnbreakable_vanillaItem_onUsingTickUnusable(ItemStack stack, EntityLivingBase player, int count, CallbackInfo ci){
        if(!StackUtil.isUsable(stack)) player.resetActiveHand();
    }

    @ModifyReturnValue(
            method = "onLeftClickEntity",
            at = @At("RETURN"),
            remap = false
    )
    private boolean balancedUnbreakable_vanillaItem_onLeftClickEntityUnusable(boolean original, ItemStack stack){
        return original || !StackUtil.isUsable(stack);
    }
}

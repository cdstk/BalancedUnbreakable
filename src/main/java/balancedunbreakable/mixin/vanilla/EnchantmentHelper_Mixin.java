package balancedunbreakable.mixin.vanilla;

import balancedunbreakable.handlers.ForgeConfigProvider;
import balancedunbreakable.util.StackUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentHelper_Mixin {

    /**
     * When stack is provided without using getItemStackFromSlot()
     */
    @ModifyExpressionValue(
            method = "getEnchantmentLevel",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z")
    )
    private static boolean balancedUnbreakable_vanillaEnchantmentHelper_getEnchantmentLevelUnusable(boolean stackIsEmpty, Enchantment enchID, ItemStack stack){
        if(stackIsEmpty) return true;

        if(ForgeConfigProvider.getEquipmentEnchantmentBlacklist().contains(enchID)) return false;

        return !StackUtil.isUsable(stack);
    }

    /**
     * When stack is provided without using getItemStackFromSlot()
     */
    @ModifyExpressionValue(
            method = "applyEnchantmentModifier",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isEmpty()Z")
    )
    private static boolean balancedUnbreakable_vanillaEnchantmentHelper_applyEnchantmentModifierUnusable(boolean stackIsEmpty, @Local(argsOnly = true) ItemStack stack){
        return stackIsEmpty || !StackUtil.isUsable(stack);
    }
}

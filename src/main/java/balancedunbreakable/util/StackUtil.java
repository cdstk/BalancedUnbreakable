package balancedunbreakable.util;

import balancedunbreakable.compat.ImmersiveEngineeringUtil;
import balancedunbreakable.compat.ModLoadedUtil;
import balancedunbreakable.handlers.ForgeConfigProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class StackUtil {

    /**
     * If EntityPlayer.getItemStackFromSlot() should skip checking StackUtil.isUsable()
     */
    public static boolean shouldSkipCheck = false;

    public static boolean shouldCheckItem(Item item){
        if(!item.isDamageable()) return false;

        if(ForgeConfigProvider.isItemInstInBlacklist(item)) return false;
        if(ForgeConfigProvider.isItemClassInBlacklist(item)) return false;

        return !shouldSkipCheck;
    }

    public static boolean isUsable(ItemStack stack){
        if(!shouldCheckItem(stack.getItem())) return true;

        if(ModLoadedUtil.isImmersiveEngineeringLoaded() && ImmersiveEngineeringUtil.isDamageableTool(stack))
            return ImmersiveEngineeringUtil.isToolUsable(stack);

        return stack.getItemDamage() < stack.getMaxDamage();
    }
}

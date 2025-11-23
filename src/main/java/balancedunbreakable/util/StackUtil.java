package balancedunbreakable.util;

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

        if(ForgeConfigProvider.getItemInstBlacklist().contains(item)) return false;
        for(Class<?> clazz : ForgeConfigProvider.getItemClassBlacklist()){
            if(clazz.isInstance(item)) return false;
        }

        return !shouldSkipCheck;
    }

    public static boolean isUsable(ItemStack stack){
        if(!shouldCheckItem(stack.getItem())) return true;
        return stack.getItemDamage() < stack.getMaxDamage();
    }
}

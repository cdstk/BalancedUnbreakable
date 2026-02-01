package balancedunbreakable.compat;

import blusunrize.immersiveengineering.common.items.IEItemInterfaces;
import net.minecraft.item.ItemStack;

public abstract class ImmersiveEngineeringUtil {

    public static boolean isDamageableTool(ItemStack itemStack){
        return itemStack.getItem() instanceof IEItemInterfaces.IItemDamageableIE;
    }

    public static boolean isToolUsable(ItemStack itemStack){
        if(!isToolDamaged(itemStack)) return true;
        return getToolDurability(itemStack) < getMaxToolDurability(itemStack);
    }

    public static boolean isToolDamaged(ItemStack itemStack){
        return getToolDurability(itemStack) > 0;
    }

    public static int getMaxToolDurability(ItemStack itemStack){
        return ((IEItemInterfaces.IItemDamageableIE) itemStack.getItem()).getMaxDamageIE(itemStack);
    }

    public static int getToolDurability(ItemStack itemStack){
        return ((IEItemInterfaces.IItemDamageableIE) itemStack.getItem()).getItemDamageIE(itemStack);
    }
}

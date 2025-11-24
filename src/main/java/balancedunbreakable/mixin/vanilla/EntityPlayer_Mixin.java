package balancedunbreakable.mixin.vanilla;

import balancedunbreakable.handlers.ForgeConfigHandler;
import balancedunbreakable.util.StackUtil;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayer_Mixin {

    @Unique
    private final NonNullList<ItemStack> balancedUnbreakable$armorInventoryCopy = NonNullList.create();

    @ModifyReturnValue(
            method = "getItemStackFromSlot",
            at = @At(value = "RETURN", ordinal = 0)
    )
    private ItemStack balancedUnbreakable_vanillaEntityLivingBase_getItemStackFromSlotMainUnusable(ItemStack original){
        if(!original.isEmpty() && ForgeConfigHandler.server.airHeldItem && !StackUtil.isUsable(original)) return ItemStack.EMPTY;
        return original;
    }

    @ModifyReturnValue(
            method = "getItemStackFromSlot",
            at = @At(value = "RETURN", ordinal = 1)
    )
    private ItemStack balancedUnbreakable_vanillaEntityLivingBase_getItemStackFromSlotOffUnusable(ItemStack original){
        if(!original.isEmpty() && ForgeConfigHandler.server.airHeldItem && !StackUtil.isUsable(original)) return ItemStack.EMPTY;
        return original;
    }

    @ModifyReturnValue(
            method = "getItemStackFromSlot",
            at = @At(value = "RETURN", ordinal = 2)
    )
    private ItemStack balancedUnbreakable_vanillaEntityLivingBase_getItemStackFromSlotArmorUnusable(ItemStack original){
        if(!original.isEmpty() && ForgeConfigHandler.server.airWornItem && !StackUtil.isUsable(original)) return ItemStack.EMPTY;
        return original;
    }

    @ModifyReturnValue(
            method = "getArmorInventoryList",
            at = @At(value = "RETURN")
    )
    private Iterable<ItemStack> balancedUnbreakable_vanillaEntityLivingBase_getArmorInventoryListUnusable(Iterable<ItemStack> original){
        if(ForgeConfigHandler.server.airWornItem) {
            balancedUnbreakable$armorInventoryCopy.clear();
            original.forEach(itemStack -> {
                if (StackUtil.isUsable(itemStack)) balancedUnbreakable$armorInventoryCopy.add(itemStack);
                else balancedUnbreakable$armorInventoryCopy.add(ItemStack.EMPTY);
            });
            return balancedUnbreakable$armorInventoryCopy;
        }
        return original;
    }
}

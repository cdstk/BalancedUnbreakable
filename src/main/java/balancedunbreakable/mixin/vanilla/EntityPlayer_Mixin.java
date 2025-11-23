package balancedunbreakable.mixin.vanilla;

import balancedunbreakable.handlers.ForgeConfigHandler;
import balancedunbreakable.util.StackUtil;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayer_Mixin {

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
}

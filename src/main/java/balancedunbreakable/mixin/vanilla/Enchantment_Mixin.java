package balancedunbreakable.mixin.vanilla;

import balancedunbreakable.handlers.ForgeConfigProvider;
import balancedunbreakable.util.StackUtil;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

import java.util.List;

@Mixin(Enchantment.class)
public abstract class Enchantment_Mixin {

    /**
     *  Allow Whitelisted Enchantments
     */
    @WrapMethod(
            method = "getEntityEquipment"
    )
    private List<ItemStack> balancedUnbreakable_vanillaEnchantmentHelper_getEntityEquipmentWhitelistedEnch(EntityLivingBase entityIn, Operation<List<ItemStack>> original){
        if(!entityIn.world.isRemote) StackUtil.shouldSkipCheck = ForgeConfigProvider.isEnchantmentInWhitelist((Enchantment)(Object)this);
        List<ItemStack> equipment = original.call(entityIn);
        StackUtil.shouldSkipCheck = false;
        return equipment;
    }
}

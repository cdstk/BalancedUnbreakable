package balancedunbreakable.mixin.vanilla;

import balancedunbreakable.handlers.ForgeConfigProvider;
import balancedunbreakable.util.StackUtil;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(Enchantment.class)
public abstract class Enchantment_Mixin {

    @Shadow @Nullable public EnumEnchantmentType type;

    /**
     *  Check Vanilla Mending and Modded Mending enchantments
     */
    @WrapMethod(
            method = "getEntityEquipment"
    )
    private List<ItemStack> balancedUnbreakable_vanillaEnchantmentHelper_getEntityEquipmentUnusable(EntityLivingBase entityIn, Operation<List<ItemStack>> original){
        StackUtil.shouldSkipCheck = ForgeConfigProvider.getEquipmentEnchantmentBlacklist().contains((Enchantment)(Object)this);
        List<ItemStack> equipment = original.call(entityIn);
        StackUtil.shouldSkipCheck = false;
        return equipment;
    }
}

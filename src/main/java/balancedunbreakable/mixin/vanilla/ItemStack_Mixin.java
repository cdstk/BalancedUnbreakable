package balancedunbreakable.mixin.vanilla;

import balancedunbreakable.util.StackUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStack_Mixin {

    @Shadow public abstract Item getItem();
    @Shadow public abstract void setItemDamage(int meta);
    @Shadow public abstract int getItemDamage();
    @Shadow public abstract int getMaxDamage();

    @WrapMethod(
            method = "getDestroySpeed"
    )
    private float balancedUnbreakable_vanillaItemStack_getDestroySpeedUnusable(IBlockState blockState, Operation<Float> original){
        return StackUtil.isUsable((ItemStack)(Object)this) ? original.call(blockState) : 1.0F;
    }

    @WrapMethod(
            method = "useItemRightClick"
    )
    private ActionResult<ItemStack> balancedUnbreakable_vanillaItemStack_useItemRightClickUnusable(World world, EntityPlayer player, EnumHand hand, Operation<ActionResult<ItemStack>> original){
        return StackUtil.isUsable((ItemStack)(Object)this) ? original.call(world, player, hand) : new ActionResult<>(EnumActionResult.FAIL, player.getHeldItem(hand));
    }

    @ModifyExpressionValue(
            method = "damageItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;attemptDamageItem(ILjava/util/Random;Lnet/minecraft/entity/player/EntityPlayerMP;)Z")
    )
    private boolean balancedUnbreakable_vanillaItemStack_damageItemCapped(boolean destroyItem, @Local(argsOnly = true) EntityLivingBase entityIn){
        if(destroyItem && StackUtil.shouldCheckItem(this.getItem())) {
            this.setItemDamage(Math.min(this.getItemDamage(), this.getMaxDamage()));
            entityIn.renderBrokenItemStack((ItemStack)(Object)this);
            return false;
        }
        return destroyItem;
    }

    @WrapMethod(
            method = "hitEntity"
    )
    private void balancedUnbreakable_vanillaItemStack_hitEntityUnusable(EntityLivingBase livingBase, EntityPlayer player, Operation<Void> original){
        if(StackUtil.isUsable((ItemStack)(Object)this)) original.call(livingBase, player);
    }

    @WrapMethod(
            method = "interactWithEntity"
    )
    private boolean balancedUnbreakable_vanillaItemStack_interactWithEntityUnusable(EntityPlayer player, EntityLivingBase livingBase, EnumHand hand, Operation<Boolean> original){
        return StackUtil.isUsable((ItemStack)(Object)this) ? original.call(player, livingBase, hand) : false;
    }

    @WrapMethod(
            method = "getAttributeModifiers"
    )
    private Multimap<String, AttributeModifier> balancedUnbreakable_vanillaItemStack_getAttributeModifiersUnusable(EntityEquipmentSlot equipmentSlot, Operation<Multimap<String, AttributeModifier>> original){
        return StackUtil.isUsable((ItemStack)(Object)this) ? original.call(equipmentSlot) : HashMultimap.create();
    }
}

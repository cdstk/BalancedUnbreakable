package balancedunbreakable.mixin.vanilla.client;

import balancedunbreakable.handlers.ForgeConfigHandler;
import balancedunbreakable.handlers.ForgeConfigProvider;
import balancedunbreakable.util.StackUtil;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RenderItem.class)
public abstract class RenderItem_Mixin {

    @WrapOperation(
            method = "renderQuads",
            at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/model/pipeline/LightUtil;renderQuadColor(Lnet/minecraft/client/renderer/BufferBuilder;Lnet/minecraft/client/renderer/block/model/BakedQuad;I)V", remap = false)
    )
    private void balancedUnbreakable_vanillaRenderItem_renderQuadsBrokenOverlay(BufferBuilder buffer, BakedQuad quad, int auxColor, Operation<Void> original, @Local(argsOnly = true) BufferBuilder renderer, @Local(argsOnly = true) ItemStack stack, @Local BakedQuad bakedquad, @Local(name = "k") int k){
        original.call(buffer, quad, auxColor);
        if(!ForgeConfigHandler.client.brokenOverlayRender) return;

        if(!StackUtil.isUsable(stack) && stack.getItem().getPropertyGetter(new ResourceLocation("broken")) == null){
            net.minecraftforge.client.model.pipeline.LightUtil.renderQuadColor(renderer,
                    new BakedQuadRetextured(
                            bakedquad,
                            Minecraft.getMinecraft().renderGlobal.destroyBlockIcons[ForgeConfigProvider.getBrokenOverlayIndex(stack.getItem())]),
                    k);
        }
    }
}

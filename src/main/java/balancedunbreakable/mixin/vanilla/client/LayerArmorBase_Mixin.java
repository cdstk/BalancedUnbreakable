package balancedunbreakable.mixin.vanilla.client;

import balancedunbreakable.handlers.ForgeConfigHandler;
import balancedunbreakable.handlers.ForgeConfigProvider;
import balancedunbreakable.util.StackUtil;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LayerArmorBase.class)
public abstract class LayerArmorBase_Mixin {

    @Shadow @Final private RenderLivingBase<?> renderer;

    @Inject(
            method = "renderArmorLayer",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/layers/LayerArmorBase;skipRenderGlint:Z")
    )
    private void balancedUnbreakable_vanillaLayerArmorBase_renderArmorLayerUnusable(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot slotIn, CallbackInfo ci, @Local ItemStack itemStack, @Local(name = "t") ModelBase model){
        if(!StackUtil.isUsable(itemStack) && itemStack.getItem().getPropertyGetter(new ResourceLocation("broken")) == null)
            balancedUnbreakable$renderBrokenOverlay(this.renderer, itemStack, slotIn, entityLivingBaseIn, model, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
    }

    @Unique
    private static void balancedUnbreakable$renderBrokenOverlay(RenderLivingBase<?> renderer, ItemStack stack, EntityEquipmentSlot slotIn, EntityLivingBase entityLivingBaseIn, ModelBase model, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale){
        if(!ForgeConfigHandler.client.brokenOverlayRender) return;

        float textureScale = scale * ForgeConfigProvider.getBrokenArmorScale(slotIn);
        float[] textureRotation = ForgeConfigProvider.getBrokenArmorRotation(slotIn);
        renderer.bindTexture(
                Minecraft.getMinecraft().getTextureMapBlocks().getResourceLocation(
                        Minecraft.getMinecraft().renderGlobal.destroyBlockIcons[ForgeConfigProvider.getBrokenOverlayIndex(stack.getItem())]
                )
        );
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        GlStateManager.enableBlend();
        GlStateManager.depthFunc(514);
        GlStateManager.depthMask(false);
        GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);

        GlStateManager.disableLighting();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.scale(textureScale, textureScale, textureScale);
        GlStateManager.rotate(textureRotation[0], textureRotation[1], textureRotation[2], textureRotation[3]);
        GlStateManager.matrixMode(5888);
        model.render(entityLivingBaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

        GlStateManager.matrixMode(5890);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        GlStateManager.enableLighting();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
        GlStateManager.disableBlend();
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
    }
}

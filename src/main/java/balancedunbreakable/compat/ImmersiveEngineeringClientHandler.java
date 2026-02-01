package balancedunbreakable.compat;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class ImmersiveEngineeringClientHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onItemTooltip(ItemTooltipEvent event){
        if(!event.getFlags().isAdvanced()) return;
        ItemStack itemStack = event.getItemStack();

        if(ImmersiveEngineeringUtil.isDamageableTool(itemStack) && ImmersiveEngineeringUtil.isToolDamaged(itemStack)){
            int tooltipIndex = event.getToolTip().indexOf(TextFormatting.DARK_GRAY + Item.REGISTRY.getNameForObject(itemStack.getItem()).toString());
            if(tooltipIndex != -1) event.getToolTip().add(tooltipIndex,
                    I18n.format(
                            "item.durability",
                            ImmersiveEngineeringUtil.getMaxToolDurability(itemStack) - ImmersiveEngineeringUtil.getToolDurability(itemStack),
                            ImmersiveEngineeringUtil.getMaxToolDurability(itemStack)
                    )
            );
        }
    }
}

package balancedunbreakable;

import balancedunbreakable.compat.ImmersiveEngineeringClientHandler;
import balancedunbreakable.compat.ModLoadedUtil;
import balancedunbreakable.handlers.ForgeConfigHandler;
import balancedunbreakable.handlers.ForgeConfigProvider;
import balancedunbreakable.util.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BalancedUnbreakable.MODID, version = BalancedUnbreakable.VERSION, name = BalancedUnbreakable.NAME, dependencies = "required-after:fermiumbooter")
public class BalancedUnbreakable {
    public static final String MODID = "balancedunbreakable";
    public static final String VERSION = "1.0.1";
    public static final String NAME = "BalancedUnbreakable";
    public static final Logger LOGGER = LogManager.getLogger();

	@Instance(MODID)
	public static BalancedUnbreakable instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(BalancedUnbreakable.class);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ForgeConfigProvider.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        if(event.getSide().isClient()) {
            if (ModLoadedUtil.isImmersiveEngineeringLoaded() && ForgeConfigHandler.client.immersiveEngineeringTooltip) {
                MinecraftForge.EVENT_BUS.register(ImmersiveEngineeringClientHandler.class);
            }
        }
    }

    @SubscribeEvent
    public static void cancelBrokenItemAttack(AttackEntityEvent event){
        if(event.isCanceled()) return;
        EntityPlayer player = event.getEntityPlayer();
        if(player.world.isRemote) return;

        if(!player.getHeldItemMainhand().isEmpty() && !StackUtil.isUsable(player.getHeldItemMainhand())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void resetBrokeItemUseTick(LivingEntityUseItemEvent event){
        if(event.getDuration() > 0 && !StackUtil.isUsable(event.getItem())) event.getEntityLiving().resetActiveHand();
    }
}
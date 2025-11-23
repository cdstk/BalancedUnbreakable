package balancedunbreakable.handlers;

import balancedunbreakable.BalancedUnbreakable;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = BalancedUnbreakable.MODID)
public class ForgeConfigHandler {

	@Config.Comment("Server-Side Options")
	@Config.Name("Server Options")
	public static final ServerConfig server = new ServerConfig();

//	@Config.Comment("Mixin Toggle Options")
//	@Config.Name("Mixin Options")
//	public static final MixinToggleConfig mixins = new MixinToggleConfig();

	public static class ServerConfig {

		@Config.Comment("Apply a very aggressive nullification to Held Items when broken.\n" +
				"Broken items will be assumed to be air.\n" +
				"Will break specific hand checks from things such as commands.")
		@Config.Name("Aggressive Held Item Nullification")
		public boolean airHeldItem = false;

		@Config.Comment("Apply a very aggressive nullification to Armor when broken.\n" +
				"Broken items will be assumed to be air.\n" +
				"Required for general coverage of disabling Armor Bonuses from other mods.")
		@Config.Name("Aggressive Worn Item Nullification")
		public boolean airWornItem = true;

		@Config.Comment("Item classes blacklisted from handling")
		@Config.Name("Item Class Blacklist")
		public String[] itemClassBlacklist = new String[] {

		};

		@Config.Comment("Item ids in the format \"domain:itemname\" blacklisted from handling")
		@Config.Name("Item ID Blacklist")
		public String[] itemIDBlacklist = new String[] {

		};

		@Config.Comment("Enchantment ids in the format \"domain:itemname\"\n" +
				"Whitelisted enchantments will be allowed to activate when an Item is broken.")
		@Config.Name("Enchantments Whitelist")
		public String[] enchantmentWhitelist = {
				"minecraft:mending",
				"somanyenchantments:advancedmending",
				"somanyenchantments:rune_revival"
		};
	}

//	@MixinConfig(name = BalancedUnbreakable.MODID) //Needed on config classes that contain MixinToggles for those mixins to be added
//	public static class MixinToggleConfig {
//
//		@Config.Comment("Allows durability damage dealt by the Disenchanter to break items")
//		@Config.Name("Disenchanter Compatibility (Disenchanter)")
//		@Config.RequiresMcRestart
//		@MixinConfig.MixinToggle(lateMixin = "mixins.balancedunbreakable.disenchanter.json", defaultValue = true)
//		@MixinConfig.CompatHandling(
//				modid = "disenchanter",
//				desired = true,
//				reason = "Mod needed for this Mixin to properly work",
//				warnIngame = false //use this if the mixin is for an optional mod dependency that can be skipped with no issue if the mod is not present
//		)
//		public boolean enableDisenchanterMixin = true;
//	}

	@Mod.EventBusSubscriber(modid = BalancedUnbreakable.MODID)
	private static class EventHandler{

		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if(event.getModID().equals(BalancedUnbreakable.MODID)) {
				ConfigManager.sync(BalancedUnbreakable.MODID, Config.Type.INSTANCE);
				ForgeConfigProvider.reset();
			}
		}
	}
}
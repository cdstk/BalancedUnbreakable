package balancedunbreakable;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BalancedUnbreakable.MODID, version = BalancedUnbreakable.VERSION, name = BalancedUnbreakable.NAME, dependencies = "required-after:fermiumbooter")
public class BalancedUnbreakable {
    public static final String MODID = "balancedunbreakable";
    public static final String VERSION = "1.0.0";
    public static final String NAME = "BalancedUnbreakable";
    public static final Logger LOGGER = LogManager.getLogger();

	@Instance(MODID)
	public static BalancedUnbreakable instance;

}
package balancedunbreakable.compat;

import net.minecraftforge.fml.common.Loader;

public class ModLoadedUtil {

    public static final String IMMERSIVE_ENGINEERING_MODID = "immersiveengineering";
    public static final String SET_BONUS_MODID = "setbonus";

    private static Boolean immersiveEngineeringLoaded = null;

    public static boolean isImmersiveEngineeringLoaded() {
        if(immersiveEngineeringLoaded == null) immersiveEngineeringLoaded = Loader.isModLoaded(IMMERSIVE_ENGINEERING_MODID);
        return immersiveEngineeringLoaded;
    }
}

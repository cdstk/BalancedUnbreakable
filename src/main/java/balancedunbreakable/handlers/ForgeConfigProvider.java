package balancedunbreakable.handlers;

import balancedunbreakable.BalancedUnbreakable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ForgeConfigProvider {

    private static final Set<Class<?>> itemClassBlacklist = new HashSet<>();
    private static final Set<Item> itemInstBlacklist = new HashSet<>();
    private static final Set<Enchantment> enchantmentsWhitelist = new HashSet<>();

    public static void init(){
        getItemClassBlacklist();
        getItemInstBlacklist();
    }

    public static void reset() {
        ForgeConfigProvider.itemClassBlacklist.clear();
        ForgeConfigProvider.itemInstBlacklist.clear();
        init();
    }

    public static Set<Class<?>> getItemClassBlacklist(){
        if(ForgeConfigProvider.itemClassBlacklist.isEmpty())
            ForgeConfigProvider.itemClassBlacklist.addAll(Arrays
                    .stream(ForgeConfigHandler.server.itemClassBlacklist)
                    .map(line -> {
                        try{
                            return Class.forName(line.trim());
                        }
                        catch (ClassNotFoundException e) {
                            BalancedUnbreakable.LOGGER.log(Level.WARN, "Item Class not found for entry: {}, ignoring", line);
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()));
        return ForgeConfigProvider.itemClassBlacklist;
    }

    public static Set<Item> getItemInstBlacklist(){
        if(ForgeConfigProvider.itemInstBlacklist.isEmpty())
            ForgeConfigProvider.itemInstBlacklist.addAll(Arrays
                    .stream(ForgeConfigHandler.server.itemIDBlacklist)
                    .map(line -> {
                        Item item = Item.REGISTRY.getObject(new ResourceLocation(line.trim()));
                        if(item == null) BalancedUnbreakable.LOGGER.log(Level.WARN, "Item ID not found for entry: {}, ignoring", line);
                        return item;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()));
        return ForgeConfigProvider.itemInstBlacklist;
    }

    public static Set<Enchantment> getEquipmentEnchantmentWhitelist() {
        if(ForgeConfigProvider.enchantmentsWhitelist.isEmpty()
                && ForgeConfigHandler.server.enchantmentWhitelist.length > 0)
            ForgeConfigProvider.enchantmentsWhitelist.addAll(Arrays
                    .stream(ForgeConfigHandler.server.enchantmentWhitelist)
                    .map(ResourceLocation::new)
                    .map(ForgeRegistries.ENCHANTMENTS::getValue)
                    .collect(Collectors.toSet())
            );
        return ForgeConfigProvider.enchantmentsWhitelist;
    }
}

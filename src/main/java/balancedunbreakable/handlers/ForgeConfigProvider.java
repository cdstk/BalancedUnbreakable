package balancedunbreakable.handlers;

import balancedunbreakable.BalancedUnbreakable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
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
    private static final Set<Potion> potionWhitelist = new HashSet<>();

    public static void init(){
        ForgeConfigProvider.initItemClassBlacklist();
        ForgeConfigProvider.initItemInstBlacklist();
        ForgeConfigProvider.initEquipmentEnchantmentWhitelist();
        ForgeConfigProvider.initPotionWhitelist();
    }

    public static boolean isItemClassInBlacklist(Item item){
        for(Class<?> clazz : ForgeConfigProvider.itemClassBlacklist){
            if(clazz.isInstance(item)) return true;
        }
        return false;
    }

    public static boolean isItemInstInBlacklist(Item item){
        return ForgeConfigProvider.itemInstBlacklist.contains(item);
    }

    public static boolean isEnchantmentInWhitelist(Enchantment enchantment){
        return ForgeConfigProvider.enchantmentsWhitelist.contains(enchantment);
    }

    public static boolean isPotionInWhitelist(Potion potion){
        return ForgeConfigProvider.potionWhitelist.contains(potion);
    }

    public static void initItemClassBlacklist(){
        ForgeConfigProvider.itemClassBlacklist.clear();
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
            .collect(Collectors.toSet())
        );
    }

    public static void initItemInstBlacklist(){
        ForgeConfigProvider.itemInstBlacklist.clear();
        ForgeConfigProvider.itemInstBlacklist.addAll(Arrays
            .stream(ForgeConfigHandler.server.itemIDBlacklist)
            .map(line -> {
                Item item = Item.REGISTRY.getObject(new ResourceLocation(line.trim()));
                if(item == null) BalancedUnbreakable.LOGGER.log(Level.WARN, "Item ID not found for entry: {}, ignoring", line);
                return item;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toSet())
        );
    }

    public static void initEquipmentEnchantmentWhitelist() {
        ForgeConfigProvider.enchantmentsWhitelist.clear();
        ForgeConfigProvider.enchantmentsWhitelist.addAll(Arrays
            .stream(ForgeConfigHandler.server.enchantmentWhitelist)
            .map(line -> {
                Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(line));
                if(enchantment == null) BalancedUnbreakable.LOGGER.log(Level.WARN, "Enchantment ID not found for entry: {}, ignoring", line);
                return enchantment;
            })
            .collect(Collectors.toSet())
        );
    }

    public static void initPotionWhitelist() {
        ForgeConfigProvider.potionWhitelist.clear();
        ForgeConfigProvider.potionWhitelist.addAll(Arrays
            .stream(ForgeConfigHandler.server.potionWhitelist)
            .map(line -> {
                Potion potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(line));
                if(potion == null) BalancedUnbreakable.LOGGER.log(Level.WARN, "Potion ID not found for entry: {}, ignoring", line);
                return potion;
            })
            .collect(Collectors.toSet())
        );
    }
}

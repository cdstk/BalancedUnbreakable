package balancedunbreakable.handlers;

import balancedunbreakable.BalancedUnbreakable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.logging.log4j.Level;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ForgeConfigProvider {

    private static final Set<Class<?>> itemClassBlacklist = new HashSet<>();
    private static final Set<Item> itemInstBlacklist = new HashSet<>();
    private static final Map<Class<?>, Integer> itemClassOverlayIndexes = new HashMap<>();
    private static final Map<Item, Integer> itemInstOverlayIndex = new HashMap<>();
    private static final Map<EntityEquipmentSlot, Integer> brokenArmorScale = new HashMap<>();
    private static final Map<EntityEquipmentSlot, float[]> brokenArmorRotation = new HashMap<>();
    private static final Set<Enchantment> enchantmentsWhitelist = new HashSet<>();
    private static final Set<Potion> potionWhitelist = new HashSet<>();

    public static void init(){
        ForgeConfigProvider.initItemClassBlacklist();
        ForgeConfigProvider.initItemInstBlacklist();
        ForgeConfigProvider.initItemClassOverlayIndexes();
        ForgeConfigProvider.initItemInstOverlayIndexes();
        ForgeConfigProvider.initBrokenArmorRenderProps();
        ForgeConfigProvider.initEquipmentEnchantmentWhitelist();
        ForgeConfigProvider.initPotionWhitelist();
    }

    public static int getBrokenOverlayIndex(Item item){
        int value = ForgeConfigHandler.client.overlayIndex;
        if(itemInstOverlayIndex.containsKey(item)) value = itemInstOverlayIndex.get(item);
        for(Class<?> clazz : ForgeConfigProvider.itemClassOverlayIndexes.keySet()){
            if(clazz.isInstance(item)) {
                value = ForgeConfigProvider.itemClassOverlayIndexes.get(clazz);
                break;
            }
        }
        return (value >= 0 && value <= 9) ? value : ForgeConfigHandler.client.overlayIndex;
    }

    public static float getBrokenArmorScale(EntityEquipmentSlot equipmentSlot){
        return brokenArmorScale.getOrDefault(equipmentSlot, 0);
    }

    public static float[] getBrokenArmorRotation(EntityEquipmentSlot equipmentSlot){
        return brokenArmorRotation.getOrDefault(equipmentSlot, new float[]{0, 0, 0, 1});
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

    public static void initItemClassOverlayIndexes(){
        ForgeConfigProvider.itemClassOverlayIndexes.clear();
        ForgeConfigProvider.itemClassOverlayIndexes.putAll(Arrays
                .stream(ForgeConfigHandler.client.itemClassOverlayIndex)
                .map(s -> s.split(","))
                .collect(Collectors.toMap(
                        split -> {
                            try{
                                return Class.forName(split[0].trim());
                            }
                            catch (ClassNotFoundException e) {
                                BalancedUnbreakable.LOGGER.log(Level.WARN, "Item Class not found for overlay index entry: {}, ignoring", split[0]);
                            }
                            return null;
                        },
                        split -> {
                            try {
                                return Integer.valueOf(split[1].trim());
                            } catch (Exception e){
                                return 9;
                            }
                        }
                )));
    }

    public static void initItemInstOverlayIndexes(){
        ForgeConfigProvider.itemInstOverlayIndex.clear();
        ForgeConfigProvider.itemInstOverlayIndex.putAll(Arrays
                .stream(ForgeConfigHandler.client.itemIDOverlayIndexes)
                .map(s -> s.split(","))
                .collect(Collectors.toMap(
                        split -> {
                            Item item = Item.REGISTRY.getObject(new ResourceLocation(split[0].trim()));
                            if(item == null) BalancedUnbreakable.LOGGER.log(Level.WARN, "Item ID not found for overlay index entry: {}, ignoring", split[0]);
                            return item;
                        },
                        split -> {
                            try {
                                return Integer.valueOf(split[1].trim());
                            } catch (Exception e){
                                return 9;
                            }
                        }
                )));
    }

    public static void initBrokenArmorRenderProps() {
        ForgeConfigProvider.brokenArmorScale.clear();
        ForgeConfigProvider.brokenArmorRotation.clear();
        Arrays.stream(ForgeConfigHandler.client.brokenArmorProperties)
                .forEach(line -> {
                    String[] fields = line.split(",");
                    EntityEquipmentSlot slot = null;
                    try {
                        slot = EntityEquipmentSlot.fromString(fields[0].trim().toLowerCase());
                    } catch (IllegalArgumentException e) {
                        BalancedUnbreakable.LOGGER.log(Level.WARN, "Equipment Slot not found for entry: {}, ignoring", fields[0]);
                    }
                    if(slot == null) return;
                    try {
                        if(fields.length >= 2){
                            ForgeConfigProvider.brokenArmorScale.put(slot, Integer.valueOf(fields[1].trim()));
                            if(fields.length >= 6){
                                ForgeConfigProvider.brokenArmorRotation.put(slot, new float[] {
                                        Float.parseFloat(fields[2]),
                                        Float.parseFloat(fields[3]),
                                        Float.parseFloat(fields[4]),
                                        Float.parseFloat(fields[5])
                                });
                            }
                        }
                    } catch (Exception e) {
                        return;
                    }
                });
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

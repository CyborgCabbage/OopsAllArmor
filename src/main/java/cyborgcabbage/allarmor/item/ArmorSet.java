package cyborgcabbage.allarmor.item;

import cyborgcabbage.allarmor.AllArmor;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class ArmorSet {
    public final MyArmorItem helmet;
    public final MyArmorItem chestplate;
    public final MyArmorItem leggings;
    public final MyArmorItem boots;
    private final ArmorMaterial material;
    public ArmorSet(ArmorMaterial armorMaterial){
        material = armorMaterial;
        helmet = new MyArmorItem(material, EquipmentSlot.HEAD, new FabricItemSettings().group(ItemGroup.COMBAT));
        chestplate = new MyArmorItem(material, EquipmentSlot.CHEST, new FabricItemSettings().group(ItemGroup.COMBAT));
        leggings = new MyArmorItem(material, EquipmentSlot.LEGS, new FabricItemSettings().group(ItemGroup.COMBAT));
        boots = new MyArmorItem(material, EquipmentSlot.FEET, new FabricItemSettings().group(ItemGroup.COMBAT));
    }
    public void register(String name){
        Registry.register(Registry.ITEM, new Identifier("allarmor", name+"_helmet"), helmet);
        Registry.register(Registry.ITEM, new Identifier("allarmor", name+"_chestplate"), chestplate);
        Registry.register(Registry.ITEM, new Identifier("allarmor", name+"_leggings"), leggings);
        Registry.register(Registry.ITEM, new Identifier("allarmor", name+"_boots"), boots);
    }
    public void register(){
        this.register(material.getName());
    }
    public static ArrayList<ItemStack> getArmorItems(LivingEntity livingEntity){
        Iterable<ItemStack> armor = livingEntity.getArmorItems();
        ArrayList<ItemStack> armorItems= new ArrayList<>();
        armor.forEach(armorItems::add);
        return armorItems;
    }
    public ArrayList<ItemStack> getThisArmorItems(LivingEntity livingEntity){
        Iterable<ItemStack> armor = livingEntity.getArmorItems();
        ArrayList<ItemStack> armorItems = new ArrayList<>();
        armor.forEach((i) -> {if(isFromSet(i)) armorItems.add(i);});
        return armorItems;
    }
    public float wearingFraction(LivingEntity entity){
        float percentage = 0.0f;
        Iterator<ItemStack> armorItems = entity.getArmorItems().iterator();
        if(isFromSet(armorItems.next())) percentage += 0.15f; //Boots
        if(isFromSet(armorItems.next())) percentage += 0.30f; //Leggings
        if(isFromSet(armorItems.next())) percentage += 0.35f; //Chestplate
        if(isFromSet(armorItems.next())) percentage += 0.20f; //Helmet
        return percentage;
    }
    public boolean wearingAny(LivingEntity entity){
        return !getThisArmorItems(entity).isEmpty();
    }
    public ArrayList<Boolean> wearingArray(LivingEntity entity){
        ArrayList<Boolean> array = new ArrayList<>();
        getArmorItems(entity).forEach((i) -> array.add(isFromSet(i)));
        return array;
    }
    public boolean wearingAll(LivingEntity entity){
        return getThisArmorItems(entity).size() == 4;
    }
    public ArmorMaterial getMaterial(){
        return material;
    }
    public boolean isFromSet(ItemStack itemstack){
        Item item = itemstack.getItem();
        if(item instanceof ArmorItem armorItem){
            return armorItem.getMaterial() == this.getMaterial();
        }
        return false;
    }
    public static EquipmentSlot getSlot(ItemStack itemstack){
        Item item = itemstack.getItem();
        if(item instanceof ArmorItem armorItem){
            return armorItem.getSlotType();
        }
        return EquipmentSlot.FEET;
    }
    public boolean wearingBoots(LivingEntity entity){
        return wearingArray(entity).get(0);
    }
    public boolean wearingLeggings(LivingEntity entity){
        return wearingArray(entity).get(1);
    }
    public boolean wearingChestplate(LivingEntity entity){
        return wearingArray(entity).get(2);
    }
    public boolean wearingHelmet(LivingEntity entity){
        return wearingArray(entity).get(3);
    }
    public boolean damage(LivingEntity livingEntity, int amount){
        ArrayList<ItemStack> items = getArmorItems(livingEntity);
        if(!wearingAny(livingEntity)){
            return false;
        }
        while(amount > 0) {
            int r = livingEntity.getRandom().nextInt(4);
            if(isFromSet(items.get(r))) {
                items.get(r).damage(1, livingEntity, ((player) -> {
                    player.sendEquipmentBreakStatus(getSlot(items.get(r)));
                }));
                amount--;
            }
        }
        return true;
    }
    public void damageEach(LivingEntity livingEntity, int amount) {
        for (ItemStack itemStack : getThisArmorItems(livingEntity)) {
            itemStack.damage(amount, livingEntity, ((player) -> {
                player.sendEquipmentBreakStatus(getSlot(itemStack));
            }));
        }
    }
}
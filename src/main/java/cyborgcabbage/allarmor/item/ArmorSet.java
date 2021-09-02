package cyborgcabbage.allarmor.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Iterator;

public class ArmorSet {
    public MyArmorItem helmet;
    public MyArmorItem chestplate;
    public MyArmorItem leggings;
    public MyArmorItem boots;
    public ArmorSet(ArmorMaterial material){
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
        this.register(helmet.getMaterial().getName());
    }
    public float wearingFraction(LivingEntity entity){
        float percentage = 0.0f;
        Iterator<ItemStack> armorItems = entity.getArmorItems().iterator();
        percentage += armorItems.next().getItem() == boots ? 0.15f : 0.0f;
        percentage += armorItems.next().getItem() == leggings ? 0.30f : 0.0f;
        percentage += armorItems.next().getItem() == chestplate ? 0.35f : 0.0f;
        percentage += armorItems.next().getItem() == helmet ? 0.20f : 0.0f;
        return percentage;
    }
    public boolean wearingAny(LivingEntity entity){
        Iterator<ItemStack> armorItems = entity.getArmorItems().iterator();
        boolean any = armorItems.next().getItem() == boots;
        any = any || armorItems.next().getItem() == leggings;
        any = any || armorItems.next().getItem() == chestplate;
        any = any || armorItems.next().getItem() == helmet;
        return any;
    }
    public boolean[] wearingArray(LivingEntity entity){
        boolean[] array = new boolean[]{false,false,false,false};
        Iterator<ItemStack> armorItems = entity.getArmorItems().iterator();
        array[0] = armorItems.next().getItem() == boots;
        array[1] = armorItems.next().getItem() == leggings;
        array[2] = armorItems.next().getItem() == chestplate;
        array[3] = armorItems.next().getItem() == helmet;
        return array;
    }
    public boolean wearingAll(LivingEntity entity){
        Iterator<ItemStack> armorItems = entity.getArmorItems().iterator();
        boolean any = armorItems.next().getItem() == boots;
        any = any && armorItems.next().getItem() == leggings;
        any = any && armorItems.next().getItem() == chestplate;
        any = any && armorItems.next().getItem() == helmet;
        return any;
    }
    public ArmorMaterial getMaterial(){
        return boots.getMaterial();
    }
    public boolean fromSet(ItemStack itemstack){
        Item item = ItemStack.EMPTY.getItem();
        if(item instanceof ArmorItem armorItem){
            return armorItem.getMaterial() == this.getMaterial();
        }
        return false;
    }
}

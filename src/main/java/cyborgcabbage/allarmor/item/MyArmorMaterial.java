package cyborgcabbage.allarmor.item;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

public class MyArmorMaterial implements ArmorMaterial {
    private int[] BASE_DURABILITY = new int[] {13, 15, 16, 11};
    private String name;
    private int durabilityMultiplier;
    private int[] protectionAmounts;
    private int enchantability;
    private SoundEvent equipSound;
    private float toughness;
    private float knockbackResistance;
    private Ingredient repairIngredient;

    public MyArmorMaterial(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Item... repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = Ingredient.ofItems(repairIngredient);
    }

    @Override
    public int getDurability(EquipmentSlot slot) {
        return BASE_DURABILITY[slot.getEntitySlotId()] * durabilityMultiplier;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return protectionAmounts[slot.getEntitySlotId()];
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }
}

/*public enum MyArmorMaterial implements ArmorMaterial {
    PAPER("paper", 3, new int[]{1, 1, 1, 1}, 15, SoundEvents.ITEM_BOOK_PAGE_TURN, 0.0F, 0.0F, () -> {
        return Ingredient.ofItems(Items.PAPER);
    }),

    //ENDER_EYE("ender_eye", 10, new int[]{1, 3, 5, 2}, 15, SoundEvents.ITEM_BOOK_PAGE_TURN, 0.0F, 0.0F, () -> {
    //    return Ingredient.ofItems(Items.ENDER_EYE);
    //}),

    BOW("bow", 7, new int[]{1, 2, 3, 1}, 15, SoundEvents.BLOCK_TRIPWIRE_ATTACH, 0.0F, 0.0F, () -> {
        return Ingredient.ofItems(Items.BOW);
    })/*,

    TNT("tnt", 3, new int[]{1, 1, 1, 1}, 15, SoundEvents.ITEM_BOOK_PAGE_TURN, 0.0F, 0.0F, () -> {
        return Ingredient.ofItems(Blocks.TNT);
    });

    private static final int[] BASE_DURABILITY = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] protectionAmounts;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Lazy<Ingredient> repairIngredientSupplier;

    private MyArmorMaterial(String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredientSupplier) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredientSupplier = new Lazy(repairIngredientSupplier);
    }

    public int getDurability(EquipmentSlot slot) {
        return BASE_DURABILITY[slot.getEntitySlotId()] * this.durabilityMultiplier;
    }

    public int getProtectionAmount(EquipmentSlot slot) {
        return this.protectionAmounts[slot.getEntitySlotId()];
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    public Ingredient getRepairIngredient() {
        return (Ingredient)this.repairIngredientSupplier.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}*/

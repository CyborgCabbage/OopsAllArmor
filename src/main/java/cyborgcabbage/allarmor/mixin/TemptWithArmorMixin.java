package cyborgcabbage.allarmor.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TemptGoal.class)
public abstract class TemptWithArmorMixin {

    @Shadow @Final private Ingredient food;

    @Inject(at = @At("HEAD"), method = "isTemptedBy", cancellable = true)
    private void injected(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        Iterable<ItemStack> armor = entity.getArmorItems();
        for (ItemStack itemStack : armor) {
            Item item = itemStack.getItem();
            if (item instanceof ArmorItem){
                Ingredient ingredient = ((ArmorItem) item).getMaterial().getRepairIngredient();
                for(ItemStack k: ingredient.getMatchingStacks()){
                    if(this.food.test(k)){
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }
}

package cyborgcabbage.allarmor.mixin;

import cyborgcabbage.allarmor.AllArmor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin{

    @Shadow public abstract Item getItem();

    @Shadow public abstract void removeSubNbt(String key);

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;setDamage(I)V"), method = "damage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z")
    private void injected(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if(this.getItem() instanceof ArmorItem){
            if (((ArmorItem)this.getItem()).getMaterial() == AllArmor.ENCHANTING_TABLE.boots.getMaterial()){
                this.removeSubNbt("Enchantments");
                EnchantmentHelper.enchant(random,(ItemStack)(Object)this,30,true);
            }
        }
    }
}

package cyborgcabbage.allarmor.mixin;

import cyborgcabbage.allarmor.AllArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnderPearlEntity.class)
public class EnderPearlEntityMixin {
    @Redirect(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean injected(Entity entity, DamageSource source, float amount) {
        if (entity instanceof LivingEntity livingEntity) {
            return entity.damage(source, amount * (1.0f-AllArmor.ENDER_PEARL.wearingFraction(livingEntity)));
        }else{
            return entity.damage(source, amount);
        }
    }
}

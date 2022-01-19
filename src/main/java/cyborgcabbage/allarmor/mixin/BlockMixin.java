package cyborgcabbage.allarmor.mixin;

import cyborgcabbage.allarmor.AllArmor;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(at = @At(value="HEAD"),method="onEntityLand",cancellable = true)
    private void inject(BlockView world, Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity) {
            float fraction = AllArmor.SLIME_BALL.wearingFraction(livingEntity);
            if (!entity.bypassesLandingEffects() && fraction > 0.0) {
                Vec3d vec3d = entity.getVelocity();
                if (vec3d.y < 0.0) {
                    entity.setVelocity(vec3d.x, -vec3d.y * fraction, vec3d.z);
                    entity.fallDistance *= 1.0f-fraction;
                    ci.cancel();
                }
            }
        }
    }
}

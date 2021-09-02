package cyborgcabbage.allarmor.mixin;

import cyborgcabbage.allarmor.AllArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({LivingEntity.class, ServerPlayerEntity.class})
public abstract class LivingEntityDieMixin extends Entity {

    public LivingEntityDieMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "onDeath")
    private void injected(DamageSource source, CallbackInfo ci) {
        float fraction = AllArmor.TNT.wearingFraction((LivingEntity) (Object) this);
        if(fraction > 0.0 && !source.isOutOfWorld()){
            for (ItemStack item : this.getArmorItems()) {
                if (item.getItem() instanceof ArmorItem armorItem){
                    item.damage(16, (LivingEntity)(Object)this, (player) -> {
                        player.sendEquipmentBreakStatus(armorItem.getSlotType());
                    });
                }
            }
            Explosion.DestructionType destructionType = this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) || (LivingEntity)(Object)this instanceof PlayerEntity ? Explosion.DestructionType.DESTROY : Explosion.DestructionType.NONE;
            this.world.createExplosion((LivingEntity) (Object) this, this.getX(), this.getY(), this.getZ(),8.0F*fraction, destructionType);
        }
    }
}

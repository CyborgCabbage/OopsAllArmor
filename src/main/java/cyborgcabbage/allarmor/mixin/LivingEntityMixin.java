package cyborgcabbage.allarmor.mixin;

import cyborgcabbage.allarmor.AllArmor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Mixin(value = LivingEntity.class, priority = 1001)
public abstract class LivingEntityMixin extends Entity{
    @Shadow @Final private DefaultedList<ItemStack> equippedArmor;

    @Shadow private Optional<BlockPos> climbingPos;

    @Shadow public abstract boolean isFallFlying();

    @Shadow protected abstract void damageHelmet(DamageSource source, float amount);

    @Shadow public abstract Iterable<ItemStack> getArmorItems();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private void injected(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if(AllArmor.BEDROCK.wearingAny((LivingEntity) (Object) this)) {
            cir.setReturnValue(false);
        }
        if(source.isProjectile()){
            Entity projectile = source.getSource();
            if(projectile instanceof PersistentProjectileEntity){
                double return_velocity = 1.0D;
                return_velocity += 9.0*AllArmor.BOW.wearingFraction((LivingEntity) (Object) this);
                if(return_velocity > 1.0D) {
                    projectile.setVelocity(projectile.getVelocity().multiply(return_velocity)); //So that when the PersistentProjectileEntity deflection code runs the effect is strengthened
                    projectile.setPitch(-projectile.getPitch());
                    projectile.prevPitch = -projectile.prevPitch;
                    cir.setReturnValue(false);
                }
            }
        }
        if(source.isOutOfWorld() && amount < Float.MAX_VALUE){//The "amount < Float.MAX_VALUE" makes sure the armor doesn't trigger on "/kill"
            if(AllArmor.ENDER_EYE.wearingAny((LivingEntity) (Object) this)) {
                if (this.world instanceof ServerWorld && !this.hasVehicle() && !this.hasPassengers() && this.canUsePortals()) {
                    RegistryKey<World> registryKey = world.getRegistryKey() == World.END ? World.OVERWORLD : World.END;
                    ServerWorld serverWorld = ((ServerWorld) world).getServer().getWorld(registryKey);
                    if (serverWorld == null) {
                        return;
                    }
                    this.moveToWorld(serverWorld);
                    this.fallDistance = 0.0f;
                }
                cir.setReturnValue(false);
            }
        }
    }
    @Inject(at = @At("HEAD"), method = "isClimbing", cancellable = true)
    private void injected(CallbackInfoReturnable<Boolean> cir) {
        if (this.isSpectator()) {
            cir.setReturnValue(false);
        } else if(AllArmor.LADDER.wearingAny((LivingEntity) (Object)this) && this.horizontalCollision){
            this.climbingPos = Optional.of(this.getBlockPos());
            cir.setReturnValue(true);
        }
    }
    @Inject(at = @At("HEAD"),method = "baseTick")
    private void injected(CallbackInfo ci){
        if(!this.world.isClient()) {
            ServerWorld sw = (ServerWorld)this.world;
            if (AllArmor.EMERALD.wearingAny((LivingEntity) (Object) this)) {
                float frequency = 1.0f/72000.0f; //Once every 60 minutes
                frequency *= AllArmor.EMERALD.wearingFraction((LivingEntity) (Object) this);
                if (this.world.random.nextFloat() < frequency) {
                    VillagerEntity villagerEntity = EntityType.VILLAGER.create(sw);
                    if (villagerEntity != null) {
                        villagerEntity.initialize(sw, sw.getLocalDifficulty(this.getBlockPos()), SpawnReason.SPAWN_EGG, null, null);
                        villagerEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), 0.0F, 0.0F);
                        sw.spawnEntityAndPassengers(villagerEntity);
                    }
                }
            }
            if (AllArmor.LIGHTNING_ROD.wearingAny((LivingEntity) (Object) this)) {
                float frequency = 1.0f/1200.0f; //Once every minute
                if (this.world.isThundering()){
                    frequency = 1.0f/100.0f;
                }
                frequency *= AllArmor.LIGHTNING_ROD.wearingFraction((LivingEntity) (Object) this);
                if (this.world.random.nextFloat() < frequency) {
                    BlockPos blockPos = this.getBlockPos();
                    if (this.world.isSkyVisible(blockPos)) {
                        if(this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) || (LivingEntity)(Object)this instanceof PlayerEntity) {
                            LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.world);
                            lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
                            lightningEntity.setChanneler(null);
                            this.world.spawnEntity(lightningEntity);
                            this.playSound(SoundEvents.ITEM_TRIDENT_THUNDER, 5.0F, 1.0F);
                        }
                    }
                }
            }
            if (AllArmor.CACTUS.wearingAny((LivingEntity) (Object) this)) {
                float fraction = AllArmor.CACTUS.wearingFraction((LivingEntity) (Object) this);
                List<Entity> collidingEntities = this.world.getOtherEntities(this, this.getBoundingBox());
                for(var e: collidingEntities){
                    e.damage(DamageSource.CACTUS, fraction);
                }
            }
            if (AllArmor.PAPER.wearingAny((LivingEntity) (Object) this)){
                Iterator<ItemStack> armor = this.getArmorItems().iterator();
                ItemStack boots = armor.next();
                ItemStack leggings = armor.next();
                ItemStack chestplate = armor.next();
                ItemStack helmet = armor.next();
                float water = (float) this.getFluidHeight(FluidTags.WATER)/this.getHeight();
                if(water > 0.00 && boots.getItem() == AllArmor.PAPER.boots){
                    boots.damage(1, (LivingEntity)(Object)this, ((player) -> {
                        player.sendEquipmentBreakStatus(EquipmentSlot.FEET);
                    }));
                }
                if(water > 0.25 && leggings.getItem() == AllArmor.PAPER.leggings){
                    leggings.damage(1, (LivingEntity)(Object)this, ((player) -> {
                        player.sendEquipmentBreakStatus(EquipmentSlot.LEGS);
                    }));
                }
                if(water > 0.50 && chestplate.getItem() == AllArmor.PAPER.chestplate){
                    chestplate.damage(1, (LivingEntity)(Object)this, ((player) -> {
                        player.sendEquipmentBreakStatus(EquipmentSlot.CHEST);
                    }));
                }
                if(water > 0.75 && helmet.getItem() == AllArmor.PAPER.helmet){
                    helmet.damage(1, (LivingEntity)(Object)this, ((player) -> {
                        player.sendEquipmentBreakStatus(EquipmentSlot.HEAD);
                    }));
                }
            }
        }
    }
    /*@ModifyVariable(at = @At(value="INVOKE",target="Lnet/minecraft/enchantment/EnchantmentHelper;getDepthStrider(Lnet/minecraft/entity/LivingEntity;)I"),method = "travel", name="d")
    private double inject(double x){
        float buoyancy = AllArmor.BOAT.wearingFraction((LivingEntity) (Object) this);
        float entityHeight = this.getHeight();
        float waterHeight = Math.min((float)this.getFluidHeight(FluidTags.WATER),entityHeight);
        if(buoyancy > 0.0){
            System.out.println("Water Level: "+(waterHeight/entityHeight));
        }
        return x-buoyancy*(waterHeight/entityHeight)*0.75;
    }*/
    @Inject(at = @At(value="INVOKE",target="Lnet/minecraft/enchantment/EnchantmentHelper;getDepthStrider(Lnet/minecraft/entity/LivingEntity;)I"),method = "travel")
    private void inject(Vec3d movementInput, CallbackInfo ci){
        float buoyancy = AllArmor.BOAT.wearingFraction((LivingEntity) (Object) this);
        float entityHeight = this.getHeight();
        float waterHeight = Math.min((float)this.getFluidHeight(FluidTags.WATER),entityHeight);
        Vec3d vel = this.getVelocity();
        this.setVelocity(vel.x,vel.y+buoyancy*(waterHeight/entityHeight)*0.06,vel.z);
    }
    @Redirect(method = "travel", at = @At(value = "INVOKE", target="Lnet/minecraft/enchantment/EnchantmentHelper;getDepthStrider(Lnet/minecraft/entity/LivingEntity;)I"))
    private int inject(LivingEntity entity){
        float buoyancy = AllArmor.BOAT.wearingFraction((LivingEntity) (Object) this);
        return EnchantmentHelper.getDepthStrider(entity)+(int)(buoyancy*3.0f);
    }
    @Inject(method="tick",at=@At("HEAD"))
    private void inject(CallbackInfo ci){
        double fraction = AllArmor.DRAGON_BREATH.wearingFraction((LivingEntity)(Object)this);
        if(fraction > 0.0) {
            Vec3d vel = this.getVelocity();
            double force = 0.04*fraction;
            if (this.isFallFlying() || this.isSwimming()) {
                Vec3d thrust = this.getRotationVector().normalize().multiply(force);
                if(vel.length() < 0.5) {
                    this.setVelocity(vel.add(thrust));
                }
                if(this.world.isClient) {
                    if(this.random.nextFloat() < fraction) {
                        this.world.addParticle(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY(), this.getZ(), vel.x - thrust.x * 4.0 + this.random.nextGaussian() * 0.05, vel.y - thrust.y * 4.0 + this.random.nextGaussian() * 0.05, vel.z - thrust.z * 4.0 + this.random.nextGaussian() * 0.05);
                    }
                }
            }
        }
        /*double fraction = AllArmor.DRAGON_BREATH.wearingFraction((LivingEntity)(Object)this);
        if(fraction > 0.0 && !this.onGround) {
            Vec3d vel = this.getVelocity();
            double force = 0.04*fraction;
            Vec3d thrust;
            if (this.isFallFlying() || this.isSwimming()) {
                thrust = this.getRotationVector().normalize().multiply(force);
            }else{
                thrust = (new Vec3d(0.0,1.0,0.0)).multiply(force);
            }
            boolean isFlying = false;
            if((LivingEntity)(Object)this instanceof PlayerEntity player) {
                if(player.getAbilities().flying){
                    isFlying = true;
                }
            }
            if(!isFlying){
                this.setVelocity(vel.add(thrust));
                if(this.world.isClient) {
                    this.world.addParticle(ParticleTypes.DRAGON_BREATH, this.getX(), this.getY(), this.getZ(), vel.x - thrust.x * 4.0 + this.random.nextGaussian() * 0.05, vel.y - thrust.y * 4.0 + this.random.nextGaussian() * 0.05, vel.z - thrust.z * 4.0 + this.random.nextGaussian() * 0.05);
                    System.out.println(this.getVelocity());
                }
            }
        }*/
    }
}

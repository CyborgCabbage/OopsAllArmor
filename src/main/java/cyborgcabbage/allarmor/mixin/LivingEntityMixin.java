package cyborgcabbage.allarmor.mixin;

import cyborgcabbage.allarmor.AllArmor;
import cyborgcabbage.allarmor.item.ArmorSet;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(value = LivingEntity.class, priority = 1001)
public abstract class LivingEntityMixin extends Entity{
    @Shadow private Optional<BlockPos> climbingPos;

    @Shadow public abstract boolean isFallFlying();

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
                AllArmor.BOW.damage((LivingEntity) (Object) this,2);
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
        } else if(AllArmor.LADDER.wearingAny((LivingEntity) (Object)this)){
            Iterable<VoxelShape> blockCollisions = this.world.getBlockCollisions(null, this.getBoundingBox().expand(0.3, 0.0, 0.3));
            for (VoxelShape voxelShape : blockCollisions) {
                if (voxelShape.isEmpty()) continue;
                this.climbingPos = Optional.of(this.getBlockPos());
                cir.setReturnValue(true);
                break;
            }
        }
    }

    @Inject(at = @At("HEAD"),method = "baseTick")
    private void injected(CallbackInfo ci){
        if(!this.world.isClient()) {
            ServerWorld sw = (ServerWorld)this.world;
            if (AllArmor.EMERALD.wearingAny((LivingEntity) (Object) this)) {
                float frequency = 1.0f/72000.0f; //Once every 60 minutes
                frequency *= AllArmor.EMERALD.wearingFraction((LivingEntity) (Object) this);
                if (sw.random.nextFloat() < frequency) {
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
                if (sw.isThundering()){
                    frequency = 1.0f/100.0f;
                }
                frequency *= AllArmor.LIGHTNING_ROD.wearingFraction((LivingEntity) (Object) this);
                if (sw.random.nextFloat() < frequency) {
                    BlockPos blockPos = this.getBlockPos();
                    if(blockPos != null) {
                        if (sw.isSkyVisible(blockPos)) {
                            if (sw.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING) || (LivingEntity) (Object) this instanceof PlayerEntity) {
                                LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(sw);
                                lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
                                lightningEntity.setChanneler(null);
                                sw.spawnEntity(lightningEntity);
                                this.playSound(SoundEvents.ITEM_TRIDENT_THUNDER, 5.0F, 1.0F);
                            }
                        }
                    }
                }
            }
            if (AllArmor.CACTUS.wearingAny((LivingEntity) (Object) this)) {
                float fraction = AllArmor.CACTUS.wearingFraction((LivingEntity) (Object) this);
                List<Entity> collidingEntities = sw.getOtherEntities(this, this.getBoundingBox());
                for(var e: collidingEntities){
                    e.damage(DamageSource.CACTUS, fraction);
                }
            }
            if (AllArmor.PAPER.wearingAny((LivingEntity) (Object) this)){
                ArrayList<ItemStack> armorItems = ArmorSet.getArmorItems((LivingEntity) (Object) this);
                float water = (float) this.getFluidHeight(FluidTags.WATER)/this.getHeight();
                for(int i = 0; i < 4; i++){
                    ItemStack itemStack = armorItems.get(i);
                    if(water > i*0.25f && AllArmor.PAPER.isFromSet(itemStack)){
                        itemStack.damage(1, (LivingEntity)(Object)this, ((player) -> {
                            player.sendEquipmentBreakStatus(ArmorSet.getSlot(itemStack));
                        }));
                    }
                }
            }
            if (AllArmor.ENCHANTING_TABLE.wearingAny((LivingEntity) (Object) this)){
                float frequency = 0.005f;
                ArrayList<ItemStack> armorItems = AllArmor.ENCHANTING_TABLE.getThisArmorItems((LivingEntity) (Object) this);
                for(ItemStack itemStack: armorItems){
                    if(sw.random.nextFloat() < frequency){
                        itemStack.removeSubNbt("Enchantments");
                        EnchantmentHelper.enchant(sw.random, itemStack,30,true);
                    }
                }
            }
            if(AllArmor.BONE_MEAL.wearingAny((LivingEntity) (Object) this)){
                float frequency = 0.2f;
                if(sw.random.nextFloat() < frequency*AllArmor.BONE_MEAL.wearingFraction((LivingEntity)(Object)this)) {
                    BlockPos blockPos = this.getBlockPos().add(random.nextInt(6) - 3, random.nextInt(4) - 2, random.nextInt(6) - 3);
                    if (world.getBlockState(blockPos) != null) {
                        if (BoneMealItem.useOnFertilizable(new ItemStack(Items.BONE_MEAL, 64), world, blockPos) || BoneMealItem.useOnGround(new ItemStack(Items.BONE_MEAL, 64), world, blockPos, null)) {
                            if (!world.isClient) {
                                world.syncWorldEvent(WorldEvents.BONE_MEAL_USED, blockPos, 0);
                            }
                            AllArmor.BONE_MEAL.damage((LivingEntity) (Object) this, 3);
                        }
                    }
                }
            }
        }
    }

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
                if (!this.world.isClient && this.age % 20 == 0) {
                    AllArmor.DRAGON_BREATH.damageEach((LivingEntity)(Object)this, 1);
                }
            }
        }
    }

    @Redirect(method = "getArmorVisibility", at = @At(value = "INVOKE",target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    public boolean injected(ItemStack itemStack){
        if (AllArmor.LAPIS_LAZULI.isFromSet(itemStack)) {
            return true;
        }
        return itemStack.isEmpty();
    }
}

package cyborgcabbage.allarmor.mixin;

import cyborgcabbage.allarmor.AllArmor;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;


@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);

    @Shadow public World world;

    @Shadow @Final protected Random random;

    @Shadow private Box entityBounds;

    @Inject(method="playStepSound", at = @At("HEAD"), cancellable = true)
    public void inject(BlockPos pos, BlockState state, CallbackInfo ci){
        if(!state.getMaterial().isLiquid() && (Entity)(Object)this instanceof LivingEntity livingEntity){
            if (AllArmor.NOTE_BLOCK.wearingBoots(livingEntity) || AllArmor.NOTE_BLOCK.wearingLeggings(livingEntity)) {
                BlockState blockState = this.world.getBlockState(pos.up());
                if (blockState.isIn(BlockTags.INSIDE_STEP_SOUND_BLOCKS)) {
                    BlockSoundGroup blockSoundGroup = blockState.getSoundGroup();
                    this.playSound(blockSoundGroup.getStepSound(), blockSoundGroup.getVolume() * 0.15F, blockSoundGroup.getPitch());
                } else {
                    int c_maj_cd = switch (this.random.nextInt(7)) {
                        case 0 -> 0;//C
                        case 1 -> 2;//D
                        case 2 -> 4;//E
                        case 3 -> 5;//F
                        case 4 -> 7;//G
                        case 5 -> 9;//A
                        case 6 -> 11;//B
                        default -> 20;//B
                    };
                    this.playSound(Instrument.fromBlockState(state).getSound(), 1f, (float) Math.pow(2.0D, (double) (c_maj_cd - 12) / 12.0D));
                }
                ci.cancel();
            }
        }
    }
}

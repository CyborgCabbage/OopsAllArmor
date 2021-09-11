package cyborgcabbage.allarmor.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.Instrument;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Shadow @Final private MinecraftClient client;

    @Redirect(method = "updateBlockBreakingProgress", at = @At(value="INVOKE",target="Lnet/minecraft/client/sound/SoundManager;play(Lnet/minecraft/client/sound/SoundInstance;)V"))
    public void inject(SoundManager soundManager, SoundInstance sound){
        try {
            BlockPos pos = new BlockPos(sound.getX(), sound.getY(), sound.getZ());
            BlockState blockState = this.client.world.getBlockState(pos);
            soundManager.play(new PositionedSoundInstance(Instrument.fromBlockState(blockState).getSound(), SoundCategory.BLOCKS, sound.getVolume(), sound.getPitch(), pos));
        } catch(Exception e) {
            soundManager.play(sound);
        }
    }
}

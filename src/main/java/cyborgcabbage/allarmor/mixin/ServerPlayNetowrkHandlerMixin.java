package cyborgcabbage.allarmor.mixin;

import cyborgcabbage.allarmor.AllArmor;
import net.minecraft.block.enums.Instrument;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetowrkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method="onUpdateSelectedSlot", at = @At("HEAD"))
    public void inject(UpdateSelectedSlotC2SPacket packet, CallbackInfo ci) {
        if (!player.world.isClient()) {
            if (AllArmor.NOTE_BLOCK.wearingHelmet(player) || AllArmor.NOTE_BLOCK.wearingChestplate(player)) {
                int c_maj_cd = switch (packet.getSelectedSlot()) {
                    case 0 -> 0;//C
                    case 1 -> 2;//D
                    case 2 -> 4;//E
                    case 3 -> 5;//F
                    case 4 -> 7;//G
                    case 5 -> 9;//A
                    case 6 -> 11;//B
                    case 7 -> 12;//C
                    case 8 -> 14;
                    default -> 0;//D
                };
                player.playSound(
                        Instrument.fromBlockState(player.world.getBlockState(player.getLandingPos())).getSound(),
                        SoundCategory.RECORDS,
                        1f,
                        (float) Math.pow(2.0D, 1.0 + (double) (c_maj_cd - 12) / 12.0D)
                );
            }
        }
    }
}

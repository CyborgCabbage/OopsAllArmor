package cyborgcabbage.allarmor.mixin;

import cyborgcabbage.allarmor.AllArmor;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow @Nullable public ClientPlayerEntity player;

    @Redirect(method = "handleInputEvents", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I", opcode = Opcodes.PUTFIELD))
    private void injected(PlayerInventory playerInventory, int value) {
        playerInventory.selectedSlot = value;
        if(this.player != null) {
            if (AllArmor.NOTE_BLOCK.wearingAny(this.player)) {
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeByte(value);//value = 0 to 8
                ClientPlayNetworking.send(new Identifier("allarmor", "note_block_armor"), buf);
            }
        }
    }
}

package cyborgcabbage.allarmor.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.text.LiteralText;

@Environment(EnvType.CLIENT)
public class AllArmorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            //for(int i=0;i<9;i++) {
            //    while (client.options.keysHotbar[i].wasPressed()) {
            //        client.player.sendMessage(new LiteralText("Key "+(i+1)+" was pressed!"), false);
            //    }
            //}
        });
    }
}

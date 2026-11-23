package dev.donutlab.sugarlens.mixin;

import dev.donutlab.sugarlens.fabric.SugarLensFabric;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Observes the vanilla server brand payload without cancelling or changing it. */
@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Inject(method = "handleCustomPayload", at = @At("HEAD"))
    private void sugarlens$captureBrand(ClientboundCustomPayloadPacket packet, CallbackInfo ci) {
        if (packet == null) return;
        CustomPacketPayload payload = packet.payload();
        if (payload == null) return;
        try {
            String id = String.valueOf(payload.type().id());
            if (!"minecraft:brand".equals(id)) return;
            Object value = payload.getClass().getMethod("brand").invoke(payload);
            if (value instanceof String brand) SugarLensFabric.recordBrandPayload(brand);
        } catch (ReflectiveOperationException | RuntimeException ignored) {
            // Unknown payload implementations are ignored; the scan remains safe.
        }
    }
}

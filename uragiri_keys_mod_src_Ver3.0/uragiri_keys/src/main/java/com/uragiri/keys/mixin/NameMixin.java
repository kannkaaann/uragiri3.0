package com.uragiri.keys.mixin;

import com.uragiri.keys.Impersonation;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class NameMixin {
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true, require = 0)
    private void uragiri$name(CallbackInfoReturnable<Text> cir) {
        if (!((Object) this instanceof AbstractClientPlayerEntity self)) {
            return;
        }
        AbstractClientPlayerEntity target = Impersonation.findTarget(self);
        if (target != null) {
            cir.setReturnValue(target.getDisplayName());
        }
    }
}

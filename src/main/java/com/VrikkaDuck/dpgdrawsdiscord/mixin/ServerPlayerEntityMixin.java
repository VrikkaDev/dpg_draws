package com.VrikkaDuck.dpgdrawsdiscord.mixin;

import com.VrikkaDuck.dpgdrawsdiscord.Variables;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {

    @Inject(method = "getPlayerListName", at = @At("TAIL"), cancellable = true)
    private void gpln(CallbackInfoReturnable<Text> cir){
        ServerPlayerEntity ent = ((ServerPlayerEntity) (Object) this);

        if(ent.getScoreboardTeam() == null){
            cir.setReturnValue(Variables.configHandler.DecorateName(ent.getEntityName()));
            return;
        }
        cir.setReturnValue(ent.getScoreboardTeam().decorateName(Variables.configHandler.DecorateName(ent.getEntityName())));
    }

}

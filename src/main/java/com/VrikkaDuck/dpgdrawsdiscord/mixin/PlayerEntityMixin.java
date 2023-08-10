package com.VrikkaDuck.dpgdrawsdiscord.mixin;

import com.VrikkaDuck.dpgdrawsdiscord.ConfigHandler;
import com.VrikkaDuck.dpgdrawsdiscord.Variables;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {


    @ModifyArg(method = "getDisplayName", at = @At(value = "INVOKE", target = "Lnet/minecraft/scoreboard/Team;decorateName(Lnet/minecraft/scoreboard/AbstractTeam;Lnet/minecraft/text/Text;)Lnet/minecraft/text/MutableText;"))
    private Text replaceName(Text text) {
        PlayerEntity ent = ((PlayerEntity) (Object) this);

        if (Variables.configHandler == null){
            MinecraftServer server = ent.getServer();

            if (server == null){
                return ent.getName();
            }

            Variables.configHandler = new ConfigHandler(server);
        }

        return Variables.configHandler.DecorateName(ent.getEntityName());
    }
}

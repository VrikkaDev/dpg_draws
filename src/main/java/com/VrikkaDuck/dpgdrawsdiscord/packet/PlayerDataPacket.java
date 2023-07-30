package com.VrikkaDuck.dpgdrawsdiscord.packet;

import com.VrikkaDuck.dpgdrawsdiscord.Variables;
import com.VrikkaDuck.dpgdrawsdiscord.utils.JsonUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.Objects;

public class PlayerDataPacket extends Packet{
    public PlayerDataPacket(String type, JsonObject data){
        super(type, data);

        Variables.LINKED_ACCOUNTS.clear();

        for(Map.Entry<String, JsonElement> entry : data.asMap().entrySet()){
            JsonObject jb = entry.getValue().getAsJsonObject();
            Variables.LINKED_ACCOUNTS.put(entry.getKey(), new String[]{jb.get("discord_name").getAsString(),
                    jb.get("minecraft_name").getAsString(), jb.get("data").getAsJsonObject().toString()});
        }

        MinecraftServer server = Variables.configHandler.server;

        if(server == null){
            return;
        }

        for(ServerPlayerEntity playerEntity : server.getPlayerManager().getPlayerList()){
            String tm = "";

            for(Map.Entry<String, String[]> entry : Variables.LINKED_ACCOUNTS.entrySet()){
                if(entry.getValue()[1].equalsIgnoreCase(playerEntity.getName().getString())){
                    tm = entry.getValue()[2];
                    break;
                }
            }

            if(tm.equals("")){
                return;
            }

            JsonObject dataobj = JsonUtils.String2Json(tm);

            assert dataobj != null;
            if(dataobj.has("spec")){
                Variables.configHandler.SetSpec(playerEntity, dataobj.get("spec").getAsString());
            }
            if(dataobj.has("pvp")){
                Variables.configHandler.SetPvp(playerEntity, dataobj.get("pvp").getAsString());
            }
        }
    }

    @Override
    public String ProcessResponse(){
        return "";
    }
}

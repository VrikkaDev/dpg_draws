package com.VrikkaDuck.dpgdrawsdiscord.packet;

import com.VrikkaDuck.dpgdrawsdiscord.Variables;
import com.google.gson.JsonObject;

import java.util.Map;

public class VerifyLinkingPacket extends Packet{
    public VerifyLinkingPacket(String type, JsonObject data) {
        super(type, data);
    }

    @Override
    public String ProcessResponse(){

        if(Variables.WAITING_TO_GET_LINKED.isEmpty()){
            return "{}";
        }

        JsonObject root = new JsonObject();

        Map.Entry<String, String[]> v = Variables.WAITING_TO_GET_LINKED.entrySet().stream().toList().get(0);
        Variables.WAITING_TO_GET_LINKED.remove(v.getKey());

        root.addProperty("discord_id", v.getKey());
        root.addProperty("discord_name", v.getValue()[0]);
        root.addProperty("minecraft_name", v.getValue()[1]);
        return root.toString();
    }
}

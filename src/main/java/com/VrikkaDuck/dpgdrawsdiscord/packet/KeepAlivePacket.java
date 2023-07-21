package com.VrikkaDuck.dpgdrawsdiscord.packet;

import com.VrikkaDuck.dpgdrawsdiscord.Variables;
import com.google.gson.JsonObject;

import java.util.Map;

public class KeepAlivePacket extends Packet{
    public KeepAlivePacket(String type, JsonObject data) {
        super(type, data);
    }

    @Override
    public String ProcessResponse(){

        Map<String, String[]> wtgl = Variables.WAITING_TO_GET_LINKED;

        if(wtgl.isEmpty()){
            return "";
        }

        Map.Entry<String, String[]> ent = wtgl.entrySet().stream().toList().get(0);

        JsonObject root = new JsonObject();

        root.addProperty("verify_linking", "true");

        return root.toString();
    }

}

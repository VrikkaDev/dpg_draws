package com.VrikkaDuck.dpgdrawsdiscord.packet;

import com.VrikkaDuck.dpgdrawsdiscord.Variables;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Objects;

public class ConnectPacket extends Packet{

    private boolean didSetup = false;

    public ConnectPacket(String type, JsonObject data) {
        super(type, data);

        if (Variables.configHandler == null || Variables.configHandler.server == null || Variables.configHandler.server.getPlayerManager() == null){
            return;
        }

        Variables.TRANSLATIONS.clear();

        for(Map.Entry<String, JsonElement> entry : data.asMap().entrySet()){
            if(Objects.equals(entry.getKey(), "teams")){
                Variables.configHandler.loadFromJsonObject(entry.getValue().getAsJsonObject());
                continue;
            }
            Variables.TRANSLATIONS.put(entry.getKey(), entry.getValue().getAsString());
        }


        this.didSetup = true;
    }
    @Override
    public String ProcessResponse(){
        return this.didSetup ? "true" : "false";
    }
}

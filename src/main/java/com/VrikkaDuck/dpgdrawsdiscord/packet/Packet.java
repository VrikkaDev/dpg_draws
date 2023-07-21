package com.VrikkaDuck.dpgdrawsdiscord.packet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Packet {
    private String type;
    private JsonObject data;

    public static Packet FromJson(String json){
        JsonElement element = JsonParser.parseString(json);
        if (element != null && element.isJsonObject())
        {
            JsonObject root = element.getAsJsonObject();
            String type = root.get("type").getAsString();
            JsonObject data = root.get("data").getAsJsonObject();

            switch (type) {
                case "connect" -> {
                    return new ConnectPacket(type, data);
                }
                case "keep_alive" -> {
                    return new KeepAlivePacket(type, data);
                }
                case "link_account" -> {
                    return new LinkAccountPacket(type, data);
                }
                case "verify_linking" -> {
                    return new VerifyLinkingPacket(type, data);
                }
                case "player_data" -> {
                    return new PlayerDataPacket(type, data);
                }
                default -> {
                }
            }
        }
        return null;
    }

    public Packet(String type, JsonObject data){
        this.type = type;
        this.data = data;
    }

    public String ProcessResponse(){
        return "";
    }

    @Override
    public String toString() {
        return "Packet{" +
                "type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}


package com.VrikkaDuck.dpgdrawsdiscord.packet;

import com.VrikkaDuck.dpgdrawsdiscord.Variables;
import com.VrikkaDuck.dpgdrawsdiscord.utils.JsonUtils;
import com.google.gson.JsonObject;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Objects;

public class LinkAccountPacket extends Packet{

    private final String discord_id;
    private final String discord_name;
    private final String minecraft_name;

    public LinkAccountPacket(String type, JsonObject data) {
        super(type, data);

        this.discord_id = data.get("discord_id").getAsString();
        this.discord_name = data.get("discord_name").getAsString();
        this.minecraft_name = data.get("minecraft_name").getAsString();
    }

    private String genJson(boolean success, String responseText){
        JsonObject root = new JsonObject();

        JsonObject obj = JsonUtils.GetNestedObject(root, "response", true);
        assert obj != null;
        obj.addProperty("success", success);

        JsonObject data = new JsonObject();
        data.addProperty("response", responseText);

        obj.add("data", data);
        return obj.toString();
    }

    @Override
    public String ProcessResponse(){

        MinecraftServer server = Variables.configHandler.server;

        boolean isIn = false;
        for(String name : Arrays.stream(server.getPlayerNames()).toList()){
            if(name.equalsIgnoreCase(this.minecraft_name)){
                isIn = true;
                break;
            }
        }

        if(!isIn)
        {
            return genJson(true, "player_not_in_server");
        }

        if(Variables.TRANSLATIONS == null || !Variables.TRANSLATIONS.containsKey("mc_player_verification")){
            return genJson(false, "TRANSLATIONS doesn't have key: mc_player_verification");
        }

        Text t = Text.of(Variables.TRANSLATIONS.get("mc_player_verification").formatted(this.discord_name));
        Text tyes = Text.literal("[Yes]").styled((style ->
            style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/verify true " + this.discord_name))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(Variables.TRANSLATIONS.get("mc_link_account_button_yes"))))));
        Text tno = Text.literal("[No]").styled((style ->
                style.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/verify false " + this.discord_name))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal(Variables.TRANSLATIONS.get("mc_link_account_button_no"))))));

        t = t.copy().append(tyes).append(" ").append(tno);

        Objects.requireNonNull(server.getPlayerManager().getPlayer(this.minecraft_name)).sendMessageToClient(t, false);

        Variables.WAITING_TO_GET_LINKED.put(this.discord_id, new String[]{this.discord_name, this.minecraft_name});

        return genJson(true, "message_sent_to_player");
    }
}

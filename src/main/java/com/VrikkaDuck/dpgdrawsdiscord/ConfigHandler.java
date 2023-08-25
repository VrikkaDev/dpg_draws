package com.VrikkaDuck.dpgdrawsdiscord;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;

import java.util.*;

public class ConfigHandler {

    private Map<String, Formatting> specmap = new HashMap<>();
    private Map<String, Text> pvpmap = new HashMap<>();
    private Map<String, Text> names = new HashMap<>();
    private Map<String, Text> pvps = new HashMap<>();
    private List<Team> teams = new ArrayList<>();

    public final MinecraftServer server;

    public ConfigHandler(MinecraftServer server){
        this.server = server;
        this.teams = this.server.getScoreboard().getTeams().stream().toList();
    }

    private void RefreshTeam(ServerPlayerEntity playerEntity){

        ServerScoreboard sb = this.server.getScoreboard();

        Team t = sb.getTeam(playerEntity.getUuidAsString());

        if(t == null){
            t = sb.addTeam(playerEntity.getUuidAsString());
            t.setColor(Formatting.WHITE);
        }

        if(!playerEntity.isTeamPlayer(t)){
            sb.addPlayerToTeam(playerEntity.getName().getString(), t);
        }
    }
    public Text DecorateName(String name){

        Text t = this.pvps.containsKey(name) ? this.pvps.get(name) : Text.empty();
        Text n = this.names.containsKey(name) ? this.names.get(name) : Text.of(name);

        return Text.empty().append(n).append(" ").append(t);
    }
    private float normalize(float value, float min, float max) {
        if (min >= max) {
            throw new IllegalArgumentException("Minimum value must be less than maximum value");
        }

        value = Math.max(min, Math.min(max, value));

        return (value - min) / (max - min);
    }

    public void SetSpec(ServerPlayerEntity playerEntity, String spec){
        this.RefreshTeam(playerEntity);
        Team t = this.server.getScoreboard().getTeam(playerEntity.getUuidAsString());
        assert t != null;

        String entname = playerEntity.getEntityName();

        List<String> a = new ArrayList<>(Arrays.stream(spec.split(",")).toList());

        a.removeIf(tas -> tas.contains("none"));

        Text prefix = Text.empty();

        int i = 1;
        int mi = 0;
        int en = entname.length()/2;
        float prevend = 0;

        for (String ts : a){
            if(!this.specmap.containsKey(ts)){
                break;
            }
            if(a.size() == 1){
                prefix = Text.of(entname);
                t.setColor(this.specmap.get(ts));
                break;
            }

            String ns = entname.substring(mi, en);

            mi = en;
            en = entname.length();

            Text te = Text.empty();

            float temprev = 0;
            for (int ie = 0; ie < ns.length(); ie++){
                float normalized = normalize(ie, 0, ns.length());
                normalized /= 2;
                normalized += prevend;
                temprev = normalized;
                int col = ColorHelper.Argb.lerp(normalized, this.specmap.get(a.get(0)).getColorValue(), this.specmap.get(a.get(1)).getColorValue());


                te = te.copy().append(Text.of(String.valueOf(ns.charAt(ie))).getWithStyle(Style.EMPTY.withColor(col)).get(0));
            }
            prevend = temprev;


            prefix = prefix.copy().append(te);

            i++;
        }

        if(a.isEmpty()){
            t.setColor(Formatting.WHITE);
            prefix = playerEntity.getName();
        }


        this.names.put(entname, prefix);

        playerEntity.setCustomName(prefix);
        playerEntity.setCustomNameVisible(true);

        Objects.requireNonNull(playerEntity.getServer()).getPlayerManager().sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, playerEntity));
    }
    public void SetPvp(ServerPlayerEntity playerEntity, String pvp){
        this.RefreshTeam(playerEntity);
        Team t = this.server.getScoreboard().getTeam(playerEntity.getUuidAsString());
        assert t != null;
        if(this.pvpmap.containsKey(pvp)){
            //t.setSuffix(this.pvpmap.get(pvp));
            this.pvps.put(playerEntity.getEntityName(), this.pvpmap.get(pvp));
        }else{
            t.setSuffix(Text.empty());
            this.pvps.put(playerEntity.getEntityName(), Text.empty());
        }
        Objects.requireNonNull(playerEntity.getServer()).getPlayerManager().sendToAll(new PlayerListS2CPacket(PlayerListS2CPacket.Action.UPDATE_DISPLAY_NAME, playerEntity));
    }

    public void loadFromJsonObject(JsonObject root){
        if (root != null)
        {
            //this.teams = this.server.getScoreboard().getTeams().stream().toList();


            Map<String, JsonElement> spm = root.get("spec").getAsJsonObject().asMap();
            for(Map.Entry<String, JsonElement> entry : spm.entrySet()){

                JsonObject val = entry.getValue().getAsJsonObject();

                if(val.has("name_color")){
                    this.specmap.put(entry.getKey(),Formatting.byName(val.get("name_color").getAsString()));
                }
            }

            Map<String, JsonElement> ppm = root.get("pvp").getAsJsonObject().asMap();
            for(Map.Entry<String, JsonElement> entry : ppm.entrySet()){
                JsonObject val = entry.getValue().getAsJsonObject();

                if(val.has("suffix")){
                    JsonObject suffix = val.getAsJsonObject("suffix");
                    Formatting col = Formatting.byName(suffix.get("color").getAsString());
                    this.pvpmap.put(entry.getKey(), Text.of(" " + suffix.get("text").getAsString()).getWithStyle(Style.EMPTY.withColor(col)).get(0));
                }
            }
        }
    }
}

package com.VrikkaDuck.dpgdrawsdiscord;

import com.google.gson.*;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ColorHelper;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigHandler {

    private Map<String, Formatting> specmap = new HashMap<>();
    private Map<String, Text> pvpmap = new HashMap<>();
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
    public void SetSpec(ServerPlayerEntity playerEntity, String spec){
        this.RefreshTeam(playerEntity);
        Team t = this.server.getScoreboard().getTeam(playerEntity.getUuidAsString());
        assert t != null;

        if(!this.specmap.containsKey(spec)){
            return;
        }
        t.setColor(this.specmap.get(spec));
    }
    public void SetPvp(ServerPlayerEntity playerEntity, String pvp){
        this.RefreshTeam(playerEntity);
        Team t = this.server.getScoreboard().getTeam(playerEntity.getUuidAsString());
        assert t != null;
        if(this.pvpmap.containsKey(pvp)){
            t.setSuffix(this.pvpmap.get(pvp));
        }
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

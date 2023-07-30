package com.VrikkaDuck.dpgdrawsdiscord;

import com.google.gson.*;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.scoreboard.Scoreboard;
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
import java.util.HashMap;
import java.util.Map;

public class ConfigHandler {

    private Map<String, Team> teams = new HashMap<String, Team>();

    public final MinecraftServer server;

    public ConfigHandler(MinecraftServer server){
        this.server = server;
    }

    public void SetTeam(ServerPlayerEntity playerEntity, String team){
        this.server.getScoreboard().addPlayerToTeam(playerEntity.getName().getString(), this.teams.get(team));
    }

    public void loadFromJsonObject(JsonObject root){
        if (root != null)
        {
            Map<String, JsonElement> am = root.asMap();
            Scoreboard sb = this.server.getScoreboard();
            for(Map.Entry<String, JsonElement> entry : am.entrySet()){
                Team t = sb.getTeam(entry.getKey());
                if (t == null){
                    t = sb.addTeam(entry.getKey());
                }
                JsonObject val = entry.getValue().getAsJsonObject();
                if(val.has("suffix")){
                    JsonObject suffix = val.getAsJsonObject("suffix");
                    Formatting col = Formatting.byName(suffix.get("color").getAsString());
                    t.setSuffix(Text.of(" " + suffix.get("text").getAsString()).getWithStyle(Style.EMPTY.withColor(col)).get(0));
                }
                if(val.has("name_color")){
                    t.setColor(Formatting.byName(val.get("name_color").getAsString()));
                }
                this.teams.put(entry.getKey(), t);
            }
        }
    }
}

package com.VrikkaDuck.dpgdrawsdiscord.utils;

import com.google.gson.*;
import org.jetbrains.annotations.Nullable;

public class JsonUtils {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    @Nullable
    public static JsonObject GetNestedObject(JsonObject parent, String key, boolean create)
    {
        if (!parent.has(key) || !parent.get(key).isJsonObject())
        {
            if (!create)
            {
                return null;
            }
            JsonObject obj = new JsonObject();
            parent.add(key, obj);
            return obj;
        }
        else
        {
            return parent.get(key).getAsJsonObject();
        }
    }

    @Nullable
    public static JsonObject String2Json(String val){
        JsonElement element = JsonParser.parseString(val);
        if (element != null && element.isJsonObject()) {
            return(element.getAsJsonObject());
        }
        return null;
    }
}

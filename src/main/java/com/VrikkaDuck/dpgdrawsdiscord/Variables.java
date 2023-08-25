

package com.VrikkaDuck.dpgdrawsdiscord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Variables {
    public static final Logger LOGGER = LoggerFactory.getLogger("dpgdraws-discord");
    public static final String MODID = "dpgdraws-discord";

    public static Map<String, String> TRANSLATIONS = new HashMap<>();
    public static Map<String, String[]> WAITING_TO_GET_LINKED = new HashMap<>();
    public static Map<String, String[]> WAITING_TO_GET_VERIFIED = new HashMap<>();
    public static Map<String, String[]> LINKED_ACCOUNTS = new HashMap<>();
    public static ConfigHandler configHandler;

}

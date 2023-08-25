package com.VrikkaDuck.dpgdrawsdiscord.command;

import com.VrikkaDuck.dpgdrawsdiscord.Variables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Map;

import static net.minecraft.command.CommandSource.suggestMatching;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class VerifyCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("verify")
                .then(argument("agree", BoolArgumentType.bool())
                        .suggests((c,b)->suggestMatching(new String[]{"true", "false"}, b))
                            .then(argument("discord_name", StringArgumentType.greedyString())
                                    .executes(VerifyCommand::Verify)))
                );
    }

    private static Map.Entry<String, String[]> getEntryFromMcName(Map<String, String[]> wtgv, String mcName){
        Map.Entry<String, String[]> r = null;

        for (Map.Entry<String, String[]> entry : wtgv.entrySet()){
            String mn = entry.getValue()[1];
            if (mn.equalsIgnoreCase(mcName)){
                r = entry;
                break;
            }
        }

        return r;
    }
    private static int Verify(CommandContext<ServerCommandSource> context){
        if(BoolArgumentType.getBool(context, "agree")){

            Map<String, String[]> wtgv = Variables.WAITING_TO_GET_VERIFIED;

            Map.Entry<String, String[]> entry = getEntryFromMcName(wtgv, context.getSource().getName());

            if(entry == null){
                Variables.LOGGER.warn("Cant get entry from mc name.");
                return 1;
            }

            Variables.WAITING_TO_GET_VERIFIED.remove(entry.getKey());
            Variables.WAITING_TO_GET_LINKED.put(entry.getKey(), entry.getValue());

            context.getSource().sendMessage(Text.of(Variables.TRANSLATIONS.get("mc_link_account_yes_command")));

        }else{
            context.getSource().sendMessage(Text.of(Variables.TRANSLATIONS.get("mc_link_account_no_command")));
        }

        return 0;
    }
}

package com.VrikkaDuck.dpgdrawsdiscord.command;

import com.VrikkaDuck.dpgdrawsdiscord.Variables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

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
    private static int Verify(CommandContext<ServerCommandSource> context){
        if(BoolArgumentType.getBool(context, "agree")){
            context.getSource().sendMessage(Text.of(Variables.TRANSLATIONS.get("mc_link_account_yes_command")));
        }else{
            context.getSource().sendMessage(Text.of(Variables.TRANSLATIONS.get("mc_link_account_no_command")));
        }

        return 0;
    }
}

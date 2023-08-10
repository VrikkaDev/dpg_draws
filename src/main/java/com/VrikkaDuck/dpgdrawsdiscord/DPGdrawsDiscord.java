package com.VrikkaDuck.dpgdrawsdiscord;

import com.VrikkaDuck.dpgdrawsdiscord.command.VerifyCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class DPGdrawsDiscord implements ModInitializer {

	@Override
	public void onInitialize() {

		Thread conthread = new DiscordHandler();
		conthread.start();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> VerifyCommand.register(dispatcher));
	}

}
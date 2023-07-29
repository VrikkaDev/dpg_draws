package com.VrikkaDuck.dpgdrawsdiscord;

import com.VrikkaDuck.dpgdrawsdiscord.command.VerifyCommand;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import com.viaversion.viaversion.api.ViaManager;
import com.viaversion.viaversion.api.platform.ViaInjector;
import com.viaversion.viaversion.api.platform.ViaPlatform;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.chase.ChaseServer;

public class DPGdrawsDiscord implements ModInitializer {

	@Override
	public void onInitialize() {

		Thread conthread = new DiscordHandler();
		conthread.start();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> VerifyCommand.register(dispatcher));
	}

}
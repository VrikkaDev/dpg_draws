import os
from dotenv import load_dotenv

import MinecraftCog

import discord
from discord.ext import commands

from src import LinkingHandler
from src.Packets.Packet import ConnectPacket
from src.Utils import ConfigUtils, MessageUtils
from src.Utils.NetworkUtils import send_packet, Packet

if __name__ == '__main__':

    load_dotenv(dotenv_path=".env")
    TOKEN = os.getenv('DISCORD_TOKEN')

    intent = discord.Intents.all()

    bot = commands.Bot(
        command_prefix='/',
        intents=intent
    )

    # tree = bot.tree

    ConfigUtils.Init(bot)


    @bot.event
    async def on_ready():
        LinkingHandler.Init()

        # LinkingHandler.linking_handler.Add_profile(2319873129, "disc_name", "mc_name", "datasolike teamnstuff")
        # LinkingHandler.linking_handler.Add_profile(1231238032, "lolzika", "nolol", "hehhhehehehe")
        # LinkingHandler.linking_handler.Add_profile(331888510363631639, "vrikkaduck", "vrikkaduck", "{}")

        await bot.add_cog(MinecraftCog.MinecraftCog(bot))
        minecraftCog: MinecraftCog.MinecraftCog = bot.get_cog("MinecraftCog")
        await minecraftCog.GenMessages()

        # s = await tree.sync(guild=discord.Object(id=ConfigUtils.DISCORD_GUILD))

        ac = [ConfigUtils.bot_activity_text, ConfigUtils.bot_activity_type]
        activity = discord.Activity(name=ac[0], type=discord.ActivityType.playing)

        await bot.change_presence(status=discord.Status.online, activity=activity)

        print(f'{bot.user} is connected and running')

    bot.run(TOKEN)

import os
from dotenv import load_dotenv

import MinecraftCog

import discord
from discord.ext import commands

import LinkingHandler
from Packets.Packet import ConnectPacket
from Utils import ConfigUtils, MessageUtils
from Utils.NetworkUtils import send_packet, Packet

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

        await bot.add_cog(MinecraftCog.MinecraftCog(bot))
        minecraftCog: MinecraftCog.MinecraftCog = bot.get_cog("MinecraftCog")
        await minecraftCog.GenMessages()

        # s = await tree.sync(guild=discord.Object(id=ConfigUtils.DISCORD_GUILD))

        ac = [ConfigUtils.bot_activity_text, ConfigUtils.bot_activity_type]
        activity = discord.Activity(name=ac[0], type=discord.ActivityType(value=int(ac[1])))

        await bot.change_presence(status=discord.Status.online, activity=activity)

        print(f'{bot.user} is connected and running')

    bot.run(TOKEN)

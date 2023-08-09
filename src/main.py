import os
import sys

from dotenv import load_dotenv

import MinecraftCog

import discord
from discord.ext import commands

import LinkingHandler
from Packets.Packet import ConnectPacket
from Utils import ConfigUtils, MessageUtils
from Utils.NetworkUtils import send_packet, Packet

import logging

if __name__ == '__main__':

    try:
        open('latest.log', 'w').close()
    except FileExistsError as e:
        logging.info(e)

    logging.basicConfig(filename='latest.log', format='%(asctime)s - %(name)s - %(levelname)s - %(message)s', datefmt='%m/%d/%Y %I:%M:%S %p',
                        level=logging.NOTSET, force=True)

    logging.getLogger().addHandler(logging.StreamHandler(sys.stdout))

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

        logging.info(f'{bot.user} is connected and running')


    bot.run(TOKEN)

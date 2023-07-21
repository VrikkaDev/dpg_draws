import asyncio
import json
import time
from abc import ABC, abstractmethod

import discord.ext.commands

from src import LinkingHandler
from src.Utils import ConfigUtils
from src.Utils.FileUtils import json_to_dict
import src.Utils.ConfigUtils


class Packet:
    def __init__(self, itype: str, idata: str):
        self.type = itype
        self.data = idata

    def ToJson(self):
        result = {'type': self.type, 'data': self.data}
        return json.dumps(result)

    @abstractmethod
    def ProcessResponse(self, response: str):
        pass


class ConnectPacket(Packet):
    def __init__(self):
        a = json_to_dict("text_configs.json")["to_mc"]
        super().__init__("connect", a)

    def ProcessResponse(self, response: str):
        if "false" in response:
            return False
        return True


class KeepAlivePacket(Packet):
    def __init__(self):
        super().__init__("keep_alive", {"isalive": "true"})

    def ProcessResponse(self, response: str):
        if len(response) < 5:
            return True

        rj = json.loads(response)

        if "verify_linking" in rj:
            bot: discord.ext.commands.Bot = ConfigUtils.bot
            cog = bot.get_cog("MinecraftCog")  # This isnt good way but im in hurry
            cog.send_packet(VerifyLinkingPacket())

        return True


class LinkAccountPacket(Packet):
    def __init__(self, message: discord.Message):
        self.message = message
        a = {"discord_id": message.author.id, "discord_name": message.author.name, "minecraft_name": message.content}
        super().__init__("link_account", a)

    def ProcessResponse(self, response: str):
        responseJson = json.loads(response)

        bot: discord.ext.commands.Bot = src.Utils.ConfigUtils.bot

        if not responseJson['success']:
            return True

        replyText: str = json_to_dict("text_configs.json")["dc"]["responses"][responseJson['data']['response']]
        replyText = replyText.format(mc_name=self.message.content)

        asyncio.run_coroutine_threadsafe(self.message.reply(replyText), bot.loop)

        return True


class VerifyLinkingPacket(Packet):
    def __init__(self):
        super().__init__("verify_linking", {"verify" : "true"})

    def ProcessResponse(self, response: str):
        if len(response) < 5:
            return True

        js: dict = json.loads(response)

        LinkingHandler.linking_handler.Add_profile(int(js["discord_id"]), js["discord_name"], js["minecraft_name"], "{}")

        replyText: str = json_to_dict("text_configs.json")["dc"]["responses"]["account_linked"]
        replyText = replyText.format(discord_id=js["discord_id"], mc_name=js["minecraft_name"])

        bot: discord.ext.commands.Bot = ConfigUtils.bot

        asyncio.run_coroutine_threadsafe(bot.get_channel(int(ConfigUtils.link_mc_channel)).send(content=replyText), bot.loop)

        return True


class PlayerDataPacket(Packet):
    def __init__(self):
        data = LinkingHandler.linking_handler.Get_full_dict()
        super().__init__("player_data", data)

    def ProcessResponse(self, response: str):
        return True

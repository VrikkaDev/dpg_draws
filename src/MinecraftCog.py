import threading

import discord
from discord.ext import commands, tasks

from Packets.Packet import *
from Utils import ConfigUtils, MessageUtils, FileUtils, NetworkUtils
from Utils.NetworkUtils import send_packet, _keep_alive_thread_
import LinkingHandler


class MinecraftCog(commands.Cog):

    def __init__(self, bot: commands.Bot):
        self.bot = bot
        _kaThread_ = threading.Thread(target=_keep_alive_thread_)
        _kaThread_.start()

    async def GenMessages(self):
        btn_channel: discord.TextChannel = self.bot.get_channel(int(ConfigUtils.team_select_channel))
        link_channel: discord.TextChannel = self.bot.get_channel(int(ConfigUtils.link_mc_channel))

        await MessageUtils.del_all_messages_on_channel(btn_channel, whitelist=[self.bot.user.id])
        await MessageUtils.del_all_messages_on_channel(link_channel)

        d = FileUtils.json_to_dict("text_configs.json")

        lt = MessageUtils.GenerateMessage(d["dc"]["link_account_text"])
        await link_channel.send(content=lt[0], embed=lt[1])

        vi: discord.ui.View = discord.ui.View()

        btns: dict = d["dc"]["buttons"]

        for bt in btns.keys():
            vi.add_item(item=discord.ui.Button(label=btns[bt]['button_text'], custom_id="team" + bt,
                                               style=discord.ButtonStyle[btns[bt]['button_color']]))

        lat = MessageUtils.GenerateMessage(d["dc"]["select_team_text"])

        await btn_channel.send(content=lat[0], embed=lat[1], view=vi)

    def send_packet(self, packet):
        NetworkUtils.send_packet(packet)

    def get_linking_handler(self):
        return LinkingHandler.linking_handler

    def SetTeam(self, user: discord.User, team: str) -> bool:
        LinkingHandler.linking_handler.Set_team(user.id, team)
        return True

    @commands.Cog.listener()
    async def on_message(self, message: discord.Message):
        if len(message.content) > 16:
            return
        if message.author.bot:
            return
        if int(message.channel.id) == int(ConfigUtils.link_mc_channel):

            if not NetworkUtils.isAlive:
                await message.reply(
                    content=FileUtils.json_to_dict("text_configs.json")["dc"]["responses"]["cant_connect_to_mc"])
                return

            send_packet(LinkAccountPacket(message))

    @commands.Cog.listener()
    async def on_interaction(self, interaction: discord.Interaction):
        if interaction.type == discord.InteractionType.component:
            cus_id = interaction.data.get('custom_id')

            js = FileUtils.json_to_dict("text_configs.json")
            buttons: dict = js["dc"]["buttons"]

            tex: discord.ui.TextInput = discord.ui.TextInput(label="lolzika", style=discord.TextStyle.short,
                                                             placeholder="yes?", default="yes/no", required=True)

            for tea in buttons.keys():
                if cus_id == "team" + tea:

                    if not LinkingHandler.linking_handler.Has_profile(interaction.user.id):
                        rest: str = js["dc"]["responses"]["not_linked"]
                        lc: discord.TextChannel = self.bot.get_channel(int(ConfigUtils.link_mc_channel))
                        await interaction.response.send_message(content=rest.format(link_channel=lc.mention),
                                                                ephemeral=True)
                        return

                    self.SetTeam(interaction.user, tea)
                    ct: str = js["dc"]["responses"]["added_to_team"]
                    await interaction.response.send_message(content=ct.format(team_name=tea), ephemeral=True)

                    break
        return

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

        await MessageUtils.del_all_messages_on_channel(btn_channel, whitelist=[self.bot.user.id])

        d = FileUtils.json_to_dict("text_configs.json")

        svi: discord.ui.View = discord.ui.View()
        pvi: discord.ui.View = discord.ui.View()

        specbtns: dict = d["dc"]["buttons"]["speciality"]
        pvpbtns: dict = d["dc"]["buttons"]["pvp_activeness"]

        for bt in specbtns.keys():
            svi.add_item(item=discord.ui.Button(label=specbtns[bt]['button_text'], custom_id="spec_" + bt,
                                                style=discord.ButtonStyle[specbtns[bt]['button_color']]))
        for bt in pvpbtns.keys():
            pvi.add_item(item=discord.ui.Button(label=pvpbtns[bt]['button_text'], custom_id="pvp_" + bt,
                                                style=discord.ButtonStyle[pvpbtns[bt]['button_color']]))

        slat = MessageUtils.GenerateMessage(d["dc"]["select_spec_text"])
        plat = MessageUtils.GenerateMessage(d["dc"]["select_pvp_text"])

        await btn_channel.send(content=slat[0], embed=slat[1], view=svi)
        await btn_channel.send(content=plat[0], embed=plat[1], view=pvi)

    def send_packet(self, packet):
        NetworkUtils.send_packet(packet)

    def get_linking_handler(self):
        return LinkingHandler.linking_handler

    def SetTeam(self, user: discord.User, team: str, c_id: str) -> bool:
        if "spec_" in c_id:
            LinkingHandler.linking_handler.Set_spec(user.id, team)
            return True
        LinkingHandler.linking_handler.Set_pvp(user.id, team)

    @commands.Cog.listener()
    async def on_interaction(self, interaction: discord.Interaction):
        if interaction.type == discord.InteractionType.component:
            cus_id = interaction.data.get('custom_id')

            js = FileUtils.json_to_dict("text_configs.json")
            buttons: dict = js["dc"]["buttons"]["speciality"]

            pvb: dict = js["dc"]["buttons"]["pvp_activeness"]
            buttons.update(pvb)

            for tea in buttons.keys():
                if cus_id == "spec_" + tea or cus_id == "pvp_" + tea:

                    if not LinkingHandler.linking_handler.Has_profile(interaction.user.id):
                        await interaction.response.send_modal(InputModal())
                        return

                    self.SetTeam(interaction.user, tea, cus_id)
                    ct: str = js["dc"]["responses"]["added_to_team"]
                    await interaction.response.send_message(content=ct.format(team_name=tea), ephemeral=True)

                    break
        return


class InputModal(discord.ui.Modal, title="Account linking"):
    answer: discord.ui.TextInput = discord.ui.TextInput(label="Minecraft account", style=discord.TextStyle.short,
                                                        placeholder="minecraft username", required=True,
                                                        max_length=16)

    async def on_submit(self, interaction: discord.Interaction):
        embb: discord.Embed = discord.Embed()
        embb.description = f"**{self.answer.label}**\n{self.answer}"
        embb.set_author(name=interaction.user, icon_url=interaction.user.avatar)

        if not NetworkUtils.isAlive:
            await interaction.response.send_message(
                content=FileUtils.json_to_dict("text_configs.json")["dc"]["responses"]["cant_connect_to_mc"],
                ephemeral=True)
            return

        NetworkUtils.send_packet(LinkAccountPacket(interaction, self.answer.value))

import json
import logging

import discord.ext.commands

from Packets.Packet import PlayerDataPacket
from Utils import FileUtils, ConfigUtils, NetworkUtils


class Profile:
    discord_id: str
    discord_name: str
    minecraft_name: str
    data: dict

    def __init__(self, discord_id: str, discord_name: str, minecraft_name: str, data: dict):
        self.discord_name = discord_name
        self.minecraft_name = minecraft_name
        self.data = data
        self.discord_id = discord_id

    def ToDict(self) -> dict:
        return {"discord_id": self.discord_id, "discord_name": self.discord_name, "minecraft_name": self.minecraft_name, "data": self.data}


class Linking_Handler:
    profiles: dict = {}

    def __init__(self):

        fj: dict = FileUtils.json_to_dict("player_data.json")

        for dn in fj.keys():
            prof = Profile(dn, fj[dn]['discord_name'], fj[dn]['minecraft_name'], fj[dn]['data'])
            self.profiles[dn] = prof

        return

    def _save_to_file_(self):
        fulljson = self.Get_full_json()
        FileUtils.save_to_file(r"player_data.json", fulljson)
        NetworkUtils.send_packet(PlayerDataPacket())

    def Get_full_json(self) -> str:
        r = {}
        for p in self.profiles.keys():
            r[p] = self.profiles[p].ToDict()

        return json.dumps(r, indent=4)

    def Get_full_dict(self) -> dict:
        d: dict = dict()
        for p in self.profiles.keys():
            v = self.profiles[p].ToDict()
            d[p] = v

        return d

    def Set_spec(self, discord_id: int, team: str):
        prof = self.Get_profile(discord_id)
        prof.data["spec"] = team
        self.Set_profile(prof)

    def Set_pvp(self, discord_id: int, pvp: str):
        prof = self.Get_profile(discord_id)
        prof.data["pvp"] = pvp
        self.Set_profile(prof)

    def Has_profile(self, discord_id: int) -> bool:
        return str(discord_id) in self.profiles.keys()

    def Remove_profile(self, discord_id: int):
        if self.Has_profile(discord_id):
            self.profiles.pop(discord_id)
            self._save_to_file_()

    def Get_profile(self, discord_id: int) -> Profile:

        if not self.Has_profile(discord_id):
            logging.info("No profile with id " + str(discord_id) + " was found")
            return None

        return self.profiles[str(discord_id)]

    def Add_profile(self, discord_id: int, discord_name: str, minecraft_name: str, data: str):

        if self.Has_profile(discord_id):
            return

        prof = Profile(str(discord_id), discord_name, minecraft_name, data)
        self.profiles[str(discord_id)] = prof
        self._save_to_file_()
        return

    def Set_profile(self, profile: Profile):
        self.profiles.pop(profile.discord_id)
        self.profiles[profile.discord_id] = profile
        self._save_to_file_()


global linking_handler


def Init():
    global linking_handler
    linking_handler = Linking_Handler()
    return

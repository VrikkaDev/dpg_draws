import os
from pathlib import Path

global PUBLIC_KEY, DISCORD_GUILD, APP_ID
global bot_activity_type, bot_activity_text

global link_mc_channel, team_select_channel

global bot


def Init(ibot):
    global PUBLIC_KEY, DISCORD_GUILD, APP_ID
    global bot_activity_type, bot_activity_text
    global link_mc_channel, team_select_channel
    global bot

    PUBLIC_KEY = os.getenv('PUBLIC_KEY')
    DISCORD_GUILD = os.getenv('DISCORD_GUILD')
    APP_ID = os.getenv('APP_ID')

    bot_activity_type = os.getenv('bot_activity_type')
    bot_activity_text = os.getenv('bot_activity_text')

    link_mc_channel = os.getenv('link_minecraft_channel')
    team_select_channel = os.getenv('team_select_channel')

    bot = ibot


def GetProjectRoot() -> Path:
    return Path(__file__).parent.parent.parent  # :)

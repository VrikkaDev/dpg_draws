# DPGDraws Bot

## Installation

1. Clone or download the repository to your local machine.
2. Make sure you have Python 3.x installed on your system.
3. Open a terminal or command prompt in the directory where the bot files are located.
4. Run the `run.bat` file to automatically install the required Python packages and start the bot.

**Note:** If you encounter any issues during the installation process, ensure that Python and pip are correctly set up on your system and added to the system PATH.

## Bot Settings

Before running the bot, you need to set up some configuration parameters in the `.env` file. Follow the steps below to configure the bot:

1. Open the `.env` file in a text editor.
2. Update the following parameters with your own Discord bot token, Discord guild (server) ID, and other settings:

   - `APP_ID`: Replace this with your Discord application ID. You can find your application ID on the Discord Developer Portal after creating a new application.
   - `DISCORD_GUILD`: Replace this with the ID of your Discord guild (server) where the bot will operate. You can get the server ID by enabling Developer Mode in Discord and right-clicking on the server name to copy its ID.
   - `DISCORD_TOKEN`: Replace this with your Discord bot token. You can obtain a bot token by creating a new bot application on the Discord Developer Portal.
   - `PUBLIC_KEY`: Replace this with the public key of your Discord bot. The public key is used for interactions with Discord's Interaction API.
   - `bot_activity_type`: The type of bot activity (1 for playing, 2 for streaming, 3 for listening, 4 for watching).
   - `bot_activity_text`: The text that will be displayed as the bot's activity status (e.g., "Minecraft").
   - `team_select_channel`: The ID of the Discord channel where players can select their teams. Replace this with the channel ID where you want team selection buttons to be displayed.

3. Save the `.env` file after making the changes.

## Text Configurations (text_configs.json)

The bot uses a configuration file named `text_configs.json` to manage text messages, button labels, and responses for various interactions. You can customize this file to change the bot's behavior and messages. Below is an explanation of the key elements in the `text_configs.json` file:

```json
{
  "to_mc": {
    "mc_player_verification": "Do you want to link discord user %s to this minecraft account?",
    "mc_link_account_button_yes": "Links your minecraft account to your discord account.",
    "mc_link_account_button_no": "Doesn't link this minecraft account to said discord account",
    "mc_link_account_yes_command": "Message sent to link the accounts.",
    "mc_link_account_no_command": "Didn't link the accounts.",

    "teams": {
      "pickaxe": {
        "suffix": {
          "text": "⛏",
          "color": "DARK_GRAY"
        },
        "name_color": "RED"
      },
      "sword": {
        "suffix": {
          "text": "🗡️",
          "color": "BLUE"
        },
        "name_color": "RED"
      },
      "bow": {
        "suffix": {
          "text": "🏹",
          "color": "DARK_RED"
        },
        "name_color": "RED"
      }
    }
  },

  "dc": {

    "responses": {
      "player_not_in_server": "Player named {mc_name} was not found. Make sure you are connected to the minecraft server when trying to link accounts.",
      "message_sent_to_player": "Message asking to link has been sent to {mc_name}. Please accept the verification in minecraft.",
      "account_linked": "Account <@{discord_id}> successfully linked to {mc_name}",

      "cant_connect_to_mc": "Couldn't connect to the minecraft server",

      "added_to_team": "You have been added to team {team_name}"
    },
    "select_team_text": {
      "text": "Select one of these to join a team.",
      "embed": {
        "title": "",
        "link": "",
        "color": "0xFF5733",
        "embedded_description": ""
      }
    },

    "buttons": {
      "pickaxe": {
        "button_text": "⛏",
        "button_color": "blurple"
      },
      "sword": {
        "button_text": "🗡️",
        "button_color": "red"
      },
      "bow": {
        "button_text": "🏹",
        "button_color": "green"
      }
    }
  }
}
```
    mc_player_verification: The message template for verifying the linking of a Discord user to a Minecraft account. Use %s as a placeholder for the Discord username.

    mc_link_account_button_yes: The label for the "Yes" button to link the Minecraft account.

    mc_link_account_button_no: The label for the "No" button to cancel the linking process.

    mc_link_account_yes_command: The response message when the linking message is sent to the player.

    mc_link_account_no_command: The response message when the linking is not successful.

    teams: Contains configurations for different teams. You can customize the suffix, name color, and other properties for each team.

    responses: Contains response messages for various actions, such as linking, team joining, and errors.

    select_team_text: The message template for instructing users to select a team.

    buttons: Contains configurations for emoji buttons used for team selection. You can customize the emoji and button color for each team.

Make sure to save the text_configs.json file after making changes.

### How to Use

Once the bot is up and running, it will be online and ready to handle commands and interactions in the configured Discord server.

Linking Minecraft Account:

    To link your Minecraft account to Discord, try joining a team, then write your Minecraft account name in the textbox while being logged in to the Minecraft server.

Selecting a Team:

    In the team selection channel, click on the corresponding emoji button to join a team.

#### Bot Commands:

The bot supports various commands for managing teams and accounts. Use the / prefix followed by the command name to trigger the commands.

    /verify [true|false] [discord_name]: Used to verify linking the Discord account to the provided Minecraft name.
   

## Important Note:

Please ensure that the bot has sufficient permissions in the Discord server to read and send messages in the designated channels.

This Discord bot was created by **VrikkaDev**, to **DPG_Draws**.

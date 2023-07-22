import discord


async def del_all_messages_on_channel(channel: discord.TextChannel, exclude_ids: [int] = None, whitelist: [int] = None):
    messages: list() = []
    async for msg in channel.history(limit=100):
        if exclude_ids is None and whitelist is None:
            messages.append(msg)
            continue
        if exclude_ids is not None:
            if msg.author.id in exclude_ids:
                continue
            messages.append(msg)
        if msg.author.id in whitelist:
            messages.append(msg)
    if not messages:
        return
    await channel.delete_messages(messages)


def GenerateMessage(jsondict: dict) -> [str, discord.Embed]:
    r = ""
    e: discord.Embed = discord.Embed()

    if 'text' in jsondict.keys():
        r = jsondict['text']
    if 'embed' not in jsondict.keys():
        return [r, None]

    ev: dict = jsondict['embed']

    if 'title' not in ev.keys():
        return [r, None]
    if len(ev['title']) < 1:
        return [r, None]

    e.title = ev['title']

    if 'link' in ev.keys():
        e.url = ev['link']

    if 'color' in ev.keys():
        c: str = ev['color']
        if len(c) < 1:
            e.colour = discord.Color.blue()
        else:
            e.colour = discord.Color(int(c, 16))

    if 'embedded_description' in ev.keys():
        e.description = ev['embedded_description']

    return [r, e]

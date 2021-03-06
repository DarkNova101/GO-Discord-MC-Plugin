package me.nhall.nutil.command.discord;

import me.nhall.nutil.NUtil;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class DiscordWhitelistCmd extends ListenerAdapter {

    private final NUtil plugin;

    public DiscordWhitelistCmd(NUtil plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (!event.isFromGuild()) return;

        if (!event.getName().equals("whitelist")) return;
        event.deferReply().queue();
        Role role = event.getGuild().getRoleById(plugin.getConfig().getString("role"));
        if (!event.getMember().getRoles().contains(role)) {
            event.getHook().sendMessage("You do not have the required permissions.").queue();
            return;
        }

        String username = event.getOption("username").getAsString();

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "whitelist add " + username);
        });
        event.getHook().sendMessage("Added " + username + " to the server whitelist.").queue();
    }
}


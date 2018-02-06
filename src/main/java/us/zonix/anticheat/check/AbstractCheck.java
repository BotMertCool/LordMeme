package us.zonix.anticheat.check;

import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import us.zonix.anticheat.event.player.*;
import us.zonix.core.profile.Profile;
import us.zonix.core.rank.Rank;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractCheck<T> implements ICheck<T>
{
    protected final LordMeme plugin;
    protected final PlayerData playerData;
    private final Class<T> clazz;
    private final String name;
    
    @Override
    public Class<? extends T> getType() {
        return (Class<? extends T>)this.clazz;
    }
    
    protected double getVl() {
        return this.playerData.getCheckVl(this);
    }
    
    protected void setVl(final double vl) {
        this.playerData.setCheckVl(vl, this);
    }
    
    protected boolean alert(final PlayerAlertEvent.AlertType alertType, final Player player, final String message, final boolean violation) {

        Profile profile = Profile.getByUuidIfAvailable(player.getUniqueId());

        if(profile != null && (profile.getRank() == Rank.PARTNER || profile.getRank() == Rank.MEDIA_OWNER || profile.getRank() == Rank.MEDIA_ADMIN || profile.getRank() == Rank.DEVELOPER || profile.getRank() == Rank.OWNER)) {
            return false;
        }

        final String check = this.name + ((alertType != PlayerAlertEvent.AlertType.RELEASE) ? (" (" + Character.toUpperCase(alertType.name().toLowerCase().charAt(0)) + alertType.name().toLowerCase().substring(1) + ")") : "") + ". ";
        final PlayerAlertEvent event = new PlayerAlertEvent(alertType, player, ChatColor.YELLOW + "was caught using " + ChatColor.GOLD + check);
        this.plugin.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            if (violation) {
                this.playerData.addViolation(this);
            }
            return true;
        }
        return false;
    }
    
    protected boolean ban(final Player player) {

        Profile profile = Profile.getByUuidIfAvailable(player.getUniqueId());

        if(profile != null && (profile.getRank() == Rank.PARTNER || profile.getRank() == Rank.MEDIA_OWNER || profile.getRank() == Rank.DEVELOPER || profile.getRank() == Rank.OWNER)) {
            return false;
        }

        int random = (int) (Math.random() * 100);

        if(random <= 5) {
            this.banWave(player, "Ban Wave");
            return true;
        }

        this.playerData.setBanning(true);
        final PlayerBanEvent event = new PlayerBanEvent(player, this.name);
        this.plugin.getServer().getPluginManager().callEvent((Event)event);
        return !event.isCancelled();
    }
    
    protected void randomBan(final Player player, final double rate) {

        Profile profile = Profile.getByUuidIfAvailable(player.getUniqueId());

        if(profile != null && (profile.getRank() == Rank.PARTNER || profile.getRank() == Rank.MEDIA_OWNER || profile.getRank() == Rank.DEVELOPER || profile.getRank() == Rank.OWNER)) {
            return;
        }

        this.playerData.setRandomBanRate(rate);
        this.playerData.setRandomBanReason(this.name);
        this.playerData.setRandomBan(true);
        final PlayerAlertEvent event = new PlayerAlertEvent(PlayerAlertEvent.AlertType.RELEASE, player, ChatColor.YELLOW + "was banned for " + ChatColor.GOLD + this.name + ".");
        this.plugin.getServer().getPluginManager().callEvent((Event)event);
    }

    protected boolean banWave(final Player player, final String reason) {
        this.playerData.setBanWave(true);
        final PlayerBanWaveEvent event = new PlayerBanWaveEvent(player, reason);
        this.plugin.getServer().getPluginManager().callEvent((Event)event);
        return !event.isCancelled();
    }
    
    public LordMeme getPlugin() {
        return this.plugin;
    }
    
    public PlayerData getPlayerData() {
        return this.playerData;
    }
    
    public Class<T> getClazz() {
        return this.clazz;
    }
    
    public String getName() {
        return this.name;
    }
    
    public AbstractCheck(final LordMeme plugin, final PlayerData playerData, final Class<T> clazz, final String name) {
        this.plugin = plugin;
        this.playerData = playerData;
        this.clazz = clazz;
        this.name = name;
    }
}

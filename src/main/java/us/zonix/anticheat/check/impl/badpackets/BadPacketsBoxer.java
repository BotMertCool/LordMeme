package us.zonix.anticheat.check.impl.badpackets;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.check.checks.PacketCheck;
import us.zonix.anticheat.data.PlayerData;
import us.zonix.anticheat.event.player.PlayerAlertEvent;

public class BadPacketsBoxer extends PacketCheck {

    private boolean banned = false;

    public BadPacketsBoxer(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Packets (Boxer)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation) {
            this.playerData.incrementBoxerCount();

            if (this.playerData.getBoxerCount() >= 750 && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "", false) && !this.banned) {
                this.banned = true;
                player.kickPlayer(ChatColor.RED + "You have been kicked for spamming packets.");
                this.ban(player);
            }
        }
    }

}

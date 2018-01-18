package us.zonix.anticheat.check.impl.autoclicker;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import net.minecraft.server.v1_8_R3.*;
import us.zonix.anticheat.event.player.*;

public class AutoClickerA extends PacketCheck
{
    private int swings;
    private int movements;
    
    public AutoClickerA(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Auto-Clicker (Check 1)");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInArmAnimation && !this.playerData.isDigging() && !this.playerData.isPlacing() && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && this.playerData.getLastMovePacket() != null && System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L) {
            ++this.swings;
        }
        else if (packet instanceof PacketPlayInFlying && ++this.movements == 20) {
            if (this.swings > 20 && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, "C " + this.swings + ".", true)) {
                final int violations = this.playerData.getViolations(this, 60000L);
                if (!this.playerData.isBanning() && violations > 3 && !this.playerData.isBanWave()) {
                    this.ban(player);
                }
            }
            this.playerData.setLastCps(this.swings);
            final boolean b = false;
            this.movements = (b ? 1 : 0);
            this.swings = (b ? 1 : 0);
        }
    }
}

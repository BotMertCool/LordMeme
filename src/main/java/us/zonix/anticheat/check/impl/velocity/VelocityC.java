package us.zonix.anticheat.check.impl.velocity;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.util.update.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class VelocityC extends PositionCheck
{
    public VelocityC(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Velocity (Check 3)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        final double offsetY = update.getTo().getY() - update.getFrom().getY();
        final double offsetH = Math.hypot(update.getTo().getX() - update.getFrom().getX(), update.getTo().getZ() - update.getFrom().getZ());
        final double velocityH = Math.hypot(this.playerData.getVelocityX(), this.playerData.getVelocityZ());
        final EntityPlayer entityPlayer = ((CraftPlayer)update.getPlayer()).getHandle();
        if (this.playerData.getVelocityY() > 0.0 && this.playerData.isWasOnGround() && !this.playerData.isUnderBlock() && !this.playerData.isWasUnderBlock() && !this.playerData.isInLiquid() && !this.playerData.isWasInLiquid() && !this.playerData.isInWeb() && !this.playerData.isWasInWeb() && update.getFrom().getY() % 1.0 == 0.0 && offsetY > 0.0 && offsetY < 0.41999998688697815 && velocityH > 0.45 && !entityPlayer.world.c(entityPlayer.getBoundingBox().grow(1.0, 0.0, 1.0))) {
            final double ratio = offsetH / velocityH;
            double vl = this.getVl();
            if (ratio < 0.62) {
                if ((vl += 1.1) >= 8.0 && this.alert(PlayerAlertEvent.AlertType.RELEASE, player, String.format("P %s. VL %.2f.", Math.round(ratio * 100.0), vl), false) && !this.playerData.isBanning() && vl >= 20.0 && !this.playerData.isBanWave()) {
                    this.ban(player);
                }
            }
            else {
                vl -= 0.4;
            }
            this.setVl(vl);
        }
    }
}

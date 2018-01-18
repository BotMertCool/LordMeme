package us.zonix.anticheat.check.impl.aimassist;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.util.update.*;
import us.zonix.anticheat.util.*;
import us.zonix.anticheat.event.player.*;

public class AimAssistD extends RotationCheck
{
    private float lastYawRate;
    private float lastPitchRate;
    
    public AimAssistD(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Aim (Check 4)");
    }
    
    @Override
    public void handleCheck(final Player player, final RotationUpdate update) {
        if (System.currentTimeMillis() - this.playerData.getLastAttackPacket() > 10000L) {
            return;
        }
        final float diffYaw = MathUtil.getDistanceBetweenAngles(update.getTo().getYaw(), update.getFrom().getYaw());
        final float diffPitch = MathUtil.getDistanceBetweenAngles(update.getTo().getPitch(), update.getFrom().getPitch());
        final float diffPitchRate = Math.abs(this.lastPitchRate - diffPitch);
        final float diffYawRate = Math.abs(this.lastYawRate - diffYaw);
        final float diffPitchRatePitch = Math.abs(diffPitchRate - diffPitch);
        final float diffYawRateYaw = Math.abs(diffYawRate - diffYaw);
        if (diffPitch < 0.009 && diffPitch > 0.001 && diffPitchRate > 1.0 && diffYawRate > 1.0 && diffYaw > 3.0 && this.lastYawRate > 1.5 && (diffPitchRatePitch > 1.0f || diffYawRateYaw > 1.0f)) {
            this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, String.format("DPR %.3f. DYR %.3f. LPR %.3f. LYR %.3f. DP %.3f. DY %.2f. DPRP %.3f. DYRY %.3f.", diffPitchRate, diffYawRate, this.lastPitchRate, this.lastYawRate, diffPitch, diffYaw, diffPitchRatePitch, diffYawRateYaw), false);
        }
        this.lastYawRate = diffYaw;
        this.lastPitchRate = diffPitch;
    }
}

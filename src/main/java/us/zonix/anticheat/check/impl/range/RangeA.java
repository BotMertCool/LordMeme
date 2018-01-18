package us.zonix.anticheat.check.impl.range;

import net.minecraft.server.v1_8_R3.Entity;
import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.*;
import us.zonix.anticheat.util.*;
import us.zonix.anticheat.event.player.*;
import net.minecraft.server.v1_8_R3.*;

public class RangeA extends PacketCheck
{
    private boolean sameTick;
    
    public RangeA(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Range");
    }
    
    @Override
    public void handleCheck(final Player player, final Packet packet) {
        if (packet instanceof PacketPlayInUseEntity && !player.getGameMode().equals((Object)GameMode.CREATIVE) && System.currentTimeMillis() - this.playerData.getLastDelayedMovePacket() > 220L && this.playerData.getLastMovePacket() != null && System.currentTimeMillis() - this.playerData.getLastMovePacket().getTimestamp() < 110L && !this.sameTick) {
            final PacketPlayInUseEntity useEntity = (PacketPlayInUseEntity)packet;
            if (useEntity.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                final Entity targetEntity = useEntity.a(((CraftPlayer)player).getHandle().getWorld());
                if (targetEntity instanceof EntityPlayer) {
                    final Player target = (Player)targetEntity.getBukkitEntity();
                    final CustomLocation targetLocation = this.playerData.getLastPlayerPacket(target.getUniqueId(), MathUtil.pingFormula(this.playerData.getPing()));
                    if (targetLocation == null) {
                        return;
                    }
                    final long diff = System.currentTimeMillis() - targetLocation.getTimestamp();
                    final long estimate = MathUtil.pingFormula(this.playerData.getPing()) * 50L;
                    final long diffEstimate = diff - estimate;
                    if (diffEstimate >= 500L) {
                        return;
                    }
                    final CustomLocation playerLocation = this.playerData.getLastMovePacket();
                    final PlayerData targetData = this.plugin.getPlayerDataManager().getPlayerData(target);
                    if (targetData == null) {
                        return;
                    }
                    final double range = Math.hypot(playerLocation.getX() - targetLocation.getX(), playerLocation.getZ() - targetLocation.getZ());
                    if (range > 6.5) {
                        return;
                    }
                    double threshold = 3.3;
                    if (!targetData.isSprinting() || MathUtil.getDistanceBetweenAngles(playerLocation.getYaw(), targetLocation.getYaw()) <= 90.0) {
                        threshold = 4.0;
                    }
                    double vl = this.getVl();
                    if (range > threshold) {
                        if (++vl >= 12.5) {
                            final boolean ex = this.plugin.getRangeVl() == 0.0;
                            if (this.alert(ex ? PlayerAlertEvent.AlertType.EXPERIMENTAL : PlayerAlertEvent.AlertType.RELEASE, player, String.format("P %.1f. R %.3f. T %.2f. D %s. VL %.2f.", range - threshold + 3.0, range, threshold, diffEstimate, vl), false)) {
                                if (!this.playerData.isBanning() && vl >= this.plugin.getRangeVl() && !ex && !this.playerData.isBanWave()) {
                                    this.ban(player);
                                }
                            }
                            else {
                                vl = 0.0;
                            }
                        }
                    }
                    else if (range >= 2.0) {
                        vl -= 0.25;
                    }
                    this.setVl(vl);
                    this.sameTick = true;
                }
            }
        }
        else if (packet instanceof PacketPlayInFlying) {
            this.sameTick = false;
        }
    }
}

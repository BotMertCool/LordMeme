package us.zonix.anticheat.check.impl.phase;

import us.zonix.anticheat.check.checks.*;
import us.zonix.anticheat.*;
import us.zonix.anticheat.data.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.util.CustomLocation;
import us.zonix.anticheat.util.update.*;
import us.zonix.anticheat.event.player.*;
import org.bukkit.*;
import java.util.*;

public class PhaseA extends PositionCheck
{
    private static final List<Material> PHASE_BLOCKS;
    private static final boolean TELEPORT_ON_FAIL = false;
    private CustomLocation lastNotInBlockLocation;
    private boolean inBlock;
    private int blocksPhased;
    
    public PhaseA(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Phase (Check 1)");
        this.inBlock = false;
        this.blocksPhased = 0;
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {
        double vl = this.getVl();
        final boolean inBlock = this.inBlock;
        final Location to = update.getTo();
        try {
            if (PhaseA.PHASE_BLOCKS.contains(to.getBlock().getType())) {
                this.inBlock = false;
                return;
            }
            if (to.getBlock().getType().name().contains("FENCE") || to.getBlock().getType().name().contains("DOOR") || !to.getBlock().getType().isSolid()) {
                this.inBlock = false;
                return;
            }
            this.inBlock = true;
            final Location from = update.getFrom();
            if (inBlock && (from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ())) {
                vl += 1.0 + ++this.blocksPhased / 10.0;
                if (vl > 5.0) {
                    this.alert(PlayerAlertEvent.AlertType.DEVELOPMENT, player, String.format("BP %s. VL %.2f.", this.blocksPhased, vl), false);
                }
            }
        }
        finally {
            if (inBlock && !this.inBlock) {
                this.lastNotInBlockLocation = CustomLocation.fromBukkitLocation(to);
                this.blocksPhased = 0;
                vl -= 0.45;
            }
            this.setVl(vl);
        }
    }
    
    static {
        PHASE_BLOCKS = Arrays.asList(Material.LAVA, Material.STATIONARY_LAVA, Material.WATER, Material.STATIONARY_WATER, Material.WATER_LILY, Material.LADDER, Material.AIR, Material.ANVIL, Material.RAILS, Material.ACTIVATOR_RAIL, Material.DETECTOR_RAIL, Material.POWERED_RAIL, Material.TORCH, Material.BED, Material.BED_BLOCK, Material.BREWING_STAND, Material.BREWING_STAND_ITEM);
    }
}

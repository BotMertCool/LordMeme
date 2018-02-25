package us.zonix.anticheat.check.impl.step;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import us.zonix.anticheat.LordMeme;
import us.zonix.anticheat.check.checks.PositionCheck;
import us.zonix.anticheat.data.PlayerData;
import us.zonix.anticheat.event.player.PlayerAlertEvent;
import us.zonix.anticheat.util.BlockUtil;
import us.zonix.anticheat.util.update.PositionUpdate;

public class StepA extends PositionCheck {

    public StepA(final LordMeme plugin, final PlayerData playerData) {
        super(plugin, playerData, "Step (Check 1)");
    }
    
    @Override
    public void handleCheck(final Player player, final PositionUpdate update) {

        final double height = 0.9;
        final double difference = Math.abs(update.getTo().getY() - update.getFrom().getY());
        int violations = 0;

        if(difference > 0.0) {
            if(difference > height) {
                violations++;

                if(difference > height * 2.0) {
                    violations++;
                }
                if (difference > height * 3.0) {
                    violations++;
                }
                if (difference > height * 4.0) {
                    violations++;
                }
                if (difference > height * 5.0) {
                    violations++;;
                }
                if (difference > height * 6.0) {
                    violations++;
                }
                if (player.isOnGround()) {
                    violations += 2;
                }
            } else {
                violations--;
            }
        }

        if(violations > 2) {
            this.alert(PlayerAlertEvent.AlertType.EXPERIMENTAL, player, "", true);
        }
    }
}

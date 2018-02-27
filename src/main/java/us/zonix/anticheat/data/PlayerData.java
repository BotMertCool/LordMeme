package us.zonix.anticheat.data;

import us.zonix.anticheat.check.*;
import us.zonix.anticheat.check.impl.phase.PhaseA;
import us.zonix.anticheat.check.impl.phase.PhaseB;
import us.zonix.anticheat.check.impl.step.StepA;
import us.zonix.anticheat.check.impl.vclip.VClipA;
import us.zonix.anticheat.check.impl.vclip.VClipB;
import us.zonix.anticheat.client.*;
import us.zonix.anticheat.*;
import java.util.concurrent.*;
import java.lang.reflect.*;
import org.bukkit.plugin.*;
import java.util.*;
import org.bukkit.entity.*;
import us.zonix.anticheat.check.impl.aimassist.*;
import us.zonix.anticheat.check.impl.autoclicker.*;
import us.zonix.anticheat.check.impl.badpackets.*;
import us.zonix.anticheat.check.impl.fly.*;
import us.zonix.anticheat.check.impl.inventory.*;
import us.zonix.anticheat.check.impl.killaura.*;
import us.zonix.anticheat.check.impl.range.*;
import us.zonix.anticheat.check.impl.timer.*;
import us.zonix.anticheat.check.impl.velocity.*;
import us.zonix.anticheat.check.impl.wtap.*;
import us.zonix.anticheat.check.impl.scaffold.*;
import us.zonix.anticheat.util.CustomLocation;

public class PlayerData
{
    public static final Class<? extends ICheck>[] CHECKS;
    private static final Map<Class<? extends ICheck>, Constructor<? extends ICheck>> CONSTRUCTORS;
    private final Map<UUID, List<CustomLocation>> recentPlayerPackets;
    private final Map<ICheck, Set<Long>> checkViolationTimes;
    private final Map<Class<? extends ICheck>, ICheck> checkMap;
    private final Map<Integer, Long> keepAliveTimes;
    private final Map<ICheck, Double> checkVlMap;
    private final Set<UUID> playersWatching;
    private final Set<String> filteredPhrases;
    private final Set<String> phrasesListeningTo;
    private final Set<CustomLocation> teleportLocations;
    private Map<String, String> forgeMods;
    private StringBuilder sniffedPacketBuilder;
    private CustomLocation lastMovePacket;
    private EnumClientType client;
    private UUID lastTarget;
    private String randomBanReason;
    private double randomBanRate;
    private double misplace;
    private boolean randomBan;
    private boolean allowTeleport;
    private boolean inventoryOpen;
    private boolean setInventoryOpen;
    private boolean sendingVape;
    private boolean attackedSinceVelocity;
    private boolean underBlock;
    private boolean sprinting;
    private boolean inLiquid;
    private boolean onGround;
    private boolean sniffing;
    private boolean onStairs;
    private boolean onCarpet;
    private boolean banWave;
    private boolean placing;
    private boolean banning;
    private boolean digging;
    private boolean inWeb;
    private boolean onIce;
    private boolean wasUnderBlock;
    private boolean wasOnGround;
    private boolean wasInLiquid;
    private boolean wasInWeb;
    private double lastGroundY;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    private long lastDelayedMovePacket;
    private long lastAnimationPacket;
    private long lastAttackPacket;
    private long lastVelocity;
    private long ping;
    private int velocityH;
    private int velocityV;
    private int lastCps;
    private int movementsSinceIce;
    private int movementsSinceUnderBlock;
    
    public PlayerData(final LordMeme plugin) {
        this.recentPlayerPackets = new HashMap<UUID, List<CustomLocation>>();
        this.checkViolationTimes = new HashMap<ICheck, Set<Long>>();
        this.checkMap = new HashMap<Class<? extends ICheck>, ICheck>();
        this.keepAliveTimes = new HashMap<Integer, Long>();
        this.checkVlMap = new HashMap<ICheck, Double>();
        this.playersWatching = new HashSet<UUID>();
        this.filteredPhrases = new HashSet<String>();
        this.phrasesListeningTo = new HashSet<String>();
        this.teleportLocations = Collections.newSetFromMap(new ConcurrentHashMap<CustomLocation, Boolean>());
        this.sniffedPacketBuilder = new StringBuilder();
        this.client = EnumClientType.VANILLA;


        plugin.getServer().getScheduler().runTaskAsynchronously((Plugin)plugin, () -> {
            Iterator constructorIterator = PlayerData.CONSTRUCTORS.keySet().iterator();

            while (constructorIterator.hasNext()) {
                Class<? extends ICheck> check = (Class<? extends ICheck>) constructorIterator.next();
                Constructor<? extends ICheck> constructor = PlayerData.CONSTRUCTORS.get(check);

                try {
                    this.checkMap.put(check, constructor.newInstance(plugin, this));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    
    public <T extends ICheck> T getCheck(final Class<T> clazz) {
        return (T)this.checkMap.get(clazz);
    }
    
    public CustomLocation getLastPlayerPacket(final UUID playerUUID, final int index) {
        final List<CustomLocation> customLocations = this.recentPlayerPackets.get(playerUUID);
        if (customLocations != null && customLocations.size() > index) {
            return customLocations.get(customLocations.size() - index);
        }
        return null;
    }
    
    public void addPlayerPacket(final UUID playerUUID, final CustomLocation customLocation) {
        List<CustomLocation> customLocations = this.recentPlayerPackets.get(playerUUID);
        if (customLocations == null) {
            customLocations = new ArrayList<CustomLocation>();
        }
        if (customLocations.size() == 20) {
            customLocations.remove(0);
        }
        customLocations.add(customLocation);
        this.recentPlayerPackets.put(playerUUID, customLocations);
    }
    
    public void addTeleportLocation(final CustomLocation teleportLocation) {
        this.teleportLocations.add(teleportLocation);
    }
    
    public boolean allowTeleport(final CustomLocation teleportLocation) {
        for (final CustomLocation customLocation : this.teleportLocations) {
            final double delta = Math.pow(teleportLocation.getX() - customLocation.getX(), 2.0) + Math.pow(teleportLocation.getZ() - customLocation.getZ(), 2.0);
            if (delta <= 0.005) {
                this.teleportLocations.remove(customLocation);
                return true;
            }
        }
        return false;
    }
    
    public double getCheckVl(final ICheck check) {
        if (!this.checkVlMap.containsKey(check)) {
            this.checkVlMap.put(check, 0.0);
        }
        return this.checkVlMap.get(check);
    }
    
    public void setCheckVl(double vl, final ICheck check) {
        if (vl < 0.0) {
            vl = 0.0;
        }
        this.checkVlMap.put(check, vl);
    }
    
    public boolean isPlayerWatching(final Player player) {
        return this.playersWatching.contains(player.getUniqueId());
    }
    
    public void togglePlayerWatching(final Player player) {
        if (!this.playersWatching.remove(player.getUniqueId())) {
            this.playersWatching.add(player.getUniqueId());
        }
    }
    
    public boolean isPhraseFiltered(final String phrase) {
        return this.filteredPhrases.contains(phrase);
    }
    
    public void togglePhraseFilter(final String phrase) {
        if (!this.filteredPhrases.remove(phrase)) {
            this.filteredPhrases.add(phrase);
        }
    }
    
    public boolean isPhraseBeingListenedTo(final String phrase) {
        return this.phrasesListeningTo.contains(phrase);
    }
    
    public void toggleListeningPhrase(final String phrase) {
        if (!this.phrasesListeningTo.remove(phrase)) {
            this.phrasesListeningTo.add(phrase);
        }
    }
    
    public boolean areAnyPhrasesBeingListenedTo() {
        return this.phrasesListeningTo.size() > 0;
    }
    
    public boolean keepAliveExists(final int id) {
        return this.keepAliveTimes.containsKey(id);
    }
    
    public long getKeepAliveTime(final int id) {
        return this.keepAliveTimes.get(id);
    }
    
    public void removeKeepAliveTime(final int id) {
        this.keepAliveTimes.remove(id);
    }
    
    public void addKeepAliveTime(final int id) {
        this.keepAliveTimes.put(id, System.currentTimeMillis());
    }
    
    public int getViolations(final ICheck check, final Long time) {
        final Set<Long> timestamps = this.checkViolationTimes.get(check);
        if (timestamps != null) {
            int violations = 0;
            for (final long timestamp : timestamps) {
                if (System.currentTimeMillis() - timestamp <= time) {
                    ++violations;
                }
            }
            return violations;
        }
        return 0;
    }
    
    public void addViolation(final ICheck check) {
        Set<Long> timestamps = this.checkViolationTimes.get(check);
        if (timestamps == null) {
            timestamps = new HashSet<Long>();
        }
        timestamps.add(System.currentTimeMillis());
        this.checkViolationTimes.put(check, timestamps);
    }
    
    public Map<UUID, List<CustomLocation>> getRecentPlayerPackets() {
        return this.recentPlayerPackets;
    }
    
    public Map<ICheck, Set<Long>> getCheckViolationTimes() {
        return this.checkViolationTimes;
    }
    
    public Map<Class<? extends ICheck>, ICheck> getCheckMap() {
        return this.checkMap;
    }
    
    public Map<Integer, Long> getKeepAliveTimes() {
        return this.keepAliveTimes;
    }
    
    public Map<ICheck, Double> getCheckVlMap() {
        return this.checkVlMap;
    }
    
    public Set<UUID> getPlayersWatching() {
        return this.playersWatching;
    }
    
    public Set<String> getFilteredPhrases() {
        return this.filteredPhrases;
    }
    
    public Set<String> getPhrasesListeningTo() {
        return this.phrasesListeningTo;
    }
    
    public Set<CustomLocation> getTeleportLocations() {
        return this.teleportLocations;
    }
    
    public Map<String, String> getForgeMods() {
        return this.forgeMods;
    }
    
    public StringBuilder getSniffedPacketBuilder() {
        return this.sniffedPacketBuilder;
    }
    
    public CustomLocation getLastMovePacket() {
        return this.lastMovePacket;
    }
    
    public EnumClientType getClient() {
        return this.client;
    }
    
    public UUID getLastTarget() {
        return this.lastTarget;
    }
    
    public String getRandomBanReason() {
        return this.randomBanReason;
    }
    
    public double getRandomBanRate() {
        return this.randomBanRate;
    }
    
    public double getMisplace() {
        return this.misplace;
    }
    
    public boolean isRandomBan() {
        return this.randomBan;
    }
    
    public boolean isAllowTeleport() {
        return this.allowTeleport;
    }
    
    public boolean isInventoryOpen() {
        return this.inventoryOpen;
    }
    
    public boolean isSetInventoryOpen() {
        return this.setInventoryOpen;
    }
    
    public boolean isSendingVape() {
        return this.sendingVape;
    }
    
    public boolean isAttackedSinceVelocity() {
        return this.attackedSinceVelocity;
    }
    
    public boolean isUnderBlock() {
        return this.underBlock;
    }
    
    public boolean isSprinting() {
        return this.sprinting;
    }
    
    public boolean isInLiquid() {
        return this.inLiquid;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public boolean isSniffing() {
        return this.sniffing;
    }
    
    public boolean isOnStairs() {
        return this.onStairs;
    }
    
    public boolean isOnCarpet() {
        return this.onCarpet;
    }
    
    public boolean isBanWave() {
        return this.banWave;
    }
    
    public boolean isPlacing() {
        return this.placing;
    }
    
    public boolean isBanning() {
        return this.banning;
    }
    
    public boolean isDigging() {
        return this.digging;
    }
    
    public boolean isInWeb() {
        return this.inWeb;
    }
    
    public boolean isOnIce() {
        return this.onIce;
    }
    
    public boolean isWasUnderBlock() {
        return this.wasUnderBlock;
    }
    
    public boolean isWasOnGround() {
        return this.wasOnGround;
    }
    
    public boolean isWasInLiquid() {
        return this.wasInLiquid;
    }
    
    public boolean isWasInWeb() {
        return this.wasInWeb;
    }
    
    public double getLastGroundY() {
        return this.lastGroundY;
    }
    
    public double getVelocityX() {
        return this.velocityX;
    }
    
    public double getVelocityY() {
        return this.velocityY;
    }
    
    public double getVelocityZ() {
        return this.velocityZ;
    }
    
    public long getLastDelayedMovePacket() {
        return this.lastDelayedMovePacket;
    }
    
    public long getLastAnimationPacket() {
        return this.lastAnimationPacket;
    }
    
    public long getLastAttackPacket() {
        return this.lastAttackPacket;
    }
    
    public long getLastVelocity() {
        return this.lastVelocity;
    }
    
    public long getPing() {
        return this.ping;
    }
    
    public int getVelocityH() {
        return this.velocityH;
    }
    
    public int getVelocityV() {
        return this.velocityV;
    }
    
    public int getLastCps() {
        return this.lastCps;
    }
    
    public int getMovementsSinceIce() {
        return this.movementsSinceIce;
    }
    
    public int getMovementsSinceUnderBlock() {
        return this.movementsSinceUnderBlock;
    }
    
    public void setForgeMods(final Map<String, String> forgeMods) {
        this.forgeMods = forgeMods;
    }
    
    public void setSniffedPacketBuilder(final StringBuilder sniffedPacketBuilder) {
        this.sniffedPacketBuilder = sniffedPacketBuilder;
    }
    
    public void setLastMovePacket(final CustomLocation lastMovePacket) {
        this.lastMovePacket = lastMovePacket;
    }
    
    public void setClient(final EnumClientType client) {
        this.client = client;
    }
    
    public void setLastTarget(final UUID lastTarget) {
        this.lastTarget = lastTarget;
    }
    
    public void setRandomBanReason(final String randomBanReason) {
        this.randomBanReason = randomBanReason;
    }
    
    public void setRandomBanRate(final double randomBanRate) {
        this.randomBanRate = randomBanRate;
    }
    
    public void setMisplace(final double misplace) {
        this.misplace = misplace;
    }
    
    public void setRandomBan(final boolean randomBan) {
        this.randomBan = randomBan;
    }
    
    public void setAllowTeleport(final boolean allowTeleport) {
        this.allowTeleport = allowTeleport;
    }
    
    public void setInventoryOpen(final boolean inventoryOpen) {
        this.inventoryOpen = inventoryOpen;
    }
    
    public void setSetInventoryOpen(final boolean setInventoryOpen) {
        this.setInventoryOpen = setInventoryOpen;
    }
    
    public void setSendingVape(final boolean sendingVape) {
        this.sendingVape = sendingVape;
    }
    
    public void setAttackedSinceVelocity(final boolean attackedSinceVelocity) {
        this.attackedSinceVelocity = attackedSinceVelocity;
    }
    
    public void setUnderBlock(final boolean underBlock) {
        this.underBlock = underBlock;
    }
    
    public void setSprinting(final boolean sprinting) {
        this.sprinting = sprinting;
    }
    
    public void setInLiquid(final boolean inLiquid) {
        this.inLiquid = inLiquid;
    }
    
    public void setOnGround(final boolean onGround) {
        this.onGround = onGround;
    }
    
    public void setSniffing(final boolean sniffing) {
        this.sniffing = sniffing;
    }
    
    public void setOnStairs(final boolean onStairs) {
        this.onStairs = onStairs;
    }
    
    public void setOnCarpet(final boolean onCarpet) {
        this.onCarpet = onCarpet;
    }
    
    public void setBanWave(final boolean banWave) {
        this.banWave = banWave;
    }
    
    public void setPlacing(final boolean placing) {
        this.placing = placing;
    }
    
    public void setBanning(final boolean banning) {
        this.banning = banning;
    }
    
    public void setDigging(final boolean digging) {
        this.digging = digging;
    }
    
    public void setInWeb(final boolean inWeb) {
        this.inWeb = inWeb;
    }
    
    public void setOnIce(final boolean onIce) {
        this.onIce = onIce;
    }
    
    public void setWasUnderBlock(final boolean wasUnderBlock) {
        this.wasUnderBlock = wasUnderBlock;
    }
    
    public void setWasOnGround(final boolean wasOnGround) {
        this.wasOnGround = wasOnGround;
    }
    
    public void setWasInLiquid(final boolean wasInLiquid) {
        this.wasInLiquid = wasInLiquid;
    }
    
    public void setWasInWeb(final boolean wasInWeb) {
        this.wasInWeb = wasInWeb;
    }
    
    public void setLastGroundY(final double lastGroundY) {
        this.lastGroundY = lastGroundY;
    }
    
    public void setVelocityX(final double velocityX) {
        this.velocityX = velocityX;
    }
    
    public void setVelocityY(final double velocityY) {
        this.velocityY = velocityY;
    }
    
    public void setVelocityZ(final double velocityZ) {
        this.velocityZ = velocityZ;
    }
    
    public void setLastDelayedMovePacket(final long lastDelayedMovePacket) {
        this.lastDelayedMovePacket = lastDelayedMovePacket;
    }
    
    public void setLastAnimationPacket(final long lastAnimationPacket) {
        this.lastAnimationPacket = lastAnimationPacket;
    }
    
    public void setLastAttackPacket(final long lastAttackPacket) {
        this.lastAttackPacket = lastAttackPacket;
    }
    
    public void setLastVelocity(final long lastVelocity) {
        this.lastVelocity = lastVelocity;
    }
    
    public void setPing(final long ping) {
        this.ping = ping;
    }
    
    public void setVelocityH(final int velocityH) {
        this.velocityH = velocityH;
    }
    
    public void setVelocityV(final int velocityV) {
        this.velocityV = velocityV;
    }
    
    public void setLastCps(final int lastCps) {
        this.lastCps = lastCps;
    }
    
    public void setMovementsSinceIce(final int movementsSinceIce) {
        this.movementsSinceIce = movementsSinceIce;
    }
    
    public void setMovementsSinceUnderBlock(final int movementsSinceUnderBlock) {
        this.movementsSinceUnderBlock = movementsSinceUnderBlock;
    }
    
    static {
        CHECKS = new Class[] { AimAssistA.class, AimAssistB.class, AimAssistC.class, AimAssistD.class, AimAssistE.class, AutoClickerA.class, AutoClickerB.class, AutoClickerC.class, AutoClickerD.class, AutoClickerE.class, AutoClickerF.class, AutoClickerG.class, AutoClickerH.class, AutoClickerI.class, AutoClickerJ.class, AutoClickerK.class, AutoClickerK.class, AutoClickerL.class, BadPacketsA.class, BadPacketsB.class, BadPacketsC.class, BadPacketsD.class, BadPacketsE.class, BadPacketsF.class, BadPacketsG.class, BadPacketsH.class, BadPacketsI.class, BadPacketsJ.class, BadPacketsK.class, BadPacketsL.class, FlyA.class, FlyB.class, FlyC.class, InventoryA.class, InventoryB.class, InventoryC.class, InventoryD.class, InventoryE.class, InventoryF.class, InventoryG.class, KillAuraA.class, KillAuraB.class, KillAuraC.class, KillAuraD.class, KillAuraE.class, KillAuraF.class, KillAuraG.class, KillAuraH.class, KillAuraI.class, KillAuraJ.class, KillAuraK.class, KillAuraL.class, KillAuraM.class, KillAuraN.class, KillAuraO.class, KillAuraP.class, KillAuraQ.class, KillAuraR.class, KillAuraS.class, RangeA.class, TimerA.class, VelocityA.class, VelocityB.class, VelocityC.class, WTapA.class, WTapB.class, ScaffoldA.class, ScaffoldB.class, ScaffoldC.class/**PhaseA.class, PhaseB.class, VClipA.class, VClipB.class, StepA.class**/};
        CONSTRUCTORS = new ConcurrentHashMap<Class<? extends ICheck>, Constructor<? extends ICheck>>();
        for (final Class<? extends ICheck> check : PlayerData.CHECKS) {
            try {
                PlayerData.CONSTRUCTORS.put(check, check.getConstructor(LordMeme.class, PlayerData.class));
            }
            catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}

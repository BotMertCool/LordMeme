package us.zonix.anticheat;

import lombok.Getter;
import org.bukkit.plugin.java.*;
import us.zonix.anticheat.commands.MemeCommand;
import us.zonix.anticheat.commands.sub.*;
import us.zonix.anticheat.manager.*;
import net.minecraft.server.v1_8_R3.*;
import club.minemen.spigot.*;
import us.zonix.anticheat.handler.*;
import club.minemen.spigot.handler.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import us.zonix.anticheat.listener.*;
import us.zonix.anticheat.runnable.*;
import us.zonix.anticheat.util.command.CommandFramework;

public class LordMeme extends JavaPlugin
{
    private static LordMeme instance;
    private PlayerDataManager playerDataManager;
    private BanWaveManager banWaveManager;
    private AlertsManager alertsManager;
    private LogManager logManager;
    private double rangeVl;

    @Getter private CommandFramework framework;
    
    public LordMeme() {
        this.rangeVl = 60.0;
    }
    
    public void onEnable() {
        (LordMeme.instance = this).registerHandlers();
        this.registerManagers();
        this.registerListeners();
        this.registerCommands();
        this.registerExportLogsTimer();
    }
    
    public boolean isAntiCheatEnabled() {
        return MinecraftServer.getServer().tps1.getAverage() > 19.0 && MinecraftServer.LAST_TICK_TIME + 100L > System.currentTimeMillis();
    }
    
    private void registerHandlers() {
        ClubSpigot.INSTANCE.addPacketHandler((PacketHandler)new CustomPacketHandler(this));
        ClubSpigot.INSTANCE.addMovementHandler((MovementHandler)new CustomMovementHandler(this));

        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeListener(this));
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }
    
    private void registerManagers() {
        this.framework = new CommandFramework(this);
        this.alertsManager = new AlertsManager(this);
        this.banWaveManager = new BanWaveManager(this);
        this.playerDataManager = new PlayerDataManager(this);
        this.logManager = new LogManager();
    }
    
    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents((Listener)new PlayerListener(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new BanWaveListener(this), (Plugin)this);
        this.getServer().getPluginManager().registerEvents((Listener)new ModListListener(this), (Plugin)this);

    }
    
    private void registerCommands() {
        new MemeCommand();
        new AlertsCommand();
        new DevAlertsCommand();
        new LogsCommand();
        new ToggleCommand();
        new BanCommand();
        new RangeCommand();
        new WatchCommand();
        new ExemptCommand();
        new MisplaceCommand();
        new MemeFunCommand();
        new BanWaveCommand();
        new GangSquadCommand();
    }
    
    private void registerExportLogsTimer() {
        this.getServer().getScheduler().runTaskTimerAsynchronously((Plugin)this, (Runnable)new ExportLogs(this), 600L, 600L);
    }
    
    public PlayerDataManager getPlayerDataManager() {
        return this.playerDataManager;
    }
    
    public BanWaveManager getBanWaveManager() {
        return this.banWaveManager;
    }
    
    public AlertsManager getAlertsManager() {
        return this.alertsManager;
    }
    
    public LogManager getLogManager() {
        return this.logManager;
    }
    
    public static LordMeme getInstance() {
        return LordMeme.instance;
    }
    
    public double getRangeVl() {
        return this.rangeVl;
    }
    
    public void setRangeVl(final double rangeVl) {
        this.rangeVl = rangeVl;
    }
}

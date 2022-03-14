package pt.dioguin.lumberjack;

import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pt.dioguin.lumberjack.commands.Lumberjack;
import pt.dioguin.lumberjack.listeners.BlockBreak;
import pt.dioguin.lumberjack.listeners.PlayerInteract;
import pt.dioguin.lumberjack.listeners.PlayerJoin;
import pt.dioguin.lumberjack.listeners.PlayerQuit;
import pt.dioguin.lumberjack.utils.MongoDB;
import pt.dioguin.lumberjack.utils.NPC;
import pt.dioguin.lumberjack.utils.Serializer;

public final class Main extends JavaPlugin {

    private static Main instance;

    @Override
    public void onEnable() {

        instance = this;

        MongoDB.openConnection();

        getCommand("lumberjack").setExecutor(new Lumberjack(this));
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuit(), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreak(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteract(), this);

        saveDefaultConfig();
        if (getConfig().contains("NPC.Location"))
        NPC.createNPC(Serializer.locationDeserializer(getConfig().getString("NPC.Location")), getConfig().getString("NPC.Name").replace("&", "§"), getConfig().getString("NPC.Skin"));


    }

    @Override
    public void onDisable() {

        for (Player player : Bukkit.getOnlinePlayers()){

            for (EntityPlayer npc : NPC.getNpcs()){
                NPC.removeNPC(player, npc);
            }

        }

    }

    public static Main getInstance() {
        return instance;
    }
}

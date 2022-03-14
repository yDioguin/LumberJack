package pt.dioguin.lumberjack.commands;

import net.minecraft.server.level.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pt.dioguin.lumberjack.Main;
import pt.dioguin.lumberjack.utils.NPC;
import pt.dioguin.lumberjack.utils.Serializer;

public class Lumberjack implements CommandExecutor {

    public Main plugin;

    public Lumberjack(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String label, String[] args) {

        if (!(s instanceof Player)) return true;

        Player player = (Player) s;

        if (!player.hasPermission("lumberjack.admin")){
            player.sendMessage(plugin.getConfig().getString("No Permission").replace("&", "§"));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")){

            plugin.reloadConfig();
            player.sendMessage(plugin.getConfig().getString("Reload Config").replace("&", "§"));
            return true;
        }

        for (Player p : Bukkit.getOnlinePlayers()){

            for (EntityPlayer npc : NPC.getNpcs()){
                NPC.removeNPC(p, npc);
            }

        }

        plugin.getConfig().set("NPC.Location", Serializer.locationSerializer(player.getLocation()));
        plugin.saveConfig();
        NPC.createNPC(player.getLocation(), plugin.getConfig().getString("NPC.Name").replace("&", "§"), plugin.getConfig().getString("NPC.Skin"));
        player.sendMessage(plugin.getConfig().getString("NPC Created").replace("&", "§"));

        return false;
    }
}

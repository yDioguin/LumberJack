package pt.dioguin.lumberjack.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pt.dioguin.lumberjack.utils.MongoDB;
import pt.dioguin.lumberjack.utils.NPC;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        Player player = e.getPlayer();
        MongoDB.loadPlayer(player.getUniqueId().toString());

        if (NPC.getNpcs() == null) return;
        if (NPC.getNpcs().isEmpty()) return;
        NPC.addJoinPacket(player);

    }

}

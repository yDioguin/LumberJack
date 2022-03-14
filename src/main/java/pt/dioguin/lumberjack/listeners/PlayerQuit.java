package pt.dioguin.lumberjack.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pt.dioguin.lumberjack.objects.LumberJack;
import pt.dioguin.lumberjack.utils.MongoDB;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){

        Player player = e.getPlayer();
        LumberJack lumberjack = LumberJack.playersData.get(player.getUniqueId());
        MongoDB.savePlayer(player.getUniqueId().toString(), lumberjack.getLevelMax(), lumberjack.getLevel(), lumberjack.getXp(), lumberjack.getXpNecessary());

    }

}

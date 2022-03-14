package pt.dioguin.lumberjack.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import pt.dioguin.lumberjack.objects.LumberJack;

public class PlayerInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){

        Player player = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK){

            LumberJack lumberjack = LumberJack.playersData.get(player.getUniqueId());
            lumberjack.activeSkill(player, e.getClickedBlock());

        }

    }

}

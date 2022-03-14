package pt.dioguin.lumberjack.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import pt.dioguin.lumberjack.objects.LumberJack;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){

        Player player = e.getPlayer();
        LumberJack lumberjack = LumberJack.playersData.get(player.getUniqueId());

        lumberjack.breakWood(player, e.getBlock());

    }

}

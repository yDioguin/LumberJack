package pt.dioguin.lumberjack.objects;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import pt.dioguin.lumberjack.Main;

import java.util.*;

/**
 *
 * LumberJack class that handles all data
 *
 */

public class LumberJack {

    public static HashMap<UUID, LumberJack> playersData = new HashMap<>();
    private List<Material> materials = Arrays.asList(new Material[] {Material.ACACIA_WOOD, Material.OAK_WOOD, Material.BIRCH_WOOD,
    Material.SPRUCE_WOOD, Material.JUNGLE_WOOD});
    private List<Material> itens = Arrays.asList(new Material[] {Material.DIAMOND_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.STONE_AXE, Material.WOODEN_AXE});

    private int levelMax;
    private int level;
    private double xp;
    private double xpNecessary;
    private long cooldownStart;

    /**
     *
     * @param levelMax maximum level that the user can pick up
     * @param level user level
     * @param xp user xp
     * @param xpNecessary xp that the user needs to evolve level
     * @param cooldownStart date in milliseconds that the user used the ability
     */

    public LumberJack(int levelMax, int level, double xp, double xpNecessary, long cooldownStart) {
        this.levelMax = levelMax;
        this.level = level;
        this.xp = xp;
        this.xpNecessary = xpNecessary;
        this.cooldownStart = cooldownStart;
    }

    public int getLevelMax() {
        return levelMax;
    }

    public void setLevelMax(int levelMax) {
        this.levelMax = levelMax;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getXp() {
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public double getXpNecessary() {
        return xpNecessary;
    }

    public void setXpNecessary(double xpNecessary) {
        this.xpNecessary = xpNecessary;
    }

    public long getCooldownStart() {
        return cooldownStart;
    }

    public void setCooldownStart(long cooldownStart) {
        this.cooldownStart = cooldownStart;
    }

    /**
     *
     * Method used for when the player breaks the wood
     *
     * @param player defines the player who broke the wood
     * @param block defines the block that was broken by the player
     */

    public void breakWood(Player player, Block block){

        if (!materials.contains(block.getType())) return;
        if (!itens.contains(player.getItemInHand().getType())) return;

        double xpPerBlock = Main.getInstance().getConfig().getDouble("LumberJack.xpPerBlock");
        this.setXp(this.getXp() + xpPerBlock);
        upLevel(player);

        String message = Main.getInstance().getConfig().getString("LumberJack.ActionBarMessage").replace("&", "§").replace("{level}", "" + this.level).replace("{xp}", "" + this.xp).replace("{xpNecessary}", "" + this.xpNecessary);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    /**
     *
     * Method used for the player to evolve from n ...
     *
     * @param player defines the user who evolves from level
     */

    public void upLevel(Player player){

        if (this.getLevel() >= this.getLevelMax()) return;

        if (this.getXp() >= this.getXpNecessary()){

            double xpNecessary = Main.getInstance().getConfig().getDouble("LumberJack.xpMultiplierInitial") + (this.getLevel() * Main.getInstance().getConfig().getDouble("LumberJack.xpMultiplierPerLevel"));
            double restXP = this.getXp() - this.getXpNecessary();
            this.setXpNecessary(xpNecessary);
            this.setLevel(this.getLevel() + 1);

            player.sendMessage(Main.getInstance().getConfig().getString("LumberJack.Message").replace("&", "§").replace("{level}", "" + this.getLevel()));
            this.setXp(restXP);

        }

    }

    /**
     *
     * Method used for the player to activate the ability
     *
     * @param player defines the player who activated the ability
     * @param block sets the block the player clicked to activate the ability
     */

    public void activeSkill(Player player, Block block){

        if (!materials.contains(block.getType())) return;
        if (!itens.contains(player.getItemInHand().getType())) return;

        double cooldown = (Main.getInstance().getConfig().getDouble("LumberJack.cooldownInitial") - (this.getLevel() * Main.getInstance().getConfig().getDouble("LumberJack.cooldownPerLevel"))) * 1000;
        long difference = Calendar.getInstance().getTimeInMillis() - this.getCooldownStart();

        if (difference < cooldown) {
            player.sendMessage(Main.getInstance().getConfig().getString("LumberJack.SkillAlreadActive").replace("&", "§"));
            return;
        }

        block.setType(Material.AIR);
        this.setCooldownStart(Calendar.getInstance().getTimeInMillis());
        player.sendMessage(Main.getInstance().getConfig().getString("LumberJack.ActiveSkill").replace("&", "§"));

        for (Block b : getNearbyBlocks(block.getLocation(), Main.getInstance().getConfig().getInt("LumberJack.Radius"))){

            if (materials.contains(b.getType())){
                breakWood(player, b);
                b.setType(Material.AIR);
            }

        }

    }

    private List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

}

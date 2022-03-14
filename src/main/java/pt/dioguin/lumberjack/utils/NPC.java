package pt.dioguin.lumberjack.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.EntityHuman;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPC {

    public static List<EntityPlayer> npcs = new ArrayList<>();

    public static void createNPC(Location location, String name, String skin){

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) Bukkit.getWorld(location.getWorld().getName())).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
        EntityPlayer npc = new EntityPlayer(server, world, gameProfile);
        npc.b(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
        String[] nameSkin = getSkin(skin);
        gameProfile.getProperties().put("textures", new Property("textures", nameSkin[0], nameSkin[1]));

        addNPCPacket(npc);
        npcs.add(npc);
    }

    public static void addNPCPacket(EntityPlayer npc){

        for (Player player : Bukkit.getOnlinePlayers()){

            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc));
            connection.a(new PacketPlayOutNamedEntitySpawn(npc));
            connection.a(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.y * 256 / 360)));


        }

    }

    public static void addJoinPacket(Player player){

        for (EntityPlayer npc : npcs){

            PlayerConnection connection = ((CraftPlayer) player).getHandle().b;
            connection.a(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a, npc));
            connection.a(new PacketPlayOutNamedEntitySpawn(npc));
            connection.a(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.y * 256 / 360)));


        }

    }

    public static List<EntityPlayer> getNpcs(){
        return npcs;
    }

    public static void removeNPC(Player player, EntityPlayer npc) {
        PlayerConnection connection = (((CraftPlayer)player).getHandle()).b;
        connection.a((Packet)new PacketPlayOutEntityDestroy(new int[] { npc.getBukkitEntity().getEntityId() }));
    }

    private static String[] getSkin(String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = (new JsonParser()).parse(reader).getAsJsonObject().get("id").getAsString();
            URL url2 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader2 = new InputStreamReader(url2.openStream());
            JsonObject property = (new JsonParser()).parse(reader2).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = property.get("value").getAsString();
            String signature = property.get("signature").getAsString();
            return new String[] { texture, signature };
        } catch (Exception e) {
            return null;
        }
    }

}

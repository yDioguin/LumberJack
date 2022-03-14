package pt.dioguin.lumberjack.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class Serializer {

    public static String locationSerializer(Location paramLocation) {
        return String.valueOf(String.valueOf(paramLocation.getWorld().getName())) + "@" + paramLocation.getX() + "@" + paramLocation.getY() + "@" + paramLocation.getZ() + "@" + paramLocation.getYaw() + "@" + paramLocation.getPitch();
    }

    public static Location locationDeserializer(String paramString) {
        String[] arrayOfString = paramString.split("@");
        return new Location(Bukkit.getWorld(arrayOfString[0]), Double.parseDouble(arrayOfString[1]), Double.parseDouble(arrayOfString[2]), Double.parseDouble(arrayOfString[3]), Float.parseFloat(arrayOfString[4]), Float.parseFloat(arrayOfString[5]));
    }

}
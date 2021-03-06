package de.alpha.uhc.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.alpha.uhc.Core;
import de.alpha.uhc.Registery;
import de.popokaka.alphalibary.reflection.ReflectionUtil;

public class HoloUtil {
	
	private Core pl;
	private Registery r;

	public HoloUtil(Core c) {
		this.pl = c;
		this.r = pl.getRegistery();
	}
	
    public void createHologram(Player p, int id, double remove) {
        Location loc = r.getHologramFile().getLocation(id).subtract(0, remove, 0);

        String name = r.getHologramFile().getName(id);

        Class<?> cWorld = ReflectionUtil.getNmsClass("World");
        Class<?>[] param = {double.class, double.class, double.class, float.class, float.class};

        Constructor<?> holoConstructor = null;
        try {
            holoConstructor = ReflectionUtil.getNmsClass("EntityArmorStand").getConstructor(cWorld);
        } catch (NoSuchMethodException | SecurityException e2) {
            e2.printStackTrace();
        }
        Class<?> cholo = ReflectionUtil.getNmsClass("EntityArmorStand");
        try {

            Object hc = holoConstructor != null ? holoConstructor.newInstance(ReflectionUtil.getWorldServer(loc.getWorld())) : null;
            name = name.replace("[Player]", p.getDisplayName());
            name = name.replace("[Kills]", Integer.toString(r.getStats().getKills(p)));
            name = name.replace("[Deaths]", Integer.toString(r.getStats().getDeaths(p)));
            name = name.replace("[Coins]", Integer.toString(r.getStats().getCoins(p)));


            cholo.getMethod("setLocation", param)
                    .invoke(hc,
                            loc.getX(),
                            loc.getY(),
                            loc.getZ(),
                            0F,
                            0F);
            cholo.getMethod("setInvisible", boolean.class)
                    .invoke(hc, true);
            cholo.getMethod("setCustomName", String.class)
                    .invoke(hc, name);
            cholo.getMethod("setCustomNameVisible", boolean.class)
                    .invoke(hc, true);

            Constructor<?> conpacket = ReflectionUtil.getNmsClass("PacketPlayOutSpawnEntityLiving").getConstructor(ReflectionUtil.getNmsClass("EntityLiving"));
            Object packet = conpacket.newInstance(hc);

            ReflectionUtil.sendPacket(p, packet);

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e1) {
            e1.printStackTrace();
        }
    }

    public void showHologram(Player p) {
        for (int id = 0; id < r.getHologramFile().holocount(); id++) {

            Location loc = r.getHologramFile().getLocation(id);

            String name = r.getHologramFile().getName(id);

            Class<?> cWorld = ReflectionUtil.getNmsClass("World");
            Class<?>[] param = {double.class, double.class, double.class, float.class, float.class};

            Constructor<?> holoConstructor = null;
            try {
                holoConstructor = ReflectionUtil.getNmsClass("EntityArmorStand").getConstructor(cWorld);
            } catch (NoSuchMethodException | SecurityException e2) {
                e2.printStackTrace();
            }
            Class<?> cholo = ReflectionUtil.getNmsClass("EntityArmorStand");
            try {

                Object hc = holoConstructor != null ? holoConstructor.newInstance(ReflectionUtil.getWorldServer(loc.getWorld())) : null;
                name = name.replace("[Player]", p.getDisplayName());
                name = name.replace("[Kills]", Integer.toString(r.getStats().getKills(p)));
                name = name.replace("[Deaths]", Integer.toString(r.getStats().getDeaths(p)));
                name = name.replace("[Coins]", Integer.toString(r.getStats().getCoins(p)));

                cholo.getMethod("setLocation", param)
                        .invoke(hc,
                                loc.getX(),
                                loc.getY(),
                                loc.getZ(),
                                0F,
                                0F);
                cholo.getMethod("setInvisible", boolean.class)
                        .invoke(hc, true);
                cholo.getMethod("setCustomName", String.class)
                        .invoke(hc, name);
                cholo.getMethod("setCustomNameVisible", boolean.class)
                        .invoke(hc, true);

                Constructor<?> conpacket = ReflectionUtil.getNmsClass("PacketPlayOutSpawnEntityLiving").getConstructor(ReflectionUtil.getNmsClass("EntityLiving"));
                Object packet = conpacket.newInstance(hc);

                ReflectionUtil.sendPacket(p, packet);

            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e1) {
                e1.printStackTrace();
            }
        }
    }

}

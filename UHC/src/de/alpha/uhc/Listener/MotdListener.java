package de.alpha.uhc.Listener;

import de.alpha.uhc.Core;
import de.alpha.uhc.GState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdListener implements Listener {
	
	public MotdListener(Core c) {
	}

    private  String lobby;
    private  String grace;
    private  String ingame;
    private  String restart;
    private  boolean custommotd;

    public  void setLobby(String lobby) {
        this.lobby = lobby;
    }

    public  void setGrace(String grace) {
    	this.grace = grace;
    }

    public  void setIngame(String ingame) {
    	this.ingame = ingame;
    }

    public  void setRestart(String restart) {
    	this.restart = restart;
    }

    public  void setCustommotd(boolean custommotd) {
    	this.custommotd = custommotd;
    }

    @EventHandler
    public void onPing(ServerListPingEvent e) {

        if (!custommotd) return;

        if (GState.isState(GState.LOBBY)) {
            e.setMotd(lobby);
        } else if (GState.isState(GState.INGAME)) {
            e.setMotd(ingame);
        } else if (GState.isState(GState.RESTART)) {
            e.setMotd(restart);
        } else if (GState.isState(GState.GRACE)) {
            e.setMotd(grace);
        }

    }
}

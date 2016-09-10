package de.alphahelix.uhc.files;

import de.alphahelix.uhc.UHC;
import de.alphahelix.uhc.util.EasyFile;

public class MainOptionsFile extends EasyFile {
	
	public MainOptionsFile(UHC uhc) {
		super("Main-Options.uhc", uhc);
	}
	
	public void addValues() {
		setDefault("Prefix", "&7[&6UHC&8-&6Remake&7] ");
		setDefault("Restartmessage", "&7The Server has stopped. Now loading a new round of UHC.");
		setDefault("Bungeecord", false);
		setDefault("Bungeecord Fallbackserver", "lobby");
		setDefault("MySQL", false);
		setDefault("Soup", false);
		setDefault("Spawndispersal", 20);
		setDefault("Status MOTD", true);
		setDefault("Minimum players", 4);
		setDefault("Maximum players", 64);
	}
	
	public void loadValues() {
		getUhc().setPrefix(getColorString("Prefix"));
		getUhc().setBunggeMode(getBoolean("Bungeecord"));
		getUhc().setLobbyServer("Bungeecord Fallbackserver");
		getUhc().setMySQLMode(getBoolean("MySQL"));
		getUhc().setSoup(getBoolean("Soup"));
		getUhc().setScenarios(getBoolean("Scenarios"));
		getUhc().setSpawnradius(getInt("Spawndispersal"));
		getUhc().setStatusMOTD(getBoolean("Status MOTD"));
	}
}

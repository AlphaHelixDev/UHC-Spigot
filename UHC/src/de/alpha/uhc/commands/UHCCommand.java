package de.alpha.uhc.commands;

import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.alpha.uhc.Core;
import de.alpha.uhc.files.HologramFileManager;
import de.alpha.uhc.files.MessageFileManager;
import de.alpha.uhc.files.OptionsFileManager;
import de.alpha.uhc.files.SpawnFileManager;
import de.alpha.uhc.files.TeamFile;
import de.alpha.uhc.kits.GUI;
import de.alpha.uhc.kits.KitFileManager;
import de.alpha.uhc.teams.ATeam;
import de.alpha.uhc.timer.Timer;
import de.alpha.uhc.utils.Cuboid;
import de.alpha.uhc.utils.HoloUtil;
import de.alpha.uhc.utils.Regions;
import de.alpha.uhc.utils.Stats;
import de.alpha.uhc.utils.WorldPopulator;

public class UHCCommand implements CommandExecutor {
	
	public static String noplayer;
	public static String noperms;
	public static String spawnset;
	public static String lobbyset;
	public static boolean teamMode;
	
	private SpawnFileManager sfm = new SpawnFileManager();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, final String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(Core.getPrefix() + noplayer);
			return true;
		}
		
		final Player p = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("uhc")) {
			
			if(args.length == 2) {
				if(args[0].equalsIgnoreCase("team")) {
					if(teamMode == true) {
						ATeam.addPlayerToTeam(p, args[1]);
						return true;
					}
				}
			}
			
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("team") || args[0].equalsIgnoreCase("teams")) {
					String a = ATeam.allTeams.replace("[teams]", ""+ATeam.teamNames);
					p.sendMessage(Core.getPrefix() + a);
					return true;
				}
				if(args[0].equalsIgnoreCase("stats")) {
					new Stats(p).sendStats();
					return true;
				}
				
				if(p.hasPermission("UHC.start")) {
					if(args[0].equalsIgnoreCase("start")) {
						Timer.changeTime();
						return true;
					}
				} else {
					p.sendMessage(Core.getPrefix() + noperms);
					return true;
				}
			}
			
			if(args.length == 0) {
				if(p.hasPermission("UHC.admin")) {
					p.sendMessage("�8---===UHC===---");
					p.sendMessage("�7 /uhc setSpawn - Set your Arena");
					p.sendMessage("�7 /uhc setLobby - Set your Lobby, where the players will wait.");
					p.sendMessage("�7 /uhc createLobby - Create a lobbyregion, which Player won't be able to leave");
					p.sendMessage("�7 /uhc createWorld <name> - create a new random world");
					p.sendMessage("�7 /uhc createHologram [lowerby deep] <name> - create a hologram with Player stats");
					p.sendMessage("�7 /uhc restart - reload the server to restart UHC");
					p.sendMessage("�7 /uhc reload - reload all UHC files");
					p.sendMessage("�7 /uhc addKit <name> <GUI block> <GUI slot> <price> <itemlore> - adds a kit with your current inventory");
					p.sendMessage("�7 /uhc start - short the countdown to 10 seconds");
					p.sendMessage("�7 /uhc team [teamname] - See all teams [join this team]");
					p.sendMessage("�7 /uhc stats - see your stats");
					p.sendMessage("�8---===XXX===---");
					return true;
				} else {
					p.sendMessage("�8---===UHC===---");
					p.sendMessage("�7 /uhc stats - see your stats");
					p.sendMessage("�7 /uhc team [teamname] - See all teams [join this team]");
					p.sendMessage("�8---===XXX===---");
					return true;
				}
			}
			
			if(p.hasPermission("UHC.admin")) {	
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("restart")) {
						new BukkitRunnable() {
							
							@Override
							public void run() {
								Bukkit.reload();
							}
						}.runTaskLater(Core.getInstance(), 20);
						return true;
					}
					if(args[0].equalsIgnoreCase("reload")) {
						
						new OptionsFileManager().addOptions();
						new OptionsFileManager().loadOptions();
							
						new MessageFileManager().addMessages();
						new MessageFileManager().loadMessages();
						
						SpawnFileManager.getSpawnFile();
						SpawnFileManager.registerRegions();
						
						TeamFile.addDefaultTeams();
						TeamFile.loadTeams();
						
						HologramFileManager.getHologramFile().save();
						p.sendMessage(Core.getPrefix() + "�cAll configs has been reloaded");
						return true;
					}
					
					
					if(args[0].equalsIgnoreCase("setSpawn")) {
						
						sfm.SetSpawn(p.getLocation().getX(),
								p.getLocation().getY(),
								p.getLocation().getZ(),
								p.getWorld());
						p.sendMessage(Core.getPrefix() + spawnset);
						return true;
						
					}
					
					if(args[0].equalsIgnoreCase("setLobby")) {
						
						sfm.SetLobby(p.getLocation().getX(),
								p.getLocation().getY(),
								p.getLocation().getZ(),
								p.getWorld());
						p.sendMessage(Core.getPrefix() + lobbyset);
						return true;
						
					}
					
					if(args[0].equalsIgnoreCase("createLobby")) {
						if(Regions.getDefined(p)) {
							
							Regions.addRegion((new Cuboid(Regions.getPos1(p), Regions.getPos2(p))));
							SpawnFileManager.addRegion(Regions.getPos1(p), Regions.getPos2(p));
							
							p.sendMessage(Core.getPrefix() + "�7You have created a lobbyregion.");
							return true;
							
						} else {
							p.sendMessage(Core.getPrefix() + "�7You have to definde 2 lobbypoints first.");
							return true;
						}
					}
				}
				
				if(args.length == 2) {
					
					if(args[0].equalsIgnoreCase("createWorld")) {
						
						Bukkit.createWorld(new WorldCreator(args[1])
						.generateStructures(false)
						.type(WorldType.NORMAL));
						
						Bukkit.getWorld(args[1]).getPopulators().add(new WorldPopulator());
						new BukkitRunnable() {
							
							@Override
							public void run() {
								
								p.teleport(Bukkit.getWorld(args[1]).getSpawnLocation());
								
							}
						}.runTaskLater(Core.getInstance(), 200);
						return true;
					}
				}
				
				String lore = "";
				String name = "";
				if(args.length >= 2) {
					if(args.length >= 4) {
						if(args[0].equalsIgnoreCase("createHologram")) {
							if(args[1].equalsIgnoreCase("lowerby")) {
								for(Player all : Bukkit.getOnlinePlayers()) {
									for(int i = 3; i < args.length; i++) {
										name = name + args[i] + " ";
									}
									
									new HologramFileManager().addHoloram(name, p.getLocation(), Double.parseDouble(args[2]));
									
									for (int i = 0; i < new HologramFileManager().holocount(); i++) {
										new HoloUtil().createHologram(all, i, Double.parseDouble(args[2]));
									}
								}
								p.sendMessage(Core.getPrefix() + "�7You have created a new Hologram");
								return true;
							}
						}
					}
					if(args[0].equalsIgnoreCase("createHologram")) {
						if(args[1].equalsIgnoreCase("lowerby")) {
							p.sendMessage(Core.getPrefix() + "/uhc createHologram [lowerby deep] <name>");
							return true;
						}
						for(Player all : Bukkit.getOnlinePlayers()) {
							for(int i = 1; i < args.length; i++) {
								name = name + args[i] + " ";
							}
							
							new HologramFileManager().addHoloram(name, p.getLocation(), 0);
							
							for (int i = 0; i < new HologramFileManager().holocount(); i++) {
								new HoloUtil().createHologram(all, i, 0);
							}
						}
						p.sendMessage(Core.getPrefix() + "�7You have created a new Hologram");
						return true;
					}
				}
				if(args.length >= 6) {
					if(args[0].equalsIgnoreCase("addKit")) { 
						
						for(int i = 5; i < args.length; i++) {
							lore = lore + args[i] + " ";
						}
						
						new KitFileManager().addKit(args[1], p.getInventory(), args[2], Integer.parseInt(args[3]), lore, Integer.parseInt(args[4]));
						p.sendMessage(Core.getPrefix()+"�7You have set the kit �a"+args[1]+" �7with GUI-block �a"+args[2]+"�7 on GUI-slot �a"+args[3]+"�7 with the price of �a"+args[4]+" �7and the lore �a"+lore);
						lore = "";
						GUI.fill();
						return true;
					}
				}
				
			} else {
				p.sendMessage(Core.getPrefix() + noperms);
				return true;
			}
		}
		
		return false;
	}
}

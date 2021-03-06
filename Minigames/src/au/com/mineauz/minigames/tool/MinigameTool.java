package au.com.mineauz.minigames.tool;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.MinigameUtils;
import au.com.mineauz.minigames.Minigames;
import au.com.mineauz.minigames.menu.Callback;
import au.com.mineauz.minigames.menu.InteractionInterface;
import au.com.mineauz.minigames.menu.Menu;
import au.com.mineauz.minigames.menu.MenuItemCustom;
import au.com.mineauz.minigames.menu.MenuItemToolMode;
import au.com.mineauz.minigames.menu.MenuItemToolTeam;
import au.com.mineauz.minigames.minigame.Minigame;
import au.com.mineauz.minigames.minigame.TeamColor;
import au.com.mineauz.minigames.minigame.modules.TeamsModule;

public class MinigameTool {
	private ItemStack tool;
	private Minigame minigame = null;
	private ToolMode mode = null;
	private TeamColor team = null;
	
	public MinigameTool(ItemStack tool){
		this.tool = tool;
		ItemMeta meta = tool.getItemMeta();
		if(meta.getLore() != null){
			String mg = ChatColor.stripColor(meta.getLore().get(0)).replace("Minigame: ", "");
			if(Minigames.plugin.mdata.hasMinigame(mg))
				minigame = Minigames.plugin.mdata.getMinigame(mg);
			
			String md = ChatColor.stripColor(meta.getLore().get(1)).replace("Mode: ", "").replace(" ", "_");
			mode = ToolModes.getToolMode(md);
			
			team = TeamColor.matchColor(ChatColor.stripColor(meta.getLore().get(2).replace("Team: ", "")).toUpperCase());
		}
		else{
			meta.setDisplayName(ChatColor.GREEN + "Minigame Tool");
			List<String> lore = new ArrayList<String>();
			lore.add(ChatColor.AQUA + "Minigame: " + ChatColor.WHITE + "None");
			lore.add(ChatColor.AQUA + "Mode: " + ChatColor.WHITE + "None");
			lore.add(ChatColor.AQUA + "Team: " + ChatColor.WHITE + "None");
			meta.setLore(lore);
			tool.setItemMeta(meta);
		}
	}
	
	public ItemStack getTool(){
		return tool;
	}
	
	public void setMinigame(Minigame minigame){
		ItemMeta meta = tool.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(0, ChatColor.AQUA + "Minigame: " + ChatColor.WHITE + minigame.getName(false));
		meta.setLore(lore);
		tool.setItemMeta(meta);
		this.minigame = minigame;
	}
	
	public void setMode(ToolMode mode){
		ItemMeta meta = tool.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(1, ChatColor.AQUA + "Mode: " + ChatColor.WHITE + MinigameUtils.capitalize(mode.getName().replace("_", " ")));
		meta.setLore(lore);
		tool.setItemMeta(meta);
		this.mode = mode;
	}
	
	public ToolMode getMode(){
		return mode;
	}
	
	public Minigame getMinigame(){
		return minigame;
	}
	
	public void setTeam(TeamColor color){
		ItemMeta meta = tool.getItemMeta();
		List<String> lore = meta.getLore();
		
		if(color == null){
			lore.set(2, ChatColor.AQUA + "Team: " + ChatColor.WHITE + "None");
		}
		else{
			lore.set(2, ChatColor.AQUA + "Team: " + color.getColor() + MinigameUtils.capitalize(color.toString().replace("_", " ")));
		}
			
		meta.setLore(lore);
		tool.setItemMeta(meta);
		team = color;
	}
	
	public TeamColor getTeam(){
		return team;
	}
	
	public void addSetting(String name, String setting){
		ItemMeta meta = tool.getItemMeta();
		List<String> lore = meta.getLore();
		lore.add(ChatColor.AQUA + name + ": " + ChatColor.WHITE + setting);
		meta.setLore(lore);
		tool.setItemMeta(meta);
	}
	
	public void changeSetting(String name, String setting){
		removeSetting(name);
		addSetting(name, setting);
	}
	
	public String getSetting(String name){
		ItemMeta meta = tool.getItemMeta();
		List<String> lore = meta.getLore();
		for(String l : lore){
			if(ChatColor.stripColor(l).startsWith(name)){
				return ChatColor.stripColor(l).replace(name + ": ", "");
			}
		}
		return "None";
	}
	
	public void removeSetting(String name){
		ItemMeta meta = tool.getItemMeta();
		List<String> lore = meta.getLore();
		for(String l : new ArrayList<String>(lore)){
			if(ChatColor.stripColor(l).startsWith(name)){
				lore.remove(l);
				break;
			}
		}
		meta.setLore(lore);
		tool.setItemMeta(meta);
	}
	
	public void openMenu(MinigamePlayer player){
		Menu men = new Menu(2, "Set Tool Mode", player);
		
		final MenuItemCustom miselect = new MenuItemCustom("Select", MinigameUtils.stringToList("Selects and area;or points visually"), Material.DIAMOND_BLOCK);
		final MenuItemCustom mideselect = new MenuItemCustom("Deselect", MinigameUtils.stringToList("Deselects an;area or points"), Material.GLASS);
		final MinigamePlayer fply = player;
		miselect.setClick(new InteractionInterface() {
			
			@Override
			public Object interact(Object object) {
				if(mode != null){
					mode.select(fply, minigame, TeamsModule.getMinigameModule(minigame).getTeam(team));
				}
				return miselect.getItem();
			}
		});
		mideselect.setClick(new InteractionInterface() {
			
			@Override
			public Object interact(Object object) {
				if(mode != null){
					mode.deselect(fply, minigame, TeamsModule.getMinigameModule(minigame).getTeam(team));
				}
				return mideselect.getItem();
			}
		});
		
		men.addItem(mideselect, men.getSize() - 1);
		men.addItem(miselect, men.getSize() - 2);
		
		List<String> teams = new ArrayList<String>(TeamColor.values().length + 1);
		for(TeamColor col : TeamColor.values())
			teams.add(MinigameUtils.capitalize(col.toString().replace("_", " ")));
		teams.add("None");
		
		men.addItem(new MenuItemToolTeam("Team", Material.PAPER, new Callback<String>() {
			
			@Override
			public void setValue(String value) {
				setTeam(TeamColor.matchColor(value.replace(" ", "_")));
			}
			
			@Override
			public String getValue() {
				if(getTeam() != null)
					return MinigameUtils.capitalize(getTeam().toString().replace("_", " "));
				return "None";
			}
		}, teams), men.getSize() - 3);
		
		for(ToolMode m : ToolModes.getToolModes()){
			men.addItem(new MenuItemToolMode(m.getDisplayName(), MinigameUtils.stringToList(m.getDescription()), m.getIcon(), m));
		}
		
		men.displayMenu(player);
	}
}

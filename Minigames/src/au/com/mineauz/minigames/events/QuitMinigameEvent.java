package au.com.mineauz.minigames.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import au.com.mineauz.minigames.MinigamePlayer;
import au.com.mineauz.minigames.minigame.Minigame;

public class QuitMinigameEvent extends Event implements Cancellable{
	private static final HandlerList handlers = new HandlerList();
	private MinigamePlayer player = null;
	private Minigame mgm = null;
	private boolean cancelled = false;
	private boolean isForced = false;
	private boolean isWinner = false;
	
	public QuitMinigameEvent(MinigamePlayer player, Minigame minigame, boolean forced, boolean isWinner){
		this.player = player;
		mgm = minigame;
		isForced = forced;
		this.isWinner = isWinner;
	}
	
	public MinigamePlayer getMinigamePlayer(){
		return player;
	}
	
	public Player getPlayer(){
		return player.getPlayer();
	}
	
	public Minigame getMinigame(){
		return mgm;
	}
	
    public boolean isForced() {
		return isForced;
	}
	
	public boolean isWinner(){
		return isWinner;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public HandlerList getHandlers() {
        return handlers;
    }
 
    public static HandlerList getHandlerList() {
        return handlers;
    }

}

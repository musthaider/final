
public class DisappearEvent extends Event {
	private String itemName;
	private String logEntry;
	public DisappearEvent(String itemName) {
		this.itemName = itemName;
        this.logEntry = logEntry;
	}

	@Override
	void trigger() throws Item.NoItemException {
	
		Item targetItem = GameState.instance().getDungeon().getItem(itemName);
		String targetItemOrigin = "";
		if (GameState.instance().isItemInPlayerInventory(itemName)) {
			GameState.instance().removeFromInventory(targetItem);
			targetItemOrigin = "player-inventory";

		} else {
			GameState.instance().getAdventurersCurrentRoom().remove(targetItem);
			targetItemOrigin = GameState.instance().getAdventurersCurrentRoom().getTitle();
		}

		GameState.instance().getDungeon().removeItemFromGame(targetItem);
		String lead0 = "Event: ";
		String lead1 = "Removed From: ";
		logEntry = String.format("  %14sDisappear(%s) %n  %14s%s%n", lead0,itemName,lead1,targetItemOrigin);
		GameState.instance().logAction(logEntry);
	}
}


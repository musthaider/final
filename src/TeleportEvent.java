import java.util.ArrayList;
import java.util.Random;
import java.math.*;

public class TeleportEvent extends Event {

	private Room destinationRoom;
	private String logEntry = "";
	public String fromRoom;

	public TeleportEvent(Room destinationRoom) {
		this.destinationRoom = destinationRoom;
		fromRoom = GameState.instance().getAdventurersCurrentRoom().getName();
	}
	
	public TeleportEvent() {
		this.destinationRoom = null;
		this.fromRoom = GameState.instance().getAdventurersCurrentRoom().getName();
		this.logEntry = logEntry;
	}


	@Override
	void trigger() {
		
		if (destinationRoom != null) {
			GameState.instance().setAdventurersCurrentRoom(destinationRoom);
		} else {
				ArrayList<Room> randomDestinationList = new ArrayList<Room>(
				GameState.instance().getDungeon().getTeleTable().values());
				do {
					int randomIndex = (int) (Math.random() * randomDestinationList.size());
					destinationRoom = randomDestinationList.get(randomIndex);
				} while (destinationRoom.getName().equals(fromRoom));
				GameState.instance().setAdventurersCurrentRoom(destinationRoom);
		} 		
		String lead0 = "Event: ";
		String lead1 = "Details: ";
		logEntry = String.format("  %14sTeleport() %n  %14s%s--->%s%n", lead0, lead1, fromRoom, destinationRoom.getName());
		GameState.instance().logAction(logEntry);

	}
}

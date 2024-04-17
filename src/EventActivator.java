import java.io.FileNotFoundException;
import java.util.regex.Pattern;

public class EventActivator {

	String eventDetails = "";
    Event manualEvent = null; 
    String EventItem = null;

	public EventActivator() {
    
	}

	public EventActivator(String eventDetails) {
		this.eventDetails = eventDetails;
		if (eventDetails.startsWith("*")) {
			String[] eventDetailParts = eventDetails.substring(1).split(Pattern.quote("*"));
			this.EventItem = eventDetailParts[0];
			this.eventDetails = eventDetailParts[1];
		}
	}

	public EventActivator(Event manualEvent) {
		this.manualEvent = manualEvent;
	}
	
	public void setManualEvent(Event e) {
		this.manualEvent = e;
	}
	
	public void reset() {
		setManualEvent(null);
		setEventDetails("");
	}

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

	public void activate() throws Item.NoItemException {
		String[] eventBuffer = eventDetails.split(Pattern.quote(","));
		if (manualEvent != null) {
			EventFactory.instance().parse(manualEvent.getDescription()).trigger();
		} else if (eventBuffer.length == 1) {
			EventFactory.instance().parse(EventItem, eventDetails).trigger();
		} else {
			int numOfEvents = eventBuffer.length;
			int eventIndex = 0;
			for (int i = numOfEvents; i > 0; i--) {
				EventFactory.instance().parse(EventItem, eventBuffer[eventIndex]).trigger();
				eventIndex++;
			}
		}
		reset();
	}
}

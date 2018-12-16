import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Pattern;

class PlaceImpl implements Place {
	public boolean start;
	public boolean end;
	public String Name;
	public int X;
	public int Y;
	public Set<Road> roads = new LinkedHashSet<Road>();
	private Set<PlaceListener> placeListenersSet = new HashSet<PlaceListener>();
	public PlaceImpl() {
	}

	public void addListener(PlaceListener pl) {
		placeListenersSet.add(pl);
	}

	public void deleteListener(PlaceListener pl) {
		if (placeListenersSet.contains(pl)) {
			placeListenersSet.remove(pl);	
		}

	}

	// Return a set containing all roads that reach this place
	public Set<Road> toRoads() {
		return roads;
	}

	// Return the road from this place to dest, if it exists
	// Returns null, if it does not
	public Road roadTo(Place dest) {
		for (Road road : roads) {
			if (road.firstPlace() == this && road.secondPlace() == dest) {
				return road;
			}
			if (road.secondPlace() == this && road.firstPlace() == dest) {
				return road;
			}
		}
		return null;
	}

	// Move the position of this place
	// by (dx,dy) from its current position
	public void moveBy(int dx, int dy) {
		System.out.println("fdsfdsfsafd");
		X += dx;
		Y += dy;
		PlaceModify();
	}

	// Return the name of this place
	public String getName() {
		return Name;
	}

	// Return the X position of this place
	public int getX() {
		return X;
	}

	// Return the Y position of this place
	public int getY() {
		return Y;
	}

	// Return true if this place is the starting place for a trip
	public boolean isStartPlace() {
		return start;
	}
    //Return true if this place is the ending place for a trip
	public boolean isEndPlace() {
		return end;
	}
    //Return a string containing information about this place 
    //in the form (without the quotes, of course!) :
    //"placeName(xPos,yPos)" 
	public String toString() {
		String string = "";
		string += Name;
		string += "(";
		string += X;
		string += ",";
		string += Y;
		string += ")";
		return string;
	}
	public void PlaceModify() {
		for (PlaceListener placeListener : placeListenersSet) {
			placeListener.placeChanged();
		}
	}
	
}

class RoadImpl implements Road {

	public String Name;
	public int length;
	public Place from;
	public Place to;
	private Set<RoadListener> roadListenersSet = new HashSet<RoadListener>();

	public RoadImpl() {
	}

	public void addListener(RoadListener rl) {
		roadListenersSet.add(rl);
	}

	public void deleteListener(RoadListener rl) {
		if (roadListenersSet.contains(rl)) {
			roadListenersSet.remove(rl);
		}
	}

	private int stringCompare(String s1, String s2) {
		return s1.compareTo(s2);
	}

	public Place firstPlace() {
		int res = this.stringCompare(from.getName(), to.getName());
		if (res > 0)
			return to;
		else
			return from;
	}

	public Place secondPlace() {
		int res = this.stringCompare(from.getName(), to.getName());
		if (res > 0)
			return from;
		else
			return to;
	}

	public boolean isChosen() {
		return true;
	}

	public void setRoadName(String name) {
		Name = name;
	}

	public String roadName() {
		return Name;
	}

	public void setLength(int l) {
		length = l;
		RoadModify();
		
	}

	public int length() {
		return length;
	}

	public String toString() {
		String string = "";
		string += firstPlace().getName();
		string += "(" + Name + ":" + length + ")";
		string += secondPlace().getName();
		return string;
	}
	public void RoadModify() {
		for (RoadListener roadListener : roadListenersSet) {
			roadListener.roadChanged();
		}
	}
}

public class MapImpl implements Map {
//	private MapPanel mapPanel;
	private boolean isChanged;
	private Set<Place> places = new LinkedHashSet<Place>();
	private Set<Road> roads = new LinkedHashSet<Road>();
	private Set<MapListener> mapListeners =  new LinkedHashSet<MapListener>();

	public MapImpl() {
	
	}

	public void addListener(MapListener ml) {
		mapListeners.add(ml);
	}

	public void deleteListener(MapListener ml) {
		mapListeners.remove(ml);
	}

	public Place newPlace(String placeName, int xPos, int yPos) throws IllegalArgumentException {
		boolean ok = Pattern.matches("[a-zA-Z]([a-zA-Z]|[0-9]|[_])*", placeName);

		// the name is not valid or is the same as that
		// Throws IllegalArgumentException
		if (ok == false || findPlace(placeName) != null) {
			throw new IllegalArgumentException();
		}
		
		PlaceImpl impl = new PlaceImpl();
		impl.Name = placeName;
		impl.X = xPos;
		impl.Y = yPos;
		places.add(impl);
		MapChanged();
		return impl;
	}

	public void deletePlace(Place place) {
		if (place == null) {
			return;
		}
		places.remove(place);
		MapChanged();
	}

	public Place findPlace(String placeName) {
		Place place = null;
		for (Place p : places) {
			if (p.getName().equals(placeName)) {
				place = p;
				break;
			}
		}
		return place;
	}

	public Set<Place> getPlaces() {
		return places;
	}
	private void MapChanged() {
		for (MapListener ml : mapListeners) {
			ml.otherChanged();
			ml.placesChanged();

		}
	}

	public Road newRoad(Place from, Place to, String roadName, int length) throws IllegalArgumentException {

		if (findPlace(from.getName()) == null || findPlace(to.getName()) == null) {
			throw new IllegalArgumentException();
		}

		boolean ok = Pattern.matches("([a-zA-Z]([a-zA-Z]|[0-9])*)|[-]", roadName);
		if (ok) {
			RoadImpl impl = new RoadImpl();
			impl.from = from;
			impl.to = to;
			impl.Name = roadName;
			impl.length = length;

			PlaceImpl impl_from = ((PlaceImpl) from);
			PlaceImpl impl_to = ((PlaceImpl) to);
			if (!impl_from.roads.contains(impl)) {
				impl_from.roads.add(impl);
			}
			if (!impl_to.roads.contains(impl)) {
				impl_to.roads.add(impl);
			}
			roads.add(impl);
			
//			this.mapPanel.roadsChanged();
			MapChanged();
			return impl;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void deleteRoad(Road rd) {
		if (rd == null) {
			return;
		}
		roads.remove(rd);
		MapChanged();
	}

	public Set<Road> getRoads() {
		return roads;
	}

	public void setStartPlace(Place p) throws IllegalArgumentException {
		if (p == null) {
			Place start = getStartPlace();
			if (start != null) {
				PlaceImpl impl = (PlaceImpl) start;
				impl.start = false;
			}
		} else {
			Place start = findPlace(p.getName());
			if (start == null) {
				throw new IllegalArgumentException();
			} else {
				PlaceImpl impl = (PlaceImpl) start;
				impl.start = true;
			}
		}
		MapChanged();

	}

	public Place getStartPlace() {
		Place start = null;
		for (Place place : places) {
			if (place.isStartPlace()) {
				start = place;
				break;
			}
		}
		return start;
	}

	public void setEndPlace(Place p) throws IllegalArgumentException {
		PlaceImpl impl=null;
		if (p == null) {
			Place end = getEndPlace();
			if (end != null) {
				impl = (PlaceImpl) end;
				impl.end = false;
				
			}
		} else {
			Place end = findPlace(p.getName());
			if (end == null) {
				throw new IllegalArgumentException();
			} else {
				impl = (PlaceImpl) end;
				impl.end = true;
			}

		}
		MapChanged();
	}

	public Place getEndPlace() {
		Place end = null;
		for (Place place : places) {
			if (place.isEndPlace()) {
				end = place;
				break;
			}
		}
		return end;
	}

	public int getTripDistance() {
		return 0;
	}

	public String toString() {
		String string = "";
		for (Place place : places) {
			string += "PLACE ";
			string += place.toString();
			string += "\n";
		}
		for (Road road : roads) {
			string += "ROAD ";
			string += road.toString();
			string += "\n";
		}
		Place start = getStartPlace();
		if (start != null) {
			string += "START ";
			string += start.getName();
			string += "\n";
		}
		Place end = this.getEndPlace();
		if (end != null) {
			string += "END ";
			string += end.getName();
			string += "\n";
		}
		return string;
	}
}

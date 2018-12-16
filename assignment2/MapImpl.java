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

	public PlaceImpl() {
	}

	public void addListener(PlaceListener pl) {
	}

	public void deleteListener(PlaceListener pl) {
	}

	public Set<Road> toRoads() {
		return roads;
	}

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

	public void moveBy(int dx, int dy) {
		X += dx;
		Y += dy;
	}

	public String getName() {
		return Name;
	}

	public int getX() {
		return X;
	}

	public int getY() {
		return Y;
	}

	public boolean isStartPlace() {
		return start;
	}

	public boolean isEndPlace() {
		return end;
	}

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
}

class RoadImpl implements Road {

	public String Name;
	public int length;
	public Place from;
	public Place to;

	public RoadImpl() {
	}

	public void addListener(RoadListener rl) {
	}

	public void deleteListener(RoadListener rl) {
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
		return false;
	}

	public void setRoadName(String name) {
		Name = name;
	}

	public String roadName() {
		return Name;
	}

	public void setLength(int l) {
		length = l;
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
}

public class MapImpl implements Map {

	private Set<Place> places = new LinkedHashSet<Place>();
	private Set<Road> roads = new LinkedHashSet<Road>();

	public MapImpl() {
	}

	public void addListener(MapListener ml) {
	}

	public void deleteListener(MapListener ml) {
	}

	public Place newPlace(String placeName, int xPos, int yPos)
			throws IllegalArgumentException {
		boolean ok = Pattern
				.matches("[a-zA-Z]([a-zA-Z]|[0-9]|[_])*", placeName);

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
		return impl;
	}

	public void deletePlace(Place place) {
		if (place == null) {
			return;
		}
		places.remove(place);
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

	public Road newRoad(Place from, Place to, String roadName, int length)
			throws IllegalArgumentException {

		if (findPlace(from.getName()) == null
				|| findPlace(to.getName()) == null) {
			throw new IllegalArgumentException();
		}

		boolean ok = Pattern.matches("([a-zA-Z]([a-zA-Z]|[0-9])*)|[-]",
				roadName);
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
		if (p == null) {
			Place end = getEndPlace();
			if (end != null) {
				PlaceImpl impl = (PlaceImpl) end;
				impl.end = false;
			}
		} else {
			Place end = findPlace(p.getName());
			if (end == null) {
				throw new IllegalArgumentException();
			} else {
				PlaceImpl impl = (PlaceImpl) end;
				impl.end = true;
			}
		}
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

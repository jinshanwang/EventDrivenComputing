import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.regex.Pattern;

public class MapReaderWriter implements MapIo {

	public MapReaderWriter() {

	}
	

	public void read(Reader r, Map m) throws IOException, MapFormatException {
		MapImpl impl = (MapImpl) m;
		int lineNr = 0;
		BufferedReader br = new BufferedReader(r);
		String record = null;
		while ((record = br.readLine()) != null) {
			lineNr++;
			record = record.trim();
			if (record.startsWith("#") || record.length() == 0) {
				continue;
			}

			if (record.startsWith("place")) {
				String[] elements = record.split("\\s+");
				if (elements.length == 4) {
					String name = elements[1];
					String x = elements[2];
					String y = elements[3];
					boolean ok = Pattern.matches(
							"[a-zA-Z]([a-zA-Z]|[0-9]|[_])*", name);
					if (!ok || !isNumber(x) || !isNumber(y)
							|| impl.findPlace(name) != null) {
						throw new MapFormatException(lineNr, "");
					} else {
						impl.newPlace(name, Integer.parseInt(x),
								Integer.parseInt(y));
					}
				}
			} else if (record.startsWith("road")) {
				String[] elements = record.split("\\s+");
				if (elements.length == 5) {
					String firstPlace = elements[1];
					String roadName = elements[2];
					String length = elements[3];
					String secondPlace = elements[4];
					Place first = impl.findPlace(firstPlace);
					Place second = impl.findPlace(secondPlace);
					boolean ok = Pattern.matches(
							"([a-zA-Z]([a-zA-Z]|[0-9])*)|[-]", roadName);
					if (!ok || !isNumber(length) || first == null
							|| second == null) {
						throw new MapFormatException(lineNr, "");
					} else {
						impl.newRoad(first, second, roadName,
								Integer.parseInt(length));
					}
				}
				
			} else if (record.startsWith("start")) {
				String[] elements = record.split("\\s+");
				if (elements.length == 2) {
					Place p = impl.findPlace(elements[1]);
					if (p != null) {
						PlaceImpl start = (PlaceImpl) p;
						start.start = true;
					}
				}
			
			} else if (record.startsWith("end")) {
				String[] elements = record.split("\\s+");
				if (elements.length == 2) {
					Place p = impl.findPlace(elements[1]);
					if (p != null) {
						PlaceImpl end = (PlaceImpl) p;
						end.end = true;
					}
				}
				
			}
		}
	}

	public static boolean isNumber(String num) {
		try {
			Integer.parseInt(num);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void write(Writer w, Map m) throws IOException {
		String res = toString(m);
		// System.out.println(res);
		w.write(res);
		w.flush();
	}

	private String toString(Map m) {
		String string = "";
		for (Place place : m.getPlaces()) {
			string += "place ";
			string += place.getName();
			string += " ";
			string += place.getX();
			string += " ";
			string += place.getY();
			string += "\n";
		}
		for (Road road : m.getRoads()) {
			string += "road ";
			string += road.firstPlace().getName();
			string += " ";
			string += road.roadName();
			string += " ";
			string += road.length();
			string += " ";
			string += road.secondPlace().getName();
			string += "\n";
		}
		Place start = m.getStartPlace();
		if (start != null) {
			string += "start ";
			string += start.getName();
			string += "\n";
		}
		Place end = m.getEndPlace();
		if (end != null) {
			string += "end ";
			string += end.getName();
			string += "\n";
		}
		return string;
	}
}

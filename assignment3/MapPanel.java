import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JPanel;

public class MapPanel extends JPanel implements MapListener, MouseListener, MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3814800093388044233L;
	private Map map;
	private Rectangle rectangle;
	private int SelectionBox_X;
	private int SelectionBox_Y;
	private HashMap<Place, PlaceIcon> MapPanel_Place = new HashMap<Place, PlaceIcon>();
	private static final int MapPanel_WIDTH = 1000;
	private static final int MapPanel_HEIGHT = 1000;

	public MapPanel() {

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		rectangle = new Rectangle(SelectionBox_X, SelectionBox_Y, 0, 0);
		this.setBounds(0, 0, MapPanel_WIDTH, MapPanel_HEIGHT);
		this.setLayout(null);
	}

	public void refresh(Map map) {
		this.map = map;
		this.MapPanel_Place = new HashMap<Place, PlaceIcon>();
		this.removeAll();
		this.repaint();

	}

	@Override
	public void placesChanged() {
		System.out.println("Places Changed");
		Set<Place> map_place = map.getPlaces();
		for (Place place : map_place) {
			if (getMapPanel_Place().containsKey(place) == false) {
				PlaceIcon MapPanel_newPlace = new PlaceIcon(place);
				place.addListener(MapPanel_newPlace);
				getMapPanel_Place().put(place, MapPanel_newPlace);
				this.add(MapPanel_newPlace);
			}
		}

//		delete a placeIcon
		Set<Place> places = MapPanel_Place.keySet();
		for (Place place : places) {
			if (map_place.contains(place) == false) {
				this.remove(MapPanel_Place.get(place));
			}
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		Graphics2D graphics2d = (Graphics2D) g;
		graphics2d.draw(rectangle);
	}

	@Override
	public void roadsChanged() {
		System.out.println("Roads Changed");
		Set<Road> map_road = map.getRoads();
		for (Road road : map_road) {
			if (getMapPanel_Road().containsKey(road) == false) {
				RoadIcon MapPanel_newRoad = new RoadIcon(road);
				road.addListener(MapPanel_newRoad);
				getMapPanel_Road().put(road, MapPanel_newRoad);
				this.add(MapPanel_newRoad);
			}
		}
	}

	@Override
	public void otherChanged() {
		System.out.println("Others Changed");
		this.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
//		we unset the status of all the placeIcon in the Panel by checking the whole hashMap
		if (this.MapPanel_Place.isEmpty() == false) {
			Set<Place> places = this.getMapPanel_Place().keySet();
			for (Place place : places) {
				PlaceIcon placeIcon = this.getMapPanel_Place().get(place);
				placeIcon.setSelected(false);
				placeIcon.repaint();
			}
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
//		this is the location of the selection Box
		this.SelectionBox_X = e.getX();
		this.SelectionBox_Y = e.getY();
		this.rectangle = new Rectangle(SelectionBox_X, SelectionBox_Y, 0, 0);
//		this.repaint();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		rectangle = new Rectangle(0, 0, 0, 0);
		this.repaint();

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public HashMap<Road, RoadIcon> getMapPanel_Road() {
		return null;
	}

	/**
	 * @return the mapPanel_Place
	 */
	public HashMap<Place, PlaceIcon> getMapPanel_Place() {
		return MapPanel_Place;
	}

	/**
	 * @param mapPanel_Place the mapPanel_Place to set
	 */
	public void setMapPanel_Place(HashMap<Place, PlaceIcon> mapPanel_Place) {
		MapPanel_Place = mapPanel_Place;
	}

	public Place getSelectedPlace() {
		Set<Place> places = this.getMapPanel_Place().keySet();
		for (Place place : places) {
			PlaceIcon placeIcon = this.getMapPanel_Place().get(place);
			if (placeIcon.isSelected() == true) {
				return place;
			}
		}
		return null;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		int x = Math.abs(e.getX());
		int y = Math.abs(e.getY());
//		If you drag the mouse right-and-down
		if (this.SelectionBox_X < x && this.SelectionBox_Y < y) {
			rectangle = new Rectangle(this.SelectionBox_X, SelectionBox_Y, Math.abs(x - SelectionBox_X),
					Math.abs(y - SelectionBox_Y));
		}
//		If you move up-and-left
		else if (this.SelectionBox_X > x && this.SelectionBox_Y > y) {
			rectangle = new Rectangle(x, y, Math.abs(x - SelectionBox_X), Math.abs(y - SelectionBox_Y));
		}
//		If you move up-and-right
		else if (this.SelectionBox_X < x && this.SelectionBox_Y > y) {
			rectangle = new Rectangle(this.SelectionBox_X, y, Math.abs(x - SelectionBox_X),
					Math.abs(SelectionBox_Y - y));
		}
//		If you move down-and-left
		else if (this.SelectionBox_X > x && this.SelectionBox_Y < y) {
			rectangle = new Rectangle(x, this.SelectionBox_Y, Math.abs(SelectionBox_X - x),
					Math.abs(y - SelectionBox_Y));
		}
		Component[] placeIcons = this.getComponents();
		for (Component component : placeIcons) {
			boolean isTouch = rectangle.intersects(component.getBounds());
			if (isTouch) {

				((PlaceIcon) component).setSelected(true);

			}
		}
		this.repaint();

	}



}

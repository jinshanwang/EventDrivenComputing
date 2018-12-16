import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Set;

import javax.swing.JComponent;

public class PlaceIcon extends JComponent implements PlaceListener, MouseListener,MouseMotionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Place place;
	private String name;
	private static final int PlaceIcon_WIDTH = 50;
	private static final int PlaceIcon_HEIGHT = 50;
	private int PlaceIcon_X_axis;
	private int PlaceIcon_y_axis;
	private boolean isSelected = false;
	

	public PlaceIcon(Place place) {
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.place=place;
		this.name=this.place.getName();
		this.PlaceIcon_X_axis=place.getX();
		this.PlaceIcon_y_axis=place.getY();
		this.setVisible(true);
//		set the size of the Place Icon
		this.setBounds(PlaceIcon_X_axis, PlaceIcon_y_axis, PlaceIcon_WIDTH, PlaceIcon_HEIGHT);
//		this.setBackground(Color.CYAN);
		this.repaint();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void placeChanged() {
		this.setLocation(this.place.getX(), this.place.getY());
		this.getParent().repaint();
		
		// TODO Auto-generated method stub
		
	}
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D graphics2d = (Graphics2D) graphics;
		graphics2d.setFont(new Font("Times New Roman", Font.PLAIN, 10));
		
		graphics2d.setColor(Color.BLACK);
		if (this.place.isStartPlace()==true) {
			graphics2d.setStroke(new BasicStroke(3));
			graphics2d.setColor(Color.RED);
		}
		if (this.place.isEndPlace()==true) {
			graphics2d.setStroke(new BasicStroke(3));
			graphics2d.setColor(Color.GREEN);
		}
		if (isSelected) {
			graphics2d.fillRect(0, 0, PlaceIcon_WIDTH, PlaceIcon_HEIGHT);
			
		} else {
			graphics2d.drawRect(0, 0, PlaceIcon_WIDTH, PlaceIcon_HEIGHT);
		}

		graphics2d.drawString(this.place.getName(),0, 20);

		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("MOUSE CLICKED");
		
		if (this.isSelected==false) {
			MapPanel mapPanel= (MapPanel) this.getParent();
			Set<Place> places = mapPanel.getMapPanel_Place().keySet();
			for (Place place : places) {
				PlaceIcon placeIcon = mapPanel.getMapPanel_Place().get(place);
				placeIcon.setSelected(false);
				placeIcon.repaint();
			}
			this.setSelected(true);
			this.repaint();
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("MOUSE PRESSED");
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("MOUSE RELEASED");
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("MOUSE ENTERED");
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("MOUSE EXITED");
	}

	/**
	 * @return the isSelected
	 */
	public boolean isSelected() {
		return isSelected;
	}

	/**
	 * @param isSelected the isSelected to set
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("mouseDragging");
		int x =e.getX();
		int y =e.getY();
		place.moveBy(x, y);
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println("mouseMoving");
	}



}

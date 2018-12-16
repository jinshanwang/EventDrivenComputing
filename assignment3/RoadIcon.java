import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;

import javax.swing.JComponent;

public class RoadIcon extends JComponent implements RoadListener, MouseMotionListener, MouseListener {

	private Road road;
	private String roadName;
	private int roadLength;
	private Place from;
	private Place to;
	private int from_x;
	private int from_y;
	private int to_x;
	private int to_y;
	private int externalControl_X;
	private int externalControl_Y;
	private boolean isSelected=false;
	private boolean isRoad=true;

	public RoadIcon(Road road) {
//		access the road
		this.road = road;
//		get the name of the road
		this.roadName = this.road.roadName();
//		get the length of the road
		this.roadLength = this.road.length();
		this.from = road.firstPlace();
		this.to = road.secondPlace();
		this.from_x=this.from.getX();
		this.from_y=this.from.getY();
		this.to_x=this.to.getX();
		this.to_y=this.to.getY();
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setVisible(true);

//		this.setBounds(this.from_x,this.from_y,Math.abs(from_x-to_x),Math.abs(from_y-to_y));
//		this.setPreferredSize(getPreferredSize());
		this.repaint();
	}

	@Override
	public void roadChanged() {
		// TODO Auto-generated method stub
		this.from_x=this.from.getX();
		this.from_y=this.from.getY();
		this.to_x=this.to.getX();
		this.to_y=this.to.getY();
		this.getTopLevelAncestor().repaint();
		this.repaint();
//		this.getParent().repaint();
	}

	public void paintComponent(Graphics graphics) {
		 Line2D line = new Line2D.Double(this.from.getX(),
 this.from.getY(), this.to.getX(), this.to.getY());
		
         Graphics2D g2d = (Graphics2D) graphics;
//		 int roadNameLength = g2d.getFontMetrics(getFont()).stringWidth(roadName);
//         g2d.drawString(roadName, (from.getX()+to.getX())/2-roadNameLength, (from.getY()+to.getY())/2);
         g2d.setColor(Color.BLACK);
         g2d.draw(line);
         System.out.println("sdadfkgsjfdhkfgs");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}
//	private void SetExternalControl() {
////		 find the middle point of two place
//		if (this.from_x== this.to_x) {
//			this.externalControl_X=this.from_x;
//			if (this.from_y<this.to_y) {
//				this.externalControl_Y=Math.abs(this.from_y-this.to_y)/2+this.from_y;
//			} else {
//				this.externalControl_Y=Math.abs(this.from_y-this.to_y)/2+this.to_y;
//			}
//			
//		}
//		else if (this.from_y== this.to_y) {
//			this.externalControl_Y=this.from_y;
//			if (this.from_x<this.to_x) {
//				this.externalControl_X=Math.abs(this.from_x-this.to_x)/2+this.from_x;
//			} else {
//				this.externalControl_X=Math.abs(this.from_x-this.to_x)/2+this.to_x;
//			}	
//		}
//		else if (this.from_x>this.to_x && this.from) {
//			
//		} 
//	}
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

	/**
	 * @return the isRoad
	 */
	public boolean isRoad() {
		return isRoad;
	}

	/**
	 * @param isRoad the isRoad to set
	 */
	public void setRoad(boolean isRoad) {
		this.isRoad = isRoad;
	}
}

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.omg.CORBA.PUBLIC_MEMBER;

public class MapEditor extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private static final int MapEditor_WIDTH = 1000;
	private static final int MapEditor_HEIGHT = 1000;
	private boolean modifiedBit =false;
	private JFileChooser fileChooser = new JFileChooser(new File("."));
	private JPanel panel;
	private JPanel DisplayAreaPanel;
	private JMenuBar menubar;
	private TextArea Text_A;
	private Map map;
	private MapIo mapIo;
	private MapPanel mp;
	

	public MapEditor() {
//		set the title 
		this.setTitle("MapEditor");
//		Default close
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		set location and the size of the window
		this.setBounds(0, 0, MapEditor.MapEditor_WIDTH, MapEditor.MapEditor_HEIGHT);

//		Map Display Area
		this.mp = new MapPanel();

//		text_ area
		this.DisplayAreaPanel = new JPanel();
		this.DisplayAreaPanel.setLayout(new BoxLayout(this.DisplayAreaPanel, BoxLayout.X_AXIS));
		this.Text_A = new TextArea("hello world");
		this.Text_A.setEditable(true);
		this.Text_A.setVisible(true);
		StartPlace red = new StartPlace(Color.RED);
		StartPlace green = new StartPlace(Color.GREEN);

		this.DisplayAreaPanel.add(this.Text_A);
		this.DisplayAreaPanel.add(green);
		this.DisplayAreaPanel.add(red);

		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout());
		this.panel.add(mp, BorderLayout.CENTER);
		this.panel.add(DisplayAreaPanel, BorderLayout.SOUTH);
		this.setContentPane(panel);

		this.menubar = new JMenuBar();
		JMenu FileMenu = getFileMenu();
		JMenu EditMenu = getEditMenu();
		menubar.add(FileMenu);
		menubar.add(EditMenu);
		this.setJMenuBar(menubar);

	}

	private JMenu getFileMenu() {
//		create a menu and name it as "File"
		JMenu FileMenu = new JMenu("File");
		JMenuItem open = new JMenuItem("Open...");
		JMenuItem Save_AS = new JMenuItem("Save as...");
		JMenuItem Append = new JMenuItem("Append...");
		JMenuItem QUIT = new JMenuItem("Quit");
//		Adding a keyboard shortcut for each file-menu item
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		Save_AS.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		Append.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		QUIT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("选择打开Open Selected");
				// TODO Auto-generated method stub
				int ret_chooser = fileChooser.showOpenDialog(null);
				if (ret_chooser == JFileChooser.APPROVE_OPTION) {
//					we need to refresh the text area
					Text_A.setText("");
//					get the file
					File f = fileChooser.getSelectedFile();
				
					Reader fReader = null;
					try {
						fReader = new FileReader(f);
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					try {

						map = new MapImpl();
//						System.out.println("创建新地图对象setup成功");
						mp.refresh(map);
//						System.out.println("更新地图panel成功");

						map.addListener(mp);
						mapIo = new MapReaderWriter();
						mapIo.read(fReader, map);
						Text_A.append(map.toString());
						fReader.close();
					} catch (IOException | MapFormatException e1) {
						// TODO Auto-generated catch block
//						e1.printStackTrace();
						JDialog Error_hint = new JDialog();
						Error_hint.setVisible(true);
						int error_hint = JOptionPane.showConfirmDialog(null,
								"the MapReaderWriter error occurred Do you want to dismiss", "error",
								JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
						if (error_hint == 0) {
							System.out.println("ignore");
						}
					}

				}
			}
		});
		Save_AS.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Save AS Selected");
				int Save_chooser = fileChooser.showSaveDialog(null);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				if (Save_chooser == JFileChooser.APPROVE_OPTION) {
					File filePWD = fileChooser.getSelectedFile();
					try {
						Writer writer = new FileWriter(filePWD);
						mapIo.write(writer, map);
						writer.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		Append.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Append Selected");
				int Append_chooser = fileChooser.showOpenDialog(null);
				if (Append_chooser == JFileChooser.APPROVE_OPTION) {
					try {
						File Append_file = fileChooser.getSelectedFile();
						System.out.println("fsdfa");
						Reader reader = new FileReader(Append_file);
						System.out.println("fsdf");
						mapIo.read(reader, map);
						reader.close();
					} catch (Exception e2) {
						System.out.println("illgal file");
					}

				}

			}
		});
		QUIT.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Quit Selected");
				if (modifiedBit) {
					int rv = JOptionPane.showConfirmDialog(null,
							"unsaved changes?");
					if (rv != JOptionPane.OK_OPTION) {
						return;
					}
				}
				modifiedBit = false;
				System.exit(getDefaultCloseOperation());
			}
		});

		FileMenu.add(open);
		FileMenu.add(Save_AS);
		FileMenu.add(Append);
		FileMenu.add(QUIT);

		return FileMenu;

	}

	private JMenu getEditMenu() {
//		initialize the items
		JMenu EditMenu = new JMenu("Edit");
		JMenuItem New_place = new JMenuItem("New place");
		JMenuItem New_Road = new JMenuItem("New Road");
		JMenuItem Set_start = new JMenuItem("Set_start");
		JMenuItem Unset_start = new JMenuItem("Unset_start");
		JMenuItem Set_end = new JMenuItem("Set end");
		JMenuItem Unset_end = new JMenuItem("Unset end");
		JMenuItem Delete = new JMenuItem("Delete");
		New_place.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) throws IllegalArgumentException {
				System.out.println("New Place Selected");
				String palceName = JOptionPane.showInputDialog("Please input the place name:");
				if (palceName != null) {
					try {
						map.newPlace(palceName, 100, 100);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(panel, "the name is illegal, or the place already exist", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
				modifiedBit=true;

			}
		});
		New_Road.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("New Road Selected");
				String roadName = JOptionPane.showInputDialog("Please input the Road name:");

			}
		});
		Set_start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Set start Selected");
				HashMap<Place, PlaceIcon> current_panel = mp.getMapPanel_Place();
//				check that if there are two seleceted or not 
				if (MoreThanOnePLS(current_panel) == false) {
					PlaceImpl place = (PlaceImpl) mp.getSelectedPlace();
					if (map.getStartPlace() != null) {
						map.setStartPlace(null);
					}
					map.setStartPlace(place);
					modifiedBit=true;
				} else {
					JOptionPane.showMessageDialog(panel, "only one place can be selected", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		Unset_start.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Unset start Selected");
//				unset the starting point
				map.setStartPlace(null);
				modifiedBit=true;
			}
		});
		Set_end.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Set end Selected");
				HashMap<Place, PlaceIcon> current_panel = mp.getMapPanel_Place();
//				check that if there are two seleceted or not 
				if (MoreThanOnePLS(current_panel) == false) {
					PlaceImpl place = (PlaceImpl) mp.getSelectedPlace();
					if (map.getEndPlace() != null) {
						map.setEndPlace(null);
					}
					map.setEndPlace(place);
					modifiedBit=true;
				} else {
					JOptionPane.showMessageDialog(panel, "only one place can be selected", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		Unset_end.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Unset end Selected");
//				unset the End place
				map.setEndPlace(null);
				modifiedBit=true;

			}
		});
		Delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Delete Selected");
				modifiedBit=true;
				Set<Place> selected = new HashSet<Place>();
				Set<Place> places = mp.getMapPanel_Place().keySet();
				for (Place p : places) {
					PlaceIcon pIcon = mp.getMapPanel_Place().get(p);
					if (pIcon.isSelected()) {
						selected.add(p);
					}
				}
				for (Place place : selected) {
					map.deletePlace(place);
				}
			}
		});
//		add them to the menu
		EditMenu.add(New_place);
		EditMenu.add(New_Road);
		EditMenu.add(Set_start);
		EditMenu.add(Unset_start);
		EditMenu.add(Set_end);
		EditMenu.add(Unset_end);
		EditMenu.add(Delete);

		return EditMenu;

	}

//	If more than one place is selected, return false
	private boolean MoreThanOnePLS(HashMap<Place, PlaceIcon> hashMap) {
		Set<Place> places = hashMap.keySet();
		int counter = 0;
		for (Place place : places) {
			PlaceIcon placeIcon = hashMap.get(place);
			if (placeIcon.isSelected() == true) {
				counter++;
			}
		}
		if (counter == 1) {
			System.out.println(counter);
			return false;

		}
		return true;

	}

	public class StartPlace extends JComponent {
		private Color color;

		public StartPlace(Color color) {
			// TODO Auto-generated constructor stub
			this.setBounds(0, 0, 100, 100);
			this.setVisible(true);
			this.color = color;

		}

		@Override
		protected void paintComponent(Graphics g) {
			// TODO Auto-generated method stub
			super.paintComponent(g);
			Graphics2D gg = (Graphics2D) g;
			gg.setFont(new Font("Times New Roman", Font.PLAIN, 16));
			gg.setColor(this.color);
			gg.setStroke(new BasicStroke(4));
			if (gg.getColor() == Color.RED) {
				gg.drawString("START", 40, 30);
			} else {
				gg.drawString("END", 40, 30);
			}
			gg.drawRect(40, 10, 100, 100);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					MapEditor mapFrame = new MapEditor();
					mapFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		});
	}

}

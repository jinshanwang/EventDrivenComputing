import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class Display implements Gui {

	private Controller clr;
	private JButton pressGOBtn = new JButton("Go/Stop");
	private JButton insertBtn = new JButton("Insert");
	private JTextField messsegeBox = new JTextField();

	@Override
	public void connect(Controller controller) {
		this.clr = controller;
	}

	@Override
	public void init() {



		Font textFont = new Font("SansSerif", Font.BOLD, 50);
		this.messsegeBox.setEditable(false);
		this.messsegeBox.setHorizontalAlignment(JTextField.CENTER);
		this.messsegeBox.setFont(textFont);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				// 1. create a window
				JFrame window = new JFrame("SRC");
				window.setBounds(100, 100, 500, 500);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				// 2. set content
				Container container = window.getContentPane();
				container.setLayout(null);
				insertBtn.setBounds(100, 20, 100, 40);
				pressGOBtn.setBounds(300, 20, 100, 40);
				messsegeBox.setBounds(50, 100, 400, 300);
				container.add(insertBtn);
				container.add(pressGOBtn);

				container.add(messsegeBox);

				// 3. show the window
				window.setVisible(true);
			}
		});

		this.insertBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clr.coinInserted();
			}

		});
		this.pressGOBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clr.goStopPressed();

			}

		});
	}

	@Override
	public void setDisplay(String s) {
		this.messsegeBox.setText(s);
	}

}

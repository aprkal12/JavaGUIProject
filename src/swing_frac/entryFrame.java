package swing_frac;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class entryFrame extends JPanel{
	Image img = new ImageIcon(entryFrame.class.getResource("../images/entry.png")).getImage();
	public String a;
	entryScreen setNickname = new entryScreen();
	public entryFrame() {
		setBounds(0, 0, 785, 563);
		setVisible(true);
		setLayout(null);

		add(setNickname);

	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
	}
	public static void main(String[] args) {
		new entryFrame();
	}
}

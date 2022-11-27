package swing_frac;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.*;

public class entryFrame extends JPanel{
	Image img = new ImageIcon(Objects.requireNonNull(entryFrame.class.getResource("../images/entryBack.png"))).getImage();
	// Image img = new ImageIcon(entryFrame.class.getResource("../images/entry.png")).getImage();
	//entryScreen setNickname = new entryScreen();
	public entryFrame() {
		setBounds(0, 0, 785, 563);
		setVisible(true);
		setLayout(null);
		//add(setNickname);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
	}
}

package swing_frac;

import java.awt.*;
import java.util.Objects;

import javax.swing.*;

public class entryFrame extends JPanel{
	Image img = new ImageIcon(Objects.requireNonNull(entryFrame.class.getResource("../images/entryBack.png"))).getImage();
	public entryFrame() {
		setBounds(0, 0, 785, 563);
		setVisible(true);
		setLayout(null);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
	}
}

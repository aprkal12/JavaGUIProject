package swing_frac;

import java.awt.*;
import javax.swing.*;

public class player extends JPanel{
	Image img = new ImageIcon(player.class.getResource("../images/mychar.png")).getImage();
	public player(){
		setBounds(300, 200, 45, 55);
		setBackground(Color.black);
		setLayout(null);
		setVisible(true);
		//setFocusable(true); // 키 이벤트를 이 패널에 적용함
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
	}
	public static void main(String []args) {
		new player();
	}
}

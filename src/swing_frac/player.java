package swing_frac;

import java.awt.*;
import javax.swing.*;

public class player extends JPanel{
	private final int playerWidth = 45, playerHeight = 55;
	Image img = new ImageIcon(player.class.getResource("../images/mychar.png")).getImage();
	public player(){
		setBounds(300, 200, 150, 150);
		setBackground(Color.WHITE);
		setLayout(null);
		setVisible(true);
		//setFocusable(true); // 키 이벤트를 이 패널에 적용함
	}
	public void paintComponent(Graphics g) {
		g.drawImage(img, 0, 20, playerWidth, playerHeight, this);
	}
	public static void main(String []args) {
		new player();
	}
}
class playerNickname extends JLabel{
	public playerNickname(){
		setBackground(Color.BLACK);
	}
}

package swing_frac;

import java.awt.*;
import java.util.Objects;
import javax.swing.*;

public class player extends JPanel{
	private JLabel nickname;
	Image img = new ImageIcon(Objects.requireNonNull(player.class.getResource("../images/mychar.png"))).getImage();
	//Image img = new ImageIcon(player.class.getResource("../images/mychar.png")).getImage();
	public player(){
		nickname = new JLabel();
		nickname.setFont(new Font("Consolas 굵게", Font.BOLD, 15));
		nickname.setBounds(0, 0, 200, 20);
		nickname.setBackground(Color.WHITE);
		nickname.setForeground(Color.WHITE);
		setBounds(300, 200, 150, 150);
		setBackground(Color.WHITE);
		setLayout(null);
		setVisible(true);
		add(nickname);
		//setFocusable(true); // 키 이벤트를 이 패널에 적용함
	}
	public void setPlayerNickname(String nickname){
		this.nickname.setText(nickname);
	}
	public String getName(){ return this.nickname.getText();}
	public void paintComponent(Graphics g) {
		int playerWidth = 45;
		int playerHeight = 55;
		g.drawImage(img, 0, 20, playerWidth, playerHeight, this);
	}
}

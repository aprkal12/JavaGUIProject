package swing_frac;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class entryScreen extends JPanel{
	public JTextField textField = new JTextField();
	private JLabel nick_label= new JLabel("닉네임");
	private JButton button  = new JButton("입장");
	private String nickname="";
	public objectSettings objSet = new objectSettings();
	//public Thread th1;
	//Image img = new ImageIcon(entryScreen.class.getResource("../images/entry.png")).getImage();

	public entryScreen() { //닉네임창 배경 (entryFrame 위)

		setBounds(500, 400, 270, 150); //텍스트 배경 크기 위치
		setLayout(null); // 레이아웃
		setBackground(Color.white);
		setVisible(true); //화면에 출력
		componentSet();
		//eventSet();
	}
	public void componentSet() { // 닉네임 입력 창 (entryScreen 위)
		Font font1 = new Font("맑은 고딕", Font.BOLD,25);
		nick_label.setBounds(100, 25, 150, 30);
		textField.setBounds(65, 60, 150, 40);
		//button.setBounds(115, 100, 70, 30);
		textField.setFont(font1);
		nick_label.setFont(font1);
		add(nick_label);
		add(textField);
		add(button);
	}
	public String getNickname() {
		return this.nickname;
	}
	public JTextField getTextField() { return this.textField;}
}
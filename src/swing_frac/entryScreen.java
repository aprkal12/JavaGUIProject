package swing_frac;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class entryScreen extends JPanel{
	public JTextField textField = new JTextField();
	private JLabel label = new JLabel("닉네임");
	private JButton button  = new JButton("go");
	private String nickname="";
	public objectSettings objSet = new objectSettings();
	//public Thread th1;
	//Image img = new ImageIcon(entryScreen.class.getResource("../images/entry.png")).getImage();
	
	public entryScreen() {
		setBounds(250, 120, 300, 300);
		setBackground(Color.WHITE);
		setVisible(true);
		setLayout(null);
		
		componentSet();
		//eventSet();
	}
	public void componentSet() {
		Font font1 = objSet.getFont1();
		textField.setBounds(75, 110, 150, 30);
		label.setBounds(120, 65, 150, 30);
		//button.setBounds(240, 110, 50, 30);
		textField.setFont(font1);
		label.setFont(font1);
		add(textField);
		add(label);
		//add(button);
	}
	public String getNickname() {
		return this.nickname;
	}
	public JTextField getTextField() { return this.textField;}
}

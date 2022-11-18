package swing_frac;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class entryScreen extends JPanel{
	public JTextField t1 = new JTextField();
	private JLabel l1 = new JLabel("닉네임");
	private JButton b1 = new JButton("go");
	public String nickname="";
	public objectSettings objset = new objectSettings();
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
		Font f1 = objset.getFont1();
		t1.setBounds(75, 110, 150, 30);
		l1.setBounds(120, 65, 150, 30);
		//b1.setBounds(240, 110, 50, 30);
		t1.setFont(f1);
		l1.setFont(f1);
		add(t1);
		add(l1);
		//add(b1);
	}
	public void eventSet() {
		//t1.addKeyListener(new KeyAdapter(){
		//	@Override
		//	public void keyPressed(KeyEvent e) {
		//		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
		//			//th1.interrupt();
		//			nickname = t1.getText();
		//			JOptionPane.showMessageDialog(null, nickname);
		//		}
		//	}
		//});
	}
	public String getNickname() {
		return nickname;
	}
	public static void main(String[] args) {
		new entryScreen();
	}
}

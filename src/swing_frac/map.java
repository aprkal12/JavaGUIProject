package swing_frac;

import java.awt.*;
import javax.swing.*;

public class map extends JPanel{
	static private int Width = 800, Height = 600;
	private JLabel la = new JLabel();
	public objectSettings objset = new objectSettings();
	private Font f1 = objset.getFont1();
	public map() {
		la.setFont(f1);
		la.setBounds(320, 20, 300, 100);
		la.setBackground(Color.BLACK);
		setBounds(0, 0, Width, Height);
		setLayout(null);
		add(la);
	}
	public void setLabeltext(String context){
		la.setText(context);
	}
	public static void main(String[] args) {
		new map();
	}
}

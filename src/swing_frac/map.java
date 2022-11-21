package swing_frac;

import java.awt.*;
import javax.swing.*;

public class map extends JPanel{
	static private int Width = 800, Height = 600;
	private JLabel label = new JLabel();
	public objectSettings objSet = new objectSettings();
	private Font font1 = objSet.getFont1();
	public map() {
		label.setFont(font1);
		label.setBounds(320, 20, 300, 100);
		label.setBackground(Color.BLACK);
		//label.setOpaque(true);
		setBounds(0, 0, Width, Height);
		setLayout(null);
		add(label);
	}
	public void setLabeltext(String context){
		label.setText(context);
	}
}

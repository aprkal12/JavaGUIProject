package swing_frac;

import java.awt.*;
import java.util.Objects;
import javax.swing.*;

public class map extends JPanel{
	static private int Width = 800, Height = 600;
	private JLabel label = new JLabel();
	public objectSettings objSet = new objectSettings();
	private Font font1 = objSet.getFont1();
	String imgName;
	Image img;
	public map(String imgName) {
		this.imgName = imgName;
		setBounds(0, 0, Width, Height);
		setLayout(null);
		img = new ImageIcon(Objects.requireNonNull(map.class.getResource("../images/"+imgName+".png"))).getImage();

	}

	public void setLabeltext(String context){
		label.setText(context);
	}
	public void paintComponent(Graphics g){ g.drawImage(img, 0, 0, getWidth(), getHeight(), this);}
}

package swing_frac;

import java.awt.*;
import javax.swing.*;

public class map extends JPanel{
	static int Width = 800, Height = 600;
	public map() {
		setBounds(0, 0, Width, Height);
		setLayout(null);
	}
	public static void main(String[] args) {
		new map();
	}
}

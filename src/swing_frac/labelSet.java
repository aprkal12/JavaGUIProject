package swing_frac;

import javax.swing.*;
import java.awt.*;

public class labelSet extends JLabel {
    public objectSettings objset = new objectSettings();
    Font f1 = objset.getFont1();
    public labelSet(){
        setFont(f1);
        setBounds(320, 20, 300, 100);
        setBackground(Color.BLACK);
    }
    public static void main(String [] args){
        new labelSet();
    }
}

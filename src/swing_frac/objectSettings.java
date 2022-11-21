package swing_frac;

import javax.swing.*;
import java.awt.*;

public class objectSettings {
    private Font font1;
    private Font font2;
    private JLabel label;
    public objectSettings(){
        this.font1 = new Font("Consolas 굵게", Font.BOLD, 20);
        this.font2 = new Font("Consolas 굵게", Font.BOLD, 10);
        this.label = new JLabel();
        this.label.setBackground(Color.BLACK);
        this.label.setFont(font1);
    }
    public Font getFont1(){
        return this.font1;
    }
    public Font getFont2(){
        return this.font2;
    }

    public JLabel getLabel(){
        return this.label;
    }
    public void setLabelText(String text){
        this.label.setText(text);
    }
    public void setLabelBounds(int x, int y, int width, int height){
        this.label.setBounds(x, y, width, height);
    }
}

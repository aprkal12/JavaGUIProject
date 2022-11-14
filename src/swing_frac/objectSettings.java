package swing_frac;

import javax.swing.*;
import java.awt.*;

public class objectSettings {
    private Font f1;
    private Font f2;
    private JLabel label;
    public objectSettings(){
        this.f1 = new Font("Consolas 굵게", Font.BOLD, 20);
        this.f2 = new Font("Consolas 굵게", Font.BOLD, 10);
        this.label = new JLabel();
        this.label.setBackground(Color.BLACK);
        this.label.setFont(f1);
    }
    public Font getFont1(){
        return this.f1;
    }
    public Font getFont2(){
        return this.f2;
    }

    public JLabel getLabel(){
        return this.label;
    }
    public void setLabeltext(String text){
        this.label.setText(text);
    }
    public void setLabelbounds(int x, int y, int width, int height){
        this.label.setBounds(x, y, width, height);
    }
    public static void main(String [] args){
        new objectSettings();
    }
}

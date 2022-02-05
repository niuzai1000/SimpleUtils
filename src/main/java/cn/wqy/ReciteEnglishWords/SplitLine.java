package cn.wqy.ReciteEnglishWords;

import cn.wqy.SwingUtils.MySwingUtils;

import javax.swing.*;
import java.awt.*;

public class SplitLine extends JPanel {

    private static final double SPACE_WIDTH = 3.8349609375D;

    private final Dimension size = new Dimension(380 , 30);

    private final JLabel infoLabel = new JLabel();

    private final String info;



    public SplitLine(String info) {
        super(new FlowLayout(FlowLayout.LEFT));
        this.info = info;
        this.add(infoLabel);
        drawString();
    }

    private void drawString(){
        StringBuilder text = new StringBuilder();
        for (int i = 0 ; i < (int)(55 / SPACE_WIDTH) ; i++) text.append(' ');
        text.append(info);
        infoLabel.setText(text.toString());
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawLine(g);
    }

    private void drawLine(Graphics g){
        int strLength = MySwingUtils.getStringWidth(MySwingUtils.DEFAULT_FONT , info);
        g.setColor(new Color(1 , 1 , 1, 74));
        g.drawLine(30 , 12 , 55 , 12);
        g.drawLine(65 + strLength , 12 , 350 , 12);
    }


    @Override
    public Dimension getPreferredSize() {
        return size;
    }
}

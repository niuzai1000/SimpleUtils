package cn.wqy.BatchModifyFiles;

import javax.swing.*;
import java.awt.*;

public class SplitLine extends JPanel {
    private final Dimension dimension = new Dimension(740 , 30);

    private final FlowLayout flowLayout = new FlowLayout();

    @Override
    public Dimension getPreferredSize() {
        return dimension;
    }

    @Override
    public LayoutManager getLayout() {
        return flowLayout;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(1 , 1 , 1, 74));
        g.drawLine(30 , 15 , 710 , 15);
    }
}

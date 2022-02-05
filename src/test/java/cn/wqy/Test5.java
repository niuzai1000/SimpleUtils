package cn.wqy;

import cn.wqy.SwingUtils.MySwingUtils;

import javax.swing.*;
import java.awt.*;

public class Test5 {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(280 , 166));
        Thread thread = new Thread(() -> {
            while (!frame.isVisible()) Thread.yield();
            frame.setVisible(false);
        });
        thread.start();
        MySwingUtils.setCenterAndVisible(frame);

    }
}

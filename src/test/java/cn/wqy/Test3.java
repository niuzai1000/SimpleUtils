package cn.wqy;

import cn.wqy.SwingUtils.MySwingUtils;

import javax.swing.*;

public class Test3 {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        JFrame jFrame = new JFrame("utils");
        JButton btn = new JButton("begin");
        MySwingUtils.defaultJFrameSetting(jFrame);
        MySwingUtils.setCenterAndVisible(jFrame);
        jFrame.add(btn);
        btn.addActionListener(e -> {
            InformationDialog.INFO_DIALOG.showInfo("visible" , () -> {
                System.out.println("showInfo conduct finish");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                System.out.println("sleep conduct finish");
                System.exit(0);
            });
        });
//        InformationDialog.INFO_DIALOG.showInfo("visible");
//        System.out.println("showInfo conduct finish");
//        try {
//            Thread.sleep(1000000);
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }
//        System.out.println("sleep conduct finish");
//        InformationDialog.INFO_DIALOG.hideInfo();
//        System.exit(0);

    }
}

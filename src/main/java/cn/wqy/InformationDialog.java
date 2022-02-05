package cn.wqy;

import cn.wqy.SwingUtils.MySwingUtils;

import javax.swing.*;
import java.awt.*;

public class InformationDialog extends JDialog {

    public static final InformationDialog INFO_DIALOG = new InformationDialog();

    private final JLabel infoLabel = new JLabel();


    public InformationDialog() {
        super((Dialog) null, "运行信息",  true);
        try {
            this.setLayout(new BorderLayout());
            JPanel infoPanel = new JPanel();
            infoPanel.add(infoLabel);
            this.add(infoPanel , BorderLayout.CENTER);
            MySwingUtils.defaultJDialogSetting(this);
            MySwingUtils.setFont(infoLabel , Font.PLAIN , 14);
            this.setAlwaysOnTop(true);
            this.setSize(new Dimension(280 , 160));
            this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注：该方法会阻塞线程，该线程其他活动必须交给另一个线程处理，并且另一个线程必须执行hideInfo()方法
     * 注：在catch代码块中不能有printStackTrace()方法
     * @param info 需要发表的信息
     */
    public void showInfo(String info , Task task){
        Thread taskThread = new Thread(() -> {
            try {
                // 确保对话框加载完成，而不会出现对话框无法关闭的bug
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            infoLabel.setText(info);
            task.conduct();
            if (this.isVisible()) hideInfo();
        });
        taskThread.start();
        MySwingUtils.setCenterAndVisible(this);
    }

    public void setInfo(String info){
        infoLabel.setText(info);
        this.repaint();
        this.validate();
        this.invalidate();
        this.validate();
    }

    /**
     * 必须在程序结尾才能使用，否则可能有bug
     */
    public void hideInfo(){
        this.setVisible(false);
    }


}

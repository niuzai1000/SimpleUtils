package cn.wqy;


import cn.wqy.BatchModifyFiles.BatchModifyFilesDialogManager;
import cn.wqy.ReciteEnglishWords.ReciteEnglishWordsDialogManager;
import cn.wqy.SwingUtils.MySwingUtils;

import javax.swing.*;
import java.awt.*;

public class OpenWindow {
    public static JFrame window = new JFrame("简单工具");





    public static JButton fileProcessBtn = new JButton("批量文件处理");

    public static JButton reciteEnglishWordsBtn = new JButton("背诵英语单词");


    public static void main(String[] args) {
        otherSettings();
        addComponents();
        setListener();
        try {
            MySwingUtils.defaultJFrameSetting(window);
            MySwingUtils.setCenterAndVisible(window);
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void otherSettings(){
        window.setLayout(new FlowLayout());
    }

    public static void addComponents(){
        MySwingUtils.add(window, fileProcessBtn , reciteEnglishWordsBtn);
    }

    public static void setListener(){
        fileProcessBtn.addActionListener(e -> new BatchModifyFilesDialogManager(window).openDialog());
        reciteEnglishWordsBtn.addActionListener(e -> new ReciteEnglishWordsDialogManager(window).openDialog());
    }

}

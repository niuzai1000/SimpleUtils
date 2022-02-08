package cn.wqy.ReciteEnglishWords;

import cn.wqy.OpenWindow;
import cn.wqy.SwingUtils.MySwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static java.awt.Dialog.ModalityType.DOCUMENT_MODAL;

public class ReciteEnglishWordsDialogManager{

    public final JDialog dialog;

    public final Box box = Box.createVerticalBox();





    public final JPanel btnPanel = new JPanel();

    public final JButton editWordBtn = new JButton("编辑单词");

    public final JButton traverseWordBtn = new JButton("遍历单词");

    public final JButton reciteWordBtn = new JButton("背诵单词");

    public final JButton importBtn = new JButton("导入单词");

    public final JButton settingBtn = new JButton("设置");





    public final WordPanelManager wordPanelManager;




    public ReciteEnglishWordsDialogManager(JFrame jFrame) {
        dialog = new JDialog(jFrame , "背诵英语单词工具" , DOCUMENT_MODAL);
        wordPanelManager = new WordPanelManager(dialog);
    }

    public void openDialog(){
        otherSettings();
        addComponents();
        setListener();
        setPreferredSize();
        try {
            MySwingUtils.defaultJDialogSetting(dialog);
            MySwingUtils.setCenterAndVisible(dialog);
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void otherSettings() {
        dialog.setLayout(new FlowLayout());
        btnPanel.setLayout(new FlowLayout());
    }

    protected void addComponents() {
        MySwingUtils.add(dialog , box);
        MySwingUtils.add(box , btnPanel);
        MySwingUtils.add(btnPanel , editWordBtn , traverseWordBtn , reciteWordBtn , importBtn , settingBtn);
    }

    protected void setListener() {
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    File parentFile = new File(OpenWindow.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
                    File temp = new File(parentFile.getAbsoluteFile() + "\\temp");
                    File[] tempFiles = temp.listFiles();
                    if (tempFiles != null) for (File file : tempFiles) if (!file.delete()) throw new IOException("无法删除文件");
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });

        editWordBtn.addActionListener(e -> wordPanelManager.editWords());

        traverseWordBtn.addActionListener(e -> wordPanelManager.traverseWords());

        reciteWordBtn.addActionListener(e -> wordPanelManager.reciteWords());

        importBtn.addActionListener(e -> wordPanelManager.importWords());

        settingBtn.addActionListener(e -> wordPanelManager.setting());


    }

    protected void setPreferredSize() {
        btnPanel.setPreferredSize(new Dimension(740 , 30));

    }


}

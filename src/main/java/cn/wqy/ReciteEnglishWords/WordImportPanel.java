package cn.wqy.ReciteEnglishWords;

import cn.wqy.InformationDialog;
import cn.wqy.SwingUtils.MySwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import static javax.swing.JFileChooser.FILES_ONLY;

public class WordImportPanel extends JPanel{

    private final JDialog window;

    private boolean legal = false;

    private File importFile = null;





    private final Box importDialogBox = Box.createVerticalBox();





    private final JPanel labelPanel = new JPanel();

    private final JLabel label = new JLabel("   文件路径：");





    private final JPanel textFieldPanel = new JPanel();

    private final JTextField textField = new JTextField(24);

    private final JButton pathChooseBtn = new JButton("...");





    private final JPanel btnPanel = new JPanel();

    private final JButton btn = new JButton("确定");





    public WordImportPanel(JDialog window) {
        this.window = window;
        init();
    }

    private void init(){
        firstSettings();
        addComponents();
        setListener();
        setPreferredSize();
        otherSettings();
    }

    private void firstSettings(){
        labelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    private void addComponents(){
        MySwingUtils.add(this , importDialogBox);
        MySwingUtils.add(importDialogBox , labelPanel , textFieldPanel , btnPanel);
        MySwingUtils.add(labelPanel , label);
        MySwingUtils.add(textFieldPanel , textField , pathChooseBtn);
        MySwingUtils.add(btnPanel , btn);
    }

    private void setListener(){
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                legal = false;
            }

            @Override
            public void windowClosing(WindowEvent e) {
                legal = false;
            }
        });
        pathChooseBtn.addActionListener(e -> {
            File selectedFile = new File(textField.getText());
            if (!selectedFile.exists()) selectedFile = new File(System.getProperty("java.class.path").split(";")[0]).getParentFile();
            JFileChooser chooser = new JFileChooser(selectedFile);
            MySwingUtils.setDefaultFonts(chooser);
            chooser.setFileSelectionMode(FILES_ONLY);
            chooser.showOpenDialog(window);
            File file = chooser.getSelectedFile();
            if (file != null && file.exists()){
                textField.setText(file.toString());
            }
        });
        btn.addActionListener(e -> {
            importFile = new File(textField.getText().trim());
            if (!importFile.exists()) {
                InformationDialog.INFO_DIALOG.showInfo("导入文件不存在", () -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                });
                return;
            }
            if (!importFile.getName().endsWith(".txt")) {
                InformationDialog.INFO_DIALOG.showInfo("文件应该以txt为拓展名", () -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                });
                return;
            }
            legal = true;
            window.setVisible(false);
        });
    }

    private void setPreferredSize(){
        importDialogBox.setPreferredSize(new Dimension(380 , importDialogBox.getComponents().length * 30));
        pathChooseBtn.setPreferredSize(new Dimension(20 , 20));
    }

    private void otherSettings(){
        MySwingUtils.setDefaultFonts(this);
    }

    public boolean isLegal(){
        return legal;
    }

    public File getFile(){
        return importFile;
    }

}

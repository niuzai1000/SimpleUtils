package cn.wqy.ReciteEnglishWords;

import cn.wqy.IOUtils.MyIOUtils;
import cn.wqy.InformationDialog;
import cn.wqy.SwingUtils.MySwingUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static javax.swing.JFileChooser.FILES_ONLY;

public class WordImportPanel extends JPanel{

    private final JDialog window;

    private final ArrayList<Word> words;





    private final Box importDialogBox = Box.createVerticalBox();





    private final JPanel labelPanel = new JPanel();

    private final JLabel label = new JLabel("   文件路径：");





    private final JPanel textFieldPanel = new JPanel();

    private final JTextField textField = new JTextField(24);

    private final JButton pathChooseBtn = new JButton("...");





    private final JPanel btnPanel = new JPanel();

    private final JButton btn = new JButton("确定");





    public WordImportPanel(JDialog window , ArrayList<Word> words) {
        this.window = window;
        this.words = words;
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
        pathChooseBtn.addActionListener(e -> {
            File selectedFile = new File(textField.getText());
            if (!selectedFile.exists()) selectedFile = new File(System.getProperty("java.class.path").split(";")[0]).getParentFile();
            JFileChooser chooser = new JFileChooser(selectedFile);
            MySwingUtils.setDefaultFonts(chooser);
            chooser.setFileSelectionMode(FILES_ONLY);
            chooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(".txt") || f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "txt文件(*.txt)";
                }
            });
            chooser.showOpenDialog(window);
            File file = chooser.getSelectedFile();
            if (file != null && file.exists()){
                textField.setText(file.toString());
            }
        });
        btn.addActionListener(e -> {
            File importFile = new File(textField.getText().trim());
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
            InformationDialog.INFO_DIALOG.showInfo("正在导入单词..." , () -> {
                ArrayList<Word> words = new ArrayList<>();
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(importFile);
                    List<String> content = Files.readAllLines(Paths.get(importFile.toURI()) , StandardCharsets.UTF_8);
                    Pattern pattern = Pattern.compile("^\\s*[a-zA-Z][a-zA-Z\\s.'()/]*=[\\u4E00-\\u9FA5A-Za-z0-9;.,\\s()/]+$");
                    for (String lineString : content){
                        if (pattern.matcher(lineString).matches()){
                            String word = lineString.split("=")[0];
                            ArrayList<String> translation = new ArrayList<>(Arrays.asList(lineString.split("=")[1].split(";")));
                            InformationDialog.INFO_DIALOG.setInfo("正在导入 " + word + " 中");
                            SearchResult result = Search.search(word , true);
                            words.add(new Word(word , translation , result.getUk_speech_File() , result.getUs_speech_File()));
                        }
                    }
                    this.words.clear();
                    this.words.addAll(words);
                    InformationDialog.INFO_DIALOG.setInfo("导入成功");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    InformationDialog.INFO_DIALOG.setInfo("导入失败，可能是因为不是UTF-8编码");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                } finally {
                    try {
                        MyIOUtils.close(fis);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
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

}

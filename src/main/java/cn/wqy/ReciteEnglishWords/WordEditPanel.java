package cn.wqy.ReciteEnglishWords;

import cn.wqy.InformationDialog;
import cn.wqy.SwingUtils.MySwingUtils;
import cn.wqy.Task;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class WordEditPanel extends JPanel{

    private final JDialog window;

    private final ArrayList<WordEditPanel> wordEditPanels;

    private final ArrayList<Word> words;

    private int index;

    private Word word;

    private boolean isSaved = false;

    private final Box editDialogBox = Box.createVerticalBox();





    private final JPanel wordTextFieldPanel = new JPanel();

    private final JTextField wordTextField = new JTextField(8);

    private final JButton searchBtn = new JButton("搜索");

    private SearchResult result = null;





    private final JPanel pronunciationEnglishPanel = new JPanel();

    private final JButton pronunciationEnglishBtn = new JButton("英");

    private final JLabel pronunciationEnglishLabel = new JLabel();





    private final JPanel pronunciationAmericanPanel = new JPanel();

    private final JButton pronunciationAmericanBtn = new JButton("美");

    private final JLabel pronunciationAmericanLabel = new JLabel();





    private final JPanel officialExplainOrExplainPanel = new JPanel();

    private final ButtonGroup explainButtonGroup = new ButtonGroup();

    private final JRadioButton explainRadioBtn = new JRadioButton("选用自定义翻译");

    private final JRadioButton officialExplainRadioBtn = new JRadioButton("选用官方翻译");

    {
        if (Config.isSelfType()) {
            explainRadioBtn.setSelected(true);
        }
        else {
            officialExplainRadioBtn.setSelected(true);
        }
    }





    private final JPanel explainSplitLine = new SplitLine("自定义翻译");





    private final JPanel explainTextFieldPanel = new JPanel();

    private final JTextField explainTextField = new JTextField(24);





    private final JPanel officialExplainSplitLine = new SplitLine("官方翻译");





    private final JPanel officialExplainTextAreaPanel = new JPanel();

    private final JTextArea officialExplainTextArea = new JTextArea(6 , 24);

    private final JScrollPane officialExplainScrollPane = new JScrollPane(officialExplainTextArea);





    private final JPanel savePanel = new JPanel();

    private final JButton saveBtn = new JButton("保存");





    private final JPanel bottomPanel = new JPanel();

    private final JButton previousBtn = new JButton("上一个");

    private final JTextField pageTextField = new JTextField(2);

    private final JLabel maxPageLabel = new JLabel();

    private int maxPageNumber = 1;

    private final JButton nextBtn = new JButton("下一个");

    private final JButton finishBtn = new JButton("完成");

    private final JButton deleteBtn = new JButton("删除");





    public WordEditPanel(JDialog window, ArrayList<WordEditPanel> wordEditPanels , ArrayList<Word> words , int index) {
        this.window = window;
        this.wordEditPanels = wordEditPanels;
        this.words = words;
        this.index = index;
        init();
    }

    private void init(){
        firstSettings();
        addComponents();
        setListener();
        otherSettings();
        setPreferredSize();
    }

    private void firstSettings(){
        this.setLayout(new BorderLayout());
    }

    private void otherSettings(){
        MySwingUtils.setDefaultFonts(this);
        pronunciationEnglishLabel.setFont(new Font("Consolas" , Font.PLAIN , 14));
        pronunciationAmericanLabel.setFont(new Font("Consolas" , Font.PLAIN , 14));
        pageTextField.setEditable(false);
        officialExplainTextArea.setEditable(false);
        saveBtn.setToolTipText("新建单词时自动保存");
        updatePageNumber();
    }

    private void addComponents() {
        MySwingUtils.add(this , editDialogBox);
        MySwingUtils.add(editDialogBox , wordTextFieldPanel , pronunciationEnglishPanel, pronunciationAmericanPanel , officialExplainOrExplainPanel , explainSplitLine , explainTextFieldPanel , officialExplainSplitLine , officialExplainTextAreaPanel , savePanel);
        MySwingUtils.add(wordTextFieldPanel , wordTextField , searchBtn);
        MySwingUtils.add(pronunciationEnglishPanel , pronunciationEnglishBtn , pronunciationEnglishLabel);
        MySwingUtils.add(pronunciationAmericanPanel , pronunciationAmericanBtn , pronunciationAmericanLabel);
        MySwingUtils.add(officialExplainOrExplainPanel , explainRadioBtn, officialExplainRadioBtn);
        MySwingUtils.add(explainButtonGroup , explainRadioBtn, officialExplainRadioBtn);
        MySwingUtils.add(explainTextFieldPanel , explainTextField);
        MySwingUtils.add(officialExplainTextAreaPanel , officialExplainScrollPane);
        MySwingUtils.add(savePanel, saveBtn);

        //special add
        this.add(bottomPanel , BorderLayout.SOUTH);
        MySwingUtils.add(bottomPanel , deleteBtn , previousBtn , pageTextField , maxPageLabel , nextBtn , finishBtn);



    }

    private void setListener(){
        searchBtn.addActionListener(e -> {
            if (!"".equals(wordTextField.getText().trim())) {
                InformationDialog.INFO_DIALOG.showInfo("搜索中..." , () -> {
                    try {
                        result = Search.search(wordTextField.getText().trim() , Config.isFreeAPI());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (result == null) return;
                    update();
                });
            }
        });

        pronunciationEnglishBtn.addActionListener(e -> {
            if (result != null){
                AudioPlayer.play(result.getUk_speech_File());
            }
        });

        pronunciationAmericanBtn.addActionListener(e -> {
            if (result != null){
                AudioPlayer.play(result.getUs_speech_File());
            }
        });

        previousBtn.addActionListener(e -> {
            if (index != 0) editPrevious();
            else if ("".equals(wordTextField.getText().trim())) InformationDialog.INFO_DIALOG.showInfo("请输入单词" , () -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
            else InformationDialog.INFO_DIALOG.showInfo("之前没有单词了" , () -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
        });

        nextBtn.addActionListener(e -> {
            if (!isSaved){
                save(() -> {
                    editNext();
                    isSaved = true;
                });
            }else editNext();
        });

        deleteBtn.addActionListener(e -> deleteWordEditPanel());

        finishBtn.addActionListener(e -> finishWordEditPanel());

        saveBtn.addActionListener(e -> save(() -> isSaved = true));



    }

    private void setPreferredSize(){
        editDialogBox.setPreferredSize(new Dimension(380 , editDialogBox.getComponents().length * 30 + 120));
        wordTextFieldPanel.setPreferredSize(new Dimension(380 , 30));
        pronunciationEnglishPanel.setPreferredSize(new Dimension(380 , 30));
        explainTextFieldPanel.setPreferredSize(new Dimension(380 , 30));
        officialExplainTextAreaPanel.setPreferredSize(new Dimension(380 , 200));

    }

    private void save(Task task){
        if (!"".equals(wordTextField.getText().trim())){
            if (officialExplainRadioBtn.isSelected()){
                if (result != null && result.getErrorCode() == 0){
                    word = new Word(wordTextField.getText().trim() , result);
                    if (task != null) task.conduct();
                }else{
                    InformationDialog.INFO_DIALOG.showInfo("请搜索并搜索成功再添加单词" , () -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            }else if (explainRadioBtn.isSelected()){
                if (!"".equals(explainTextField.getText().trim())){
                    InformationDialog.INFO_DIALOG.showInfo("正在搜索音频中..." , () -> {
                        try {
                            this.result = Search.search(wordTextField.getText().trim() , true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        word = new Word(wordTextField.getText().trim() , new ArrayList<>(Arrays.asList(explainTextField.getText().trim().split(";"))) , result.getUk_speech_File() , result.getUs_speech_File());
                        if (task != null) task.conduct();
                    });
                }else InformationDialog.INFO_DIALOG.showInfo("请输入自定义翻译" , () -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        }else {
            InformationDialog.INFO_DIALOG.showInfo("请输入单词" , () -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        update();
    }

    private void update(){
        pronunciationEnglishLabel.setText("");
        pronunciationAmericanLabel.setText("");
        officialExplainTextArea.setText("");
        if (result == null) return;
        ArrayList<String> translation = result.getTranslation();
        ArrayList<String> explains = result.getExplains();
        StringBuilder builder = new StringBuilder();
        if (translation != null) {
            builder.append("翻译：");
            builder.append("\n");
            for (String trans : translation) {
                builder.append("        ");
                builder.append(trans);
                builder.append(";");
            }
            builder.deleteCharAt(builder.toString().length() - 1);
            officialExplainTextArea.setText(builder.toString());
        }
        officialExplainTextArea.append("\n");
        if (explains != null){
            officialExplainTextArea.append("解释：");
            officialExplainTextArea.append("\n");
            for (String exp : explains){
                officialExplainTextArea.append("        ");
                officialExplainTextArea.append(exp);
                officialExplainTextArea.append("\n");
            }
        }
        if (result.getUk_phonetic() != null) pronunciationEnglishLabel.setText('/' + result.getUk_phonetic() + '/');
        if (result.getUs_phonetic() != null) pronunciationAmericanLabel.setText('/' + result.getUs_phonetic() + '/');
    }

    private void updatePageNumber(){
        pageTextField.setText(String.valueOf(index + 1));
        maxPageLabel.setText(" / " + maxPageNumber);
    }

    private void editNext(){
        while (index + 1 > wordEditPanels.size() - 1){
            wordEditPanels.add(new WordEditPanel(window, wordEditPanels , words , wordEditPanels.size()));
        }
        for (WordEditPanel panel : wordEditPanels) {
            panel.maxPageNumber = wordEditPanels.size();
            panel.updatePageNumber();
        }
        window.remove(wordEditPanels.get(index));
        window.add(wordEditPanels.get(index + 1));
        window.repaint();
        window.validate();
        window.invalidate();
        window.validate();
    }

    private void editPrevious(){
        window.remove(wordEditPanels.get(index));
        window.add(wordEditPanels.get(index - 1));
        window.repaint();
        window.validate();
        window.invalidate();
        window.validate();
    }

    private void deleteWordEditPanel(){
        if (wordEditPanels.size() <= 1) return;
        window.remove(wordEditPanels.get(index));
        wordEditPanels.remove(index);
        if (index != wordEditPanels.size()){
            for (int i = index ; i < wordEditPanels.size() ; i++){
                wordEditPanels.get(i).index -= 1;
            }
            window.add(wordEditPanels.get(index));
        } else {
            window.add(wordEditPanels.get(wordEditPanels.size() - 1));
        }
        for (WordEditPanel panel : wordEditPanels) {
            panel.maxPageNumber = wordEditPanels.size();
            panel.updatePageNumber();
        }
        window.repaint();
        window.validate();
        window.invalidate();
        window.validate();
    }

    private void finishWordEditPanel(){
        AtomicBoolean legal = new AtomicBoolean(false);
        WordEditPanel wordEditPanel =  wordEditPanels.get(wordEditPanels.size() - 1);
        if (!wordEditPanel.isSaved) wordEditPanel.save(() -> legal.set(true));
        else legal.set(true);
        if (legal.get()){
            words.clear();
            for (WordEditPanel panel : wordEditPanels) words.add(panel.word);
            window.setVisible(false);
        }
    }
}

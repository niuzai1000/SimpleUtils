package cn.wqy.ReciteEnglishWords;

import cn.wqy.InformationDialog;
import cn.wqy.SwingUtils.MySwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class WordShowPanel extends JPanel {

    private final JDialog window;

    private final Word word;

    private final boolean isTraverse;

    private final int panelIndex;

    private final int orderIndex;

    private final ArrayList<Integer> order;

    private final ArrayList<WordShowPanel> wordShowPanels;

    private final JPanel topPanel = new JPanel();

    private final Box showWordBox = Box.createVerticalBox();





    private final JPanel wordTextFieldPanel = new JPanel();

    private final JTextField wordTextField = new JTextField(8);

    private final JButton showBtn = new JButton("显示");





    private final JPanel pronunciationEnglishPanel = new JPanel();

    private final JButton pronunciationEnglishBtn = new JButton("英");

    private final JLabel pronunciationEnglishLabel = new JLabel();





    private final JPanel pronunciationAmericanPanel = new JPanel();

    private final JButton pronunciationAmericanBtn = new JButton("美");

    private final JLabel pronunciationAmericanLabel = new JLabel();





    private final JPanel explainSplitLine = new SplitLine("翻译及解释");





    private final JPanel explainTextAreaPanel = new JPanel();

    private final JTextArea explainTextArea = new JTextArea(6 , 24);

    private final JScrollPane explainScrollPane = new JScrollPane(explainTextArea);





    private final JPanel bottomPanel = new JPanel();

    private final JButton previousBtn = new JButton("上一个");

    private final JTextField pageTextField = new JTextField(2);

    private final JLabel maxPageLabel = new JLabel();

    private final int maxPageNumber;

    private final JButton nextBtn = new JButton("下一个");






    public WordShowPanel(JDialog window , Word word , boolean isTraverse , int panelIndex, int orderIndex , ArrayList<Integer> order , ArrayList<WordShowPanel> wordShowPanels) {
        this.window = window;
        this.word = word;
        this.isTraverse = isTraverse;
        this.panelIndex = panelIndex;
        this.orderIndex = orderIndex;
        this.order = order;
        this.wordShowPanels = wordShowPanels;
        this.maxPageNumber = order.size();
        init();
    }

    private void init(){
        firstSettings();
        addComponents();
        setListener();
        otherSettings();
        setPreferredSize();
        if (!isTraverse && orderIndex == 0) AudioPlayer.play(Config.isEnglishType() ? word.getUk_speech_File() : word.getUs_speech_File());
    }

    private void firstSettings(){
        this.setLayout(new BorderLayout());
        pageTextField.setText(String.valueOf(orderIndex + 1));
        maxPageLabel.setText(" / " + maxPageNumber);
    }

    private void otherSettings(){
        MySwingUtils.setDefaultFonts(this);
        pronunciationEnglishLabel.setFont(new Font("Consolas" , Font.PLAIN , 14));
        pronunciationAmericanLabel.setFont(new Font("Consolas" , Font.PLAIN , 14));
        wordTextField.setEditable(false);
        explainTextArea.setEditable(false);
        if (isTraverse) showWord();
        if (orderIndex == (order.size() - 1)) {
            bottomPanel.remove(nextBtn);
            JButton finishBtn = new JButton("结束");
            MySwingUtils.setDefaultFont(finishBtn);
            bottomPanel.add(finishBtn);
            finishBtn.addActionListener(e -> window.setVisible(false));
        }
    }

    private void addComponents() {
        MySwingUtils.add(this , topPanel);
        MySwingUtils.add(topPanel , showWordBox);
        MySwingUtils.add(showWordBox , wordTextFieldPanel , pronunciationEnglishPanel, pronunciationAmericanPanel , explainSplitLine , explainTextAreaPanel);
        MySwingUtils.add(wordTextFieldPanel , wordTextField);
        if (!isTraverse) MySwingUtils.add(wordTextFieldPanel , showBtn);
        MySwingUtils.add(pronunciationEnglishPanel , pronunciationEnglishBtn , pronunciationEnglishLabel);
        MySwingUtils.add(pronunciationAmericanPanel , pronunciationAmericanBtn , pronunciationAmericanLabel);
        MySwingUtils.add(explainTextAreaPanel , explainScrollPane);

        //special add
        this.add(bottomPanel , BorderLayout.SOUTH);
        MySwingUtils.add(bottomPanel , previousBtn , pageTextField , maxPageLabel , nextBtn);



    }

    private void setListener(){
        if (!isTraverse) showBtn.addActionListener(e -> showWord());
        pronunciationEnglishBtn.addActionListener(e -> AudioPlayer.play(word.getUk_speech_File()));
        pronunciationAmericanBtn.addActionListener(e -> AudioPlayer.play(word.getUs_speech_File()));
        previousBtn.addActionListener(e -> {
            if (orderIndex != 0) showPrevious();
            else InformationDialog.INFO_DIALOG.showInfo("这是第一个单词了" , () -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
        });
        nextBtn.addActionListener(e -> {
            if (orderIndex != order.size() - 1) showNext();
            else InformationDialog.INFO_DIALOG.showInfo("这是最后一个单词了" , () -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
        });
    }

    private void setPreferredSize(){
        showWordBox.setPreferredSize(new Dimension(380 , showWordBox.getComponents().length * 30 + 120));
        wordTextFieldPanel.setPreferredSize(new Dimension(380 , 30));
        pronunciationEnglishPanel.setPreferredSize(new Dimension(380 , 30));
        explainTextAreaPanel.setPreferredSize(new Dimension(380 , 200));
    }

    private void showWord() {
        wordTextField.setText(word.getWord());
        if (word.getUk_phonetic() != null) {
            pronunciationEnglishLabel.setText('/' + word.getUk_phonetic() + '/');
        }
        if (word.getUs_phonetic() != null) {
            pronunciationAmericanLabel.setText('/' + word.getUs_phonetic() + '/');
        }
        StringBuilder builder = new StringBuilder();
        ArrayList<String> translation = word.getTranslation();
        ArrayList<String> explains = word.getExplains();
        builder.append("翻译：");
        builder.append("\n");
        builder.append("        ");
        for (String trans : translation) builder.append(trans).append(';');
        builder.deleteCharAt(builder.toString().length() - 1);
        builder.append('\n');
        if (explains.size() != 0) {
            builder.append("解释：");
            builder.append("\n");
            for (String explain : explains) builder.append("        ").append(explain).append("\n");
        }
        explainTextArea.setText(builder.toString());
    }

    private void showPrevious(){
        window.remove(wordShowPanels.get(panelIndex));
        window.add(wordShowPanels.get(order.get(orderIndex - 1)));
        window.repaint();
        window.validate();
        window.invalidate();
        window.validate();
    }

    private void showNext(){
        window.remove(wordShowPanels.get(panelIndex));
        window.add(wordShowPanels.get(order.get(orderIndex + 1)));
        if (!isTraverse) AudioPlayer.play(Config.isEnglishType() ? wordShowPanels.get(order.get(orderIndex + 1)).word.getUk_speech_File() : wordShowPanels.get(order.get(orderIndex + 1)).word.getUs_speech_File());
        window.repaint();
        window.validate();
        window.invalidate();
        window.validate();
    }




}

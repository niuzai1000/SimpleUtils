package cn.wqy.ReciteEnglishWords;

import cn.wqy.SwingUtils.MySwingUtils;

import javax.swing.*;
import java.awt.*;

public class WordSettingPanel extends JPanel {

    private final JDialog window;

    private final String[] settingOptions = {"   编辑单词" , "   遍历单词" , "   背诵单词"};

    private final JList<String> settingList = new JList<>(settingOptions);





    private final JPanel rightPanel = new JPanel();





    private final JPanel rightTopPanel = new JPanel();

    private final JPanel rightBottomPanel = new JPanel();





    private final JPanel editWordPanel = new JPanel();

    private final Box editWordBox = Box.createVerticalBox();

    private final ButtonGroup APIBtnGroup = new ButtonGroup();





    private final JPanel APIPanel = new JPanel();

    private final JRadioButton notFreeAPIRadioBtn = new JRadioButton("选用网易付费API" , true);

    private final JRadioButton freeAPIRadioBtn = new JRadioButton("选用网易免费API");





    private final JPanel keyPanel = new JPanel();

    private final JLabel keyLabel = new JLabel("应用ID：");

    private final JTextField keyTextField = new JTextField(20);





    private final JPanel secretPanel = new JPanel();

    private final JLabel secretLabel = new JLabel("应用密钥：");

    private final JTextField secretTextField = new JTextField(20);

    {
        notFreeAPIRadioBtn.setToolTipText("可以获取音标，发音，翻译和解释，并且有50元体验金，足够使用，但不推荐使用");
        freeAPIRadioBtn.setToolTipText("可以获取发音，但无法获取音标，翻译和解释，但可以添加短语，当选用自定义翻译时，会自动搜索音频");
        MySwingUtils.add(APIBtnGroup , notFreeAPIRadioBtn , freeAPIRadioBtn);
    }





    private final JPanel selfOrOfficialPanel = new JPanel();

    private final ButtonGroup selfOrOfficialBtnGroup = new ButtonGroup();

    private final JRadioButton selfDefinitionRadioBtn = new JRadioButton("默认自定义翻译" , true);

    private final JRadioButton officialDefinitionRadioBtn = new JRadioButton("默认官方翻译");

    {
        MySwingUtils.add(selfOrOfficialBtnGroup , selfDefinitionRadioBtn , officialDefinitionRadioBtn);
    }







    private final JPanel traverseWordPanel = new JPanel();

    private final Box traverseWordBox = Box.createVerticalBox();





    private final JPanel randomOrOrderPanel = new JPanel();

    private final ButtonGroup traverseBtnGroup = new ButtonGroup();

    private final JRadioButton randomRadioBtn = new JRadioButton("随机遍历" , true);

    private final JRadioButton orderRadioBtn = new JRadioButton("顺序遍历");

    {
        MySwingUtils.add(traverseBtnGroup , randomRadioBtn , orderRadioBtn);
    }









    private final JPanel reciteWordPanel = new JPanel();

    private final Box reciteWordBox = Box.createVerticalBox();





    private final JPanel pronunciationTypePanel = new JPanel();

    private final ButtonGroup reciteBtnGroup = new ButtonGroup();

    private final JRadioButton englishTypeRadioBtn = new JRadioButton("英式" , true);

    private final JRadioButton americanTypeRadioBtn = new JRadioButton("美式");

    {
        MySwingUtils.add(reciteBtnGroup , englishTypeRadioBtn, americanTypeRadioBtn);
    }





    private final JPanel bottomPanel = new JPanel();

    private final JButton cancelBtn = new JButton("取消");

    private final JButton saveBtn = new JButton("保存");

    private final JButton sureBtn = new JButton("确定");


    public WordSettingPanel(JDialog window) {
        this.window = window;
        firstSettings();
        addComponents();
        setPreferredSize();
        otherSettings();
        setListener();
        initSettings();
    }

    private void firstSettings(){
        this.setLayout(new BorderLayout());
        rightPanel.setLayout(new BorderLayout());
        rightBottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        settingList.setSelectedIndex(0);
    }

    private void addComponents() {
        this.add(settingList , BorderLayout.WEST);
        MySwingUtils.add(this , rightPanel);
        MySwingUtils.add(rightPanel , rightTopPanel);
        rightPanel.add(rightBottomPanel , BorderLayout.SOUTH);
        MySwingUtils.add(rightTopPanel , editWordPanel);
        MySwingUtils.add(editWordPanel , editWordBox);
        MySwingUtils.add(editWordBox , APIPanel , keyPanel , secretPanel , selfOrOfficialPanel);
        MySwingUtils.add(APIPanel , notFreeAPIRadioBtn , freeAPIRadioBtn);
        MySwingUtils.add(keyPanel, keyLabel , keyTextField);
        MySwingUtils.add(secretPanel , secretLabel , secretTextField);
        MySwingUtils.add(selfOrOfficialPanel , selfDefinitionRadioBtn , officialDefinitionRadioBtn);
        MySwingUtils.add(traverseWordPanel , traverseWordBox);
        MySwingUtils.add(traverseWordBox , randomOrOrderPanel);
        MySwingUtils.add(randomOrOrderPanel , randomRadioBtn , orderRadioBtn);
        MySwingUtils.add(reciteWordPanel , reciteWordBox);
        MySwingUtils.add(reciteWordBox , pronunciationTypePanel);
        MySwingUtils.add(pronunciationTypePanel , englishTypeRadioBtn, americanTypeRadioBtn);
        MySwingUtils.add(rightBottomPanel , cancelBtn , saveBtn , sureBtn);
    }

    private void setPreferredSize(){
        settingList.setPreferredSize(new Dimension(90 , 500));
        editWordPanel.setPreferredSize(new Dimension(650 , 470));
        editWordBox.setPreferredSize(new Dimension(650 , editWordBox.getComponents().length * 30 + 5));
        traverseWordPanel.setPreferredSize(new Dimension(650 , 470));
        traverseWordBox.setPreferredSize(new Dimension(650 , traverseWordBox.getComponents().length * 30));
        reciteWordPanel.setPreferredSize(new Dimension(650 , 470));
        reciteWordBox.setPreferredSize(new Dimension(650 , reciteWordBox.getComponents().length * 30));
        bottomPanel.setPreferredSize(new Dimension(650 , 30));
    }

    private void otherSettings() {
        MySwingUtils.setDefaultFonts(this);
        MySwingUtils.setDefaultFonts(editWordPanel);
        MySwingUtils.setDefaultFonts(traverseWordPanel);
        MySwingUtils.setDefaultFonts(reciteWordPanel);
    }

    private void setListener() {
        settingList.addListSelectionListener(e -> {
            int selectedIndex = settingList.getSelectedIndex();
            rightTopPanel.removeAll();
            switch (selectedIndex){
                case 0:
                    rightTopPanel.add(editWordPanel);
                    break;
                case 1:
                    rightTopPanel.add(traverseWordPanel);
                    break;
                case 2:
                    rightTopPanel.add(reciteWordPanel);
                    break;
            }
            rightTopPanel.repaint();
            rightTopPanel.validate();
            rightTopPanel.invalidate();
            rightTopPanel.validate();
        });

        cancelBtn.addActionListener(e -> {
            initSettings();
            window.setVisible(false);
        });

        saveBtn.addActionListener(e -> saveSettings());

        sureBtn.addActionListener(e -> {
            saveSettings();
            window.setVisible(false);
        });


    }

    private void initSettings(){
        if (Config.isFreeAPI()) {
            freeAPIRadioBtn.setSelected(true);
        } else {
            notFreeAPIRadioBtn.setSelected(true);
        }
        if (Config.isSelfType()){
            selfDefinitionRadioBtn.setSelected(true);
        }else {
            officialDefinitionRadioBtn.setSelected(true);
        }
        if (Config.isRandomTraverse()){
            randomRadioBtn.setSelected(true);
        }else {
            orderRadioBtn.setSelected(true);
        }
        keyTextField.setText(Config.getAppKey());
        secretTextField.setText(Config.getAppSecret());
        if (Config.isEnglishType()) englishTypeRadioBtn.setSelected(true);
        else americanTypeRadioBtn.setSelected(true);
    }

    private void saveSettings(){
        Config.setFreeAPI(freeAPIRadioBtn.isSelected());
        Config.setSelfType(selfDefinitionRadioBtn.isSelected());
        Config.setRandomTraverse(randomRadioBtn.isSelected());
        Config.setAppKey(keyTextField.getText().trim());
        Config.setAppSecret(secretTextField.getText().trim());
        Config.setEnglishType(englishTypeRadioBtn.isSelected());
        Config.applySettings();
    }

}

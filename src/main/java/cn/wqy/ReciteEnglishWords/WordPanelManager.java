package cn.wqy.ReciteEnglishWords;

import cn.wqy.InformationDialog;
import cn.wqy.SwingUtils.MySwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.awt.Dialog.ModalityType.DOCUMENT_MODAL;

public class WordPanelManager {

    private final JDialog editDialog;

    private final ArrayList<WordEditPanel> wordEditPanels = new ArrayList<>();

    private final ArrayList<Word> words = new ArrayList<>();





    private final JDialog traverseDialog;

    private final JDialog reciteDialog;

    private final ArrayList<WordShowPanel> wordShowPanels = new ArrayList<>();





    private final JDialog importDialog;

    private final WordImportPanel wordImportPanel;





    private final JDialog settingDialog;

    private final WordSettingPanel wordSettingPanel;



    public WordPanelManager(JDialog parentDialog) {
        editDialog = new JDialog(parentDialog , "编辑单词" , DOCUMENT_MODAL);
        traverseDialog = new JDialog(parentDialog , "遍历单词" , DOCUMENT_MODAL);
        reciteDialog = new JDialog(parentDialog , "背诵单词" , DOCUMENT_MODAL);
        importDialog = new JDialog(parentDialog , "导入单词" , DOCUMENT_MODAL);
        settingDialog = new JDialog(parentDialog , "设置" , DOCUMENT_MODAL);
        wordImportPanel = new WordImportPanel(importDialog , words);
        wordSettingPanel = new WordSettingPanel(settingDialog);
        init();
    }

    private void init(){
        defaultJDialogSetting();
        firstSettings();
        addComponents();
        setPreferredSize();
    }

    private void defaultJDialogSetting(){
        try {
            MySwingUtils.defaultJDialogSetting(editDialog);
            MySwingUtils.defaultJDialogSetting(traverseDialog);
            MySwingUtils.defaultJDialogSetting(reciteDialog);
            MySwingUtils.defaultJDialogSetting(importDialog);
            MySwingUtils.defaultJDialogSetting(settingDialog);
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void firstSettings(){
        wordEditPanels.add(new WordEditPanel(editDialog,  wordEditPanels , words ,  0));
    }

    private void addComponents() {
        MySwingUtils.add(editDialog , wordEditPanels.get(0));
        MySwingUtils.add(importDialog , wordImportPanel);
        MySwingUtils.add(settingDialog , wordSettingPanel);
    }

    private void setPreferredSize(){
        editDialog.setSize(new Dimension(380 , 515));
        traverseDialog.setSize(new Dimension(380 , 515));
        reciteDialog.setSize(new Dimension(380 , 515));
        importDialog.setSize(new Dimension(380 , 150));
    }

    private void initWordShowPanel(boolean isTraverse) {
        wordShowPanels.clear();
        if (isTraverse) traverseDialog.getContentPane().removeAll();
        else reciteDialog.getContentPane().removeAll();
        ArrayList<Integer> order = new ArrayList<>();
        if (!isTraverse || Config.isRandomTraverse()){
            for (int i = 0 ; i < words.size() ; i++){
                Random random = new Random();
                Integer index = random.nextInt(words.size());
                boolean repeated = false;
                for (Integer orderIndex : order) if (index.equals(orderIndex)) {
                    repeated = true;
                    i--;
                    break;
                }
                if (!repeated) order.add(index);
            }
        }else {
            for (int i = 0 ; i < words.size() ; i++){
                order.add(i);
            }
        }
        int index = 0;
        if (isTraverse) {
            for (Word word : words) {
                wordShowPanels.add(new WordShowPanel(traverseDialog, word, true, index, order.indexOf(index), order, wordShowPanels));
                index++;
            }
            traverseDialog.add(wordShowPanels.get(order.get(0)));
        }
        else {
            for (Word word : words) {
                wordShowPanels.add(new WordShowPanel(reciteDialog, word, false, index, order.indexOf(index), order, wordShowPanels));
                index++;
            }
            reciteDialog.add(wordShowPanels.get(order.get(0)));
        }
    }

    public void editWords(){
        MySwingUtils.setCenterAndVisible(editDialog);
    }

    public void traverseWords(){
        if (words.size() <= 0) {
            InformationDialog.INFO_DIALOG.showInfo("请先添加单词", () -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            return;
        }
        initWordShowPanel(true);
        MySwingUtils.setCenterAndVisible(traverseDialog);
    }

    public void reciteWords(){
        if (words.size() <= 0) {
            InformationDialog.INFO_DIALOG.showInfo("请先添加单词", () -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            return;
        }
        initWordShowPanel(false);
        MySwingUtils.setCenterAndVisible(reciteDialog);
    }

    public void importWords(){
        MySwingUtils.setCenterAndVisible(importDialog);
    }

    public void setting(){
        MySwingUtils.setCenterAndVisible(settingDialog);
    }

}

package cn.wqy.ReciteEnglishWords;

import cn.wqy.IOUtils.MyIOUtils;
import cn.wqy.InformationDialog;
import cn.wqy.ReciteEnglishWords.SearchWordOnline.Search;
import cn.wqy.ReciteEnglishWords.SearchWordOnline.SearchResult;
import cn.wqy.SwingUtils.MySwingUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import static java.awt.Dialog.ModalityType.DOCUMENT_MODAL;

public class WordManager {

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



    public WordManager(JDialog parentDialog) {
        editDialog = new JDialog(parentDialog , "编辑单词" , DOCUMENT_MODAL);
        traverseDialog = new JDialog(parentDialog , "遍历单词" , DOCUMENT_MODAL);
        reciteDialog = new JDialog(parentDialog , "背诵单词" , DOCUMENT_MODAL);
        importDialog = new JDialog(parentDialog , "导入单词" , DOCUMENT_MODAL);
        settingDialog = new JDialog(parentDialog , "设置" , DOCUMENT_MODAL);
        wordImportPanel = new WordImportPanel(importDialog);
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
        if (!wordImportPanel.isLegal()) return;
        InformationDialog.INFO_DIALOG.showInfo("正在导入单词..." , () -> {
            File importFile = wordImportPanel.getFile();
            ArrayList<Word> words = new ArrayList<>();
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(importFile);
                List<String> content = Files.readAllLines(Paths.get(importFile.toURI()) , MyIOUtils.getFileCharset(importFile));
                Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z\\s.']*=[\\u4E00-\\u9FA5A-Za-z0-9;.]+$");
                for (String lineString : content){
                    if (pattern.matcher(lineString).matches()){
                        String word = lineString.split("=")[0];
                        ArrayList<String> translation = new ArrayList<>(Arrays.asList(lineString.split("=")[1].split(";")));
                        InformationDialog.INFO_DIALOG.setInfo("正在导入 " + word + " 中");
                        System.out.println(word);
                        System.out.println(Arrays.toString(translation.toArray()));
                        SearchResult result = Search.search(word , true);
                        words.add(new Word(word , translation , result.getUk_speech_File() , result.getUs_speech_File()));
                    }
                }
                this.words.clear();
                this.words.addAll(words);
                InformationDialog.INFO_DIALOG.setInfo("导入成功");
            } catch (IOException e) {
                e.printStackTrace();
                InformationDialog.INFO_DIALOG.setInfo("导入失败");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } finally {
                try {
                    MyIOUtils.close(fis);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setting(){
        MySwingUtils.setCenterAndVisible(settingDialog);
    }

}

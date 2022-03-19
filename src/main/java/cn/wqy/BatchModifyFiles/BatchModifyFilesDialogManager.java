package cn.wqy.BatchModifyFiles;

import cn.wqy.IOUtils.MyIOUtils;
import cn.wqy.InformationDialog;
import cn.wqy.SwingUtils.MySwingUtils;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.AudioInfo;
import ws.schild.jave.info.VideoInfo;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.awt.Dialog.ModalityType.DOCUMENT_MODAL;
import static javax.swing.JFileChooser.DIRECTORIES_ONLY;

public class BatchModifyFilesDialogManager {

    private JDialog dialog;

    private final Box fileProcessBox = Box.createVerticalBox();





    public JPanel pathPanel = new JPanel();

    public JLabel pathLabel = new JLabel("   文件夹路径（必填）：");

    public JTextField pathTextField = new JTextField(44);

    public JButton pathBtn = new JButton("...");





    public JPanel linePanel1 = new SplitLine();





    public JPanel reSuffixPanel1 = new JPanel();

    public JLabel reSuffixLabel1 = new JLabel("   修改后缀名（不要加点）：  ");

    public JLabel oldSuffixLabel = new JLabel("旧后缀名：");

    public JTextField oldSuffixTextField = new JTextField(10);

    public JLabel newSuffixLabel = new JLabel("新后缀名：");

    public JTextField newSuffixTextField = new JTextField(10);





    public JPanel reSuffixPanel2 = new JPanel();

    public JLabel reSuffixLabel2 = new JLabel("                                                直接修改后缀名：");

    public JButton reSuffixBtn1 = new JButton("开始");

    public JLabel reSuffixLabel3 = new JLabel(" 视音频文件转码：");

    public JButton reSuffixBtn2 = new JButton("开始");

    public JLabel reSuffixLabel4 = new JLabel(" 提取音频：");

    public JButton reSuffixBtn3 = new JButton("开始");





    public JPanel linePanel2 = new SplitLine();





    public JPanel movePanel1 = new JPanel();

    public JLabel moveLabel = new JLabel("   根目录修改文件：   ");

    public JLabel moveContainLabel = new JLabel("文件名中含有：");

    public JTextField moveContainTextField = new JTextField(12);

    public JLabel moveNonContainLabel = new JLabel("文件名中不含有：");

    public JTextField moveNonContainTextField = new JTextField(12);





    public JPanel movePanelExtra = new JPanel();

    public JCheckBox moveCaseCheckBox = new JCheckBox("区分大小写" , true);

    public JCheckBox onlyRootPathFileCheckBox = new JCheckBox("处理根目录文件" , true);

    public JCheckBox onlyRootPathDirectoryCheckBox = new JCheckBox("处理根目录文件夹" , true);

    public JCheckBox saveRootDirCheckBox = new JCheckBox("保留根目录文件夹" , false);





    public JPanel movePanel2 = new JPanel();

    public JLabel movePathLabel = new JLabel("                           移动到：");

    public JTextField movePathTextField = new JTextField(36);

    public JButton movePathBtn = new JButton("...");

    public JButton moveBtn = new JButton("开始");





    public JPanel movePanel3 = new JPanel();

    public JLabel copyPathLabel = new JLabel("                           复制到：");

    public JTextField copyPathTextField = new JTextField(36);

    public JButton copyPathBtn = new JButton("...");

    public JButton copyBtn = new JButton("开始");





    public JPanel movePanel4 = new JPanel();

    public JLabel deleteLabel = new JLabel("                           删除：                                                                                                                         ");

    public JButton deleteBtn = new JButton("开始");





    public JPanel linePanel3 = new SplitLine();





    public JPanel complexModifyFilePanel = new JPanel();

    public JLabel complexModifyFileLabel1 = new JLabel("   复杂修改文件：   ");

    public JLabel complexModifyFileDirectoryNumberLabel = new JLabel("根路径下的路径长度：");

    public JTextField complexModifyFileDirectoryNumberTextField = new JTextField(3);

    public JLabel complexModifyFileLabel2 = new JLabel("   设置：");

    public JButton complexModifyFileBtn = new JButton("开始");

    public DirectoryManager complexModifyFileDirectoryManager;

    public JDialog complexModifyDialog = new JDialog(dialog, "设置相对路径" , DOCUMENT_MODAL);





    public JPanel complexModifyFileMovePanel = new JPanel();

    public JLabel complexModifyFileMoveLabel = new JLabel("                         ");

    public JLabel complexModifyFileMovePathLabel = new JLabel("移动到：");

    public JTextField complexModifyFileMovePathTextField = new JTextField(36);

    public JButton complexModifyFileMovePathBtn = new JButton("...");

    public JButton complexModifyFileMoveBtn = new JButton("开始");





    public JPanel complexModifyFileCopyPanel = new JPanel();

    public JLabel complexModifyFileCopyLabel = new JLabel("                         ");

    public JLabel complexModifyFileCopyPathLabel = new JLabel("复制到：");

    public JTextField complexModifyFileCopyPathTextField = new JTextField(36);

    public JButton complexModifyFileCopyPathBtn = new JButton("...");

    public JButton complexModifyFileCopyBtn = new JButton("开始");





    public JPanel complexModifyFileDeletePanel = new JPanel();

    public JLabel complexModifyFileDeleteLabel = new JLabel("                         ");

    public JLabel complexModifyFileDeletePathLabel = new JLabel("删除：                                                                                                                         ");

    public JButton complexModifyFileDeleteBtn = new JButton("开始");


    public BatchModifyFilesDialogManager(JFrame jFrame) {
        dialog = new JDialog(jFrame , "批量文件处理工具" , DOCUMENT_MODAL);
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

    protected void addComponents(){
        MySwingUtils.add(dialog
                , fileProcessBox);
        MySwingUtils.add(fileProcessBox
                , pathPanel
                , linePanel1
                , reSuffixPanel1
                , reSuffixPanel2
                , linePanel2
                , movePanel1
                , movePanelExtra
                , movePanel2
                , movePanel3
                , movePanel4
                , linePanel3
                , complexModifyFilePanel
                , complexModifyFileMovePanel
                , complexModifyFileCopyPanel
                , complexModifyFileDeletePanel);
        MySwingUtils.add(pathPanel
                , pathLabel
                , pathTextField
                , pathBtn);
        MySwingUtils.add(reSuffixPanel1
                , reSuffixLabel1
                , oldSuffixLabel
                , oldSuffixTextField
                , newSuffixLabel
                , newSuffixTextField);
        MySwingUtils.add(reSuffixPanel2
                , reSuffixLabel2
                , reSuffixBtn1
                , reSuffixLabel3
                , reSuffixBtn2
                , reSuffixLabel4
                , reSuffixBtn3);
        MySwingUtils.add(movePanel1
                , moveLabel
                , moveContainLabel
                , moveContainTextField
                , moveNonContainLabel
                , moveNonContainTextField);
        MySwingUtils.add(movePanelExtra
                , moveCaseCheckBox
                , onlyRootPathFileCheckBox
                , onlyRootPathFileCheckBox
                , onlyRootPathDirectoryCheckBox
                , saveRootDirCheckBox);
        MySwingUtils.add(movePanel2
                , movePathLabel
                , movePathTextField
                , movePathBtn
                , moveBtn);
        MySwingUtils.add(movePanel3
                , copyPathLabel
                , copyPathTextField
                , copyPathBtn
                , copyBtn);
        MySwingUtils.add(movePanel4
                , deleteLabel
                , deleteBtn);
        MySwingUtils.add(complexModifyFilePanel
                , complexModifyFileLabel1
                , complexModifyFileDirectoryNumberLabel
                , complexModifyFileDirectoryNumberTextField
                , complexModifyFileLabel2
                , complexModifyFileBtn);
        MySwingUtils.add(complexModifyFileMovePanel
                , complexModifyFileMoveLabel
                , complexModifyFileMovePathLabel
                , complexModifyFileMovePathTextField
                , complexModifyFileMovePathBtn
                , complexModifyFileMoveBtn);
        MySwingUtils.add(complexModifyFileCopyPanel
                , complexModifyFileCopyLabel
                , complexModifyFileCopyPathLabel
                , complexModifyFileCopyPathTextField
                , complexModifyFileCopyPathBtn
                , complexModifyFileCopyBtn);
        MySwingUtils.add(complexModifyFileDeletePanel
                , complexModifyFileDeleteLabel
                , complexModifyFileDeletePathLabel
                , complexModifyFileDeleteBtn);

    }

    protected void otherSettings(){
        dialog.setLayout(new FlowLayout());
        pathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        reSuffixPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        reSuffixPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        movePanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        movePanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        movePanel3.setLayout(new FlowLayout(FlowLayout.LEFT));
        movePanel4.setLayout(new FlowLayout(FlowLayout.LEFT));
        complexModifyFilePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        complexModifyFileMovePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        complexModifyFileCopyPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        complexModifyFileDeletePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    protected void setListener(){

        addChoosePathListener(pathBtn, pathTextField);

        addChoosePathListener(movePathBtn, movePathTextField);

        addChoosePathListener(copyPathBtn , copyPathTextField);

        addChoosePathListener(complexModifyFileMovePathBtn, complexModifyFileMovePathTextField);

        addChoosePathListener(complexModifyFileCopyPathBtn, complexModifyFileCopyPathTextField);


        reSuffixBtn1.addActionListener(e -> InformationDialog.INFO_DIALOG.showInfo("正在处理文件中..." , () -> {
            File[] files = new File(pathTextField.getText()).listFiles();
            String oldSuffix = oldSuffixTextField.getText().trim();
            String newSuffix = newSuffixTextField.getText().trim();
            if (!("".equals(oldSuffix) && "".equals(newSuffix))){
                if (files == null) return;
                for (File file : files){
                    if (file.getName().contains("." + oldSuffix)){
                        InformationDialog.INFO_DIALOG.setInfo("正在处理" + file.getAbsolutePath().replace(new File(pathTextField.getText().trim()).getAbsolutePath() , "") + "文件中...");
                        if (!file.renameTo(new File(file.getAbsolutePath().replace("." + oldSuffix, "." + newSuffix)))){
                            try {
                                throw new FileNotFoundException("无法修改该文件");
                            } catch (FileNotFoundException fileNotFoundException) {
                                fileNotFoundException.printStackTrace();
                            }
                        }
                    }
                }
            }
        }));

        reSuffixBtn2.addActionListener(e -> InformationDialog.INFO_DIALOG.showInfo("正在处理文件中..." , () -> {
            File[] files = new File(pathTextField.getText()).listFiles();
            String oldSuffix = oldSuffixTextField.getText().trim();
            String newSuffix = newSuffixTextField.getText().trim();
            if (files == null) return;
            if (!("".equals(oldSuffix) && "".equals(newSuffix))){
                for (File file : files){
                    if (file.isFile() && file.getName().contains("." + oldSuffix)){
                        InformationDialog.INFO_DIALOG.setInfo("正在处理" + file.getAbsolutePath().replace(new File(pathTextField.getText().trim()).getAbsolutePath() , "") + "文件中...");
                        String path = file.getAbsolutePath();
                        MultimediaObject source = new MultimediaObject(new File(path));
                        File target = new File(path.replace("." + oldSuffix, "." + newSuffix));
                        AudioInfo audioInfo;
                        VideoInfo videoInfo;
                        AudioAttributes audio;
                        VideoAttributes video;
                        EncodingAttributes attrs = new EncodingAttributes();
                        try {
                            video = new VideoAttributes();
                            videoInfo = source.getInfo().getVideo();
                            if (videoInfo != null) {
                                video.setBitRate(videoInfo.getBitRate());
                                video.setFrameRate((int) videoInfo.getFrameRate());
                                video.setSize(videoInfo.getSize());
                                attrs.setOutputFormat(newSuffix);
                                attrs.setVideoAttributes(video);
                            }
                        } catch (EncoderException encoderException) {
                            encoderException.printStackTrace();
                        }
                        try {
                            audio = new AudioAttributes();
                            audioInfo = source.getInfo().getAudio();
                            audio.setBitRate(audioInfo.getBitRate());
                            audio.setChannels(audioInfo.getChannels());
                            audio.setSamplingRate(audioInfo.getSamplingRate());
                            attrs.setOutputFormat(newSuffix);
                            attrs.setAudioAttributes(audio);
                        } catch (EncoderException encoderException) {
                            encoderException.printStackTrace();
                        }
                        Encoder encoder = new Encoder();
                        try {
                            InformationDialog.INFO_DIALOG.setInfo("正在转码" + file.getAbsolutePath().replace(new File(pathTextField.getText().trim()).getAbsolutePath() , "") + "文件中...");
                            encoder.encode(source , target , attrs);
                            InformationDialog.INFO_DIALOG.setInfo("文件" + file.getAbsolutePath().replace(new File(pathTextField.getText().trim()).getAbsolutePath() , "") + "转码成功");
                        } catch (EncoderException encoderException) {
                            InformationDialog.INFO_DIALOG.setInfo("文件" + file.getAbsolutePath().replace(new File(pathTextField.getText().trim()).getAbsolutePath() , "") + "转码失败");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            encoderException.printStackTrace();
                        }
                    }
                }
            }
        }));

        reSuffixBtn3.addActionListener(e -> InformationDialog.INFO_DIALOG.showInfo("正在处理文件中..." , () -> {
            File[] files = new File(pathTextField.getText()).listFiles();
            String oldSuffix = oldSuffixTextField.getText().trim();
            String newSuffix = newSuffixTextField.getText().trim();
            if (files == null) {
                InformationDialog.INFO_DIALOG.setInfo("该目录下没有文件");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return;
            }
            if (!("".equals(oldSuffix) && "".equals(newSuffix))){
                for (File file : files){
                    if (file.isFile() && file.getName().contains("." + oldSuffix)){
                        InformationDialog.INFO_DIALOG.setInfo("正在处理" + file.getAbsolutePath().replace(new File(pathTextField.getText().trim()).getAbsolutePath() , "") + "文件中...");
                        String path = file.getAbsolutePath();
                        MultimediaObject source = new MultimediaObject(new File(path));
                        File target = new File(path.replace("." + oldSuffix, "." + newSuffix));
                        AudioInfo audioInfo;
                        AudioAttributes audio;
                        EncodingAttributes attrs = new EncodingAttributes();
                        try {
                            audio = new AudioAttributes();
                            audioInfo = source.getInfo().getAudio();
                            audio.setBitRate(audioInfo.getBitRate());
                            audio.setChannels(audioInfo.getChannels());
                            audio.setSamplingRate(audioInfo.getSamplingRate());
                            attrs.setOutputFormat(newSuffix);
                            attrs.setAudioAttributes(audio);
                        } catch (EncoderException encoderException) {
                            encoderException.printStackTrace();
                        }
                        Encoder encoder = new Encoder();
                        try {
                            InformationDialog.INFO_DIALOG.setInfo("正在提取" + file.getAbsolutePath().replace(new File(pathTextField.getText().trim()).getAbsolutePath() , "") + "文件音频中...");
                            encoder.encode(source , target , attrs);
                            InformationDialog.INFO_DIALOG.setInfo("文件" + file.getAbsolutePath().replace(new File(pathTextField.getText().trim()).getAbsolutePath() , "") + "音频提取成功");
                        } catch (EncoderException encoderException) {
                            InformationDialog.INFO_DIALOG.setInfo("文件" + file.getAbsolutePath().replace(new File(pathTextField.getText().trim()).getAbsolutePath() , "") + "音频提取失败");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                            encoderException.printStackTrace();
                        }
                    }
                }
            }
        }));

        moveBtn.addActionListener(e -> InformationDialog.INFO_DIALOG.showInfo("正在处理文件中..." , () -> {
            File[] files = new File(pathTextField.getText()).listFiles();
            if (files == null) {
                InformationDialog.INFO_DIALOG.setInfo("该目录下没有文件");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return;
            }
            String contain = moveContainTextField.getText();
            String nonContain = moveNonContainTextField.getText();
            if (!moveCaseCheckBox.isSelected()){
                contain = contain.toLowerCase();
                nonContain = nonContain.toLowerCase();
            }
            for (File file : files){
                if (onlyRootPathFileCheckBox.isSelected() && file.isFile()){
                    InformationDialog.INFO_DIALOG.setInfo("正在处理" + file.getAbsolutePath().replace(new File(pathTextField.getText().trim()).getAbsolutePath() , "") + "文件中...");
                    String fileName = file.getName();
                    if (!moveCaseCheckBox.isSelected()) fileName = fileName.toLowerCase();
                    if (fileName.contains(contain) && ("".equals(nonContain) || !fileName.contains(nonContain))){
                        if (!file.renameTo(new File(file.getAbsolutePath().replace(file.getParent() , movePathTextField.getText().trim())))){
                            try {
                                throw new FileNotFoundException("无法修改该文件");
                            } catch (FileNotFoundException fileNotFoundException) {
                                fileNotFoundException.printStackTrace();
                            }
                        }
                    }
                }
                if (onlyRootPathDirectoryCheckBox.isSelected() && file.isDirectory()){
                    InformationDialog.INFO_DIALOG.setInfo("正在处理" + file.getAbsolutePath().replace(new File(pathTextField.getText().trim()).getAbsolutePath() , "") + "文件夹中...");
                    String fileName = file.getName();
                    if (!moveCaseCheckBox.isSelected()) fileName = fileName.toLowerCase();
                    if (fileName.contains(contain) && ("".equals(nonContain) || !fileName.contains(nonContain))){
                        if (!file.renameTo(new File(file.getAbsolutePath().replace(file.getParent() , movePathTextField.getText().trim())))){
                            try {
                                throw new FileNotFoundException("无法修改该文件");
                            } catch (FileNotFoundException fileNotFoundException) {
                                fileNotFoundException.printStackTrace();
                            }
                        }
                    }
                }
            }
        }));

        copyBtn.addActionListener(e -> InformationDialog.INFO_DIALOG.showInfo("正在处理文件中..." , () -> {
            File oldFolder = new File(pathTextField.getText());
            File newFolder = new File(copyPathTextField.getText());
            File[] files = oldFolder.listFiles();
            if (files == null) {
                InformationDialog.INFO_DIALOG.setInfo("该目录下没有文件");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return;
            }
            String contain = moveContainTextField.getText();
            String nonContain = moveNonContainTextField.getText();
            boolean toLowerCase = !moveCaseCheckBox.isSelected();
            if (toLowerCase){
                contain = contain.toLowerCase();
                nonContain = nonContain.toLowerCase();
            }
            String oldAbsolutePath = oldFolder.getAbsolutePath();
            String newAbsolutePath = newFolder.getAbsolutePath();
            for (File oldFile : files){
                if (onlyRootPathFileCheckBox.isSelected() && oldFile.isFile()){
                    InformationDialog.INFO_DIALOG.setInfo("正在处理" + oldFile.getAbsolutePath().replace(new File(pathTextField.getText().trim()).getAbsolutePath() , "") + "文件中...");
                    String fileName = oldFile.getName();
                    if (toLowerCase) fileName = fileName.toLowerCase();
                    if (fileName.contains(contain) && ("".equals(nonContain) || !fileName.contains(nonContain))){
                        File newFile = new File(oldFile.getAbsolutePath().replace(oldAbsolutePath , newAbsolutePath));
                        try {
                            MyIOUtils.copyFile(oldFile , newFile , saveRootDirCheckBox.isSelected());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                if (onlyRootPathDirectoryCheckBox.isSelected() && oldFile.isDirectory()){
                    InformationDialog.INFO_DIALOG.setInfo("正在处理" + oldFile.getAbsolutePath().replace(new File(pathTextField.getText().trim()).getAbsolutePath() , "") + "文件夹中...");
                    String fileName = oldFile.getName();
                    if (toLowerCase) fileName = fileName.toLowerCase();
                    if (fileName.contains(contain) && ("".equals(nonContain) || !fileName.contains(nonContain))){
                        File newFile = new File(oldFile.getAbsolutePath().replace(oldAbsolutePath , newAbsolutePath));
                        try {
                            MyIOUtils.copyFile(oldFile , newFile , saveRootDirCheckBox.isSelected());
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                }
            }
        }));


        deleteBtn.addActionListener(e -> InformationDialog.INFO_DIALOG.showInfo("正在处理文件中..." , () -> {
            File[] files = new File(pathTextField.getText()).listFiles();
            if (files == null) {
                InformationDialog.INFO_DIALOG.setInfo("该目录下没有文件");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return;
            }
            String contain = moveContainTextField.getText();
            String nonContain = moveNonContainTextField.getText();
            boolean toLowerCase = !moveCaseCheckBox.isSelected();
            if (toLowerCase){
                contain = contain.toLowerCase();
                nonContain = nonContain.toLowerCase();
            }
            for (File file : files){
                if (onlyRootPathFileCheckBox.isSelected() && file.isFile()){
                    InformationDialog.INFO_DIALOG.setInfo("正在处理" + file.getAbsolutePath().replace(new File(pathTextField.getText().trim()).getAbsolutePath() , "") + "文件中...");
                    String fileName = file.getName();
                    if (toLowerCase) fileName = fileName.toLowerCase();
                    if (fileName.contains(contain) && ("".equals(nonContain) || !fileName.contains(nonContain))){
                        boolean succeed = file.delete();
                        if (!succeed) return;
                    }
                }
                if (onlyRootPathDirectoryCheckBox.isSelected() && file.isDirectory()){
                    InformationDialog.INFO_DIALOG.setInfo("正在处理" + file.getAbsolutePath().replace(new File(pathTextField.getText().trim()).getAbsolutePath() , "") + "文件夹中...");
                    String fileName = file.getName();
                    if (toLowerCase) fileName = fileName.toLowerCase();
                    if (fileName.contains(contain) && ("".equals(nonContain) || !fileName.contains(nonContain))){
                        try {
                            MyIOUtils.deleteFile(file , saveRootDirCheckBox.isSelected());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }));

        complexModifyFileBtn.addActionListener(e -> {
            AtomicBoolean legal = new AtomicBoolean(true);
            InformationDialog.INFO_DIALOG.showInfo("正在初始化路径编辑中...", () -> {
                try {
                    int directoryLength = Integer.parseInt(complexModifyFileDirectoryNumberTextField.getText().trim());
                    if (!(directoryLength >= 1 && directoryLength < 100)) {
                        InformationDialog.INFO_DIALOG.setInfo("请输入小于100的正整数");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        legal.set(false);
                    }
                    complexModifyFileDirectoryManager = new DirectoryManager(directoryLength, complexModifyDialog, pathTextField.getText());
                } catch (NumberFormatException exception) {
                    InformationDialog.INFO_DIALOG.setInfo("请输入合法的整数");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    exception.printStackTrace();
                    legal.set(false);
                }
            });
            if (!legal.get()) return;
            try {
                complexModifyFileDirectoryManager.editDirectory();
            } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException exception) {
                exception.printStackTrace();
            }
        });

        complexModifyFileMoveBtn.addActionListener(e -> InformationDialog.INFO_DIALOG.showInfo("正在处理文件中..." , () -> {
            if (complexModifyFileDirectoryManager != null) complexModifyFileDirectoryManager.moveFiles(complexModifyFileMovePathTextField.getText());
            else {
                try {
                    InformationDialog.INFO_DIALOG.setInfo("请先编辑文件路径");
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }));

        complexModifyFileCopyBtn.addActionListener(e -> InformationDialog.INFO_DIALOG.showInfo("正在处理文件中..." , () -> {
            if (complexModifyFileDirectoryManager != null) complexModifyFileDirectoryManager.copyFiles(complexModifyFileCopyPathTextField.getText());
            else {
                try {
                    InformationDialog.INFO_DIALOG.setInfo("请先编辑文件路径");
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }));

        complexModifyFileDeleteBtn.addActionListener(e -> InformationDialog.INFO_DIALOG.showInfo("正在处理文件中..." , () -> {
            if (complexModifyFileDirectoryManager != null) complexModifyFileDirectoryManager.deleteFiles();
            else {
                try {
                    InformationDialog.INFO_DIALOG.setInfo("请先编辑文件路径");
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }));


    }



    private void addChoosePathListener(JButton btn, JTextField textField) {
        btn.addActionListener(e -> {
            File selectedFile = new File(textField.getText());
            if (!selectedFile.exists()) selectedFile = new File(System.getProperty("java.class.path").split(";")[0]).getParentFile();
            JFileChooser chooser = new JFileChooser(selectedFile);
            MySwingUtils.setDefaultFonts(chooser);
            chooser.setFileSelectionMode(DIRECTORIES_ONLY);
            chooser.showSaveDialog(dialog);
            File file = chooser.getSelectedFile();
            if (file != null && file.exists()){
                textField.setText(file.toString());
            }
        });
    }

    private void setPreferredSize(){
        fileProcessBox.setPreferredSize(new Dimension(740 , fileProcessBox.getComponents().length * 30));
        pathBtn.setPreferredSize(new Dimension(20 , 20));
        pathPanel.setPreferredSize(new Dimension(740 , 30));
        reSuffixPanel1.setPreferredSize(new Dimension(740 , 30));
        reSuffixPanel2.setPreferredSize(new Dimension(740 , 30));
        movePathBtn.setPreferredSize(new Dimension(20 , 20));
        movePanel1.setPreferredSize(new Dimension(740 , 30));
        movePanelExtra.setPreferredSize(new Dimension(740 , 30));
        movePanel2.setPreferredSize(new Dimension(740 , 30));
        copyPathBtn.setPreferredSize(new Dimension(20 , 20));
        movePanel3.setPreferredSize(new Dimension(740 , 30));
        movePanel4.setPreferredSize(new Dimension(740 , 30));
        complexModifyFilePanel.setPreferredSize(new Dimension(740 , 30));
        complexModifyFileMovePanel.setPreferredSize(new Dimension(740 , 30));
        complexModifyFileMovePathBtn.setPreferredSize(new Dimension(20 , 20));
        complexModifyFileCopyPanel.setPreferredSize(new Dimension(740 , 30));
        complexModifyFileCopyPathBtn.setPreferredSize(new Dimension(20 , 20));
        complexModifyFileDeletePanel.setPreferredSize(new Dimension(740 , 30));



    }


}

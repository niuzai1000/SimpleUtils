package cn.wqy.BatchModifyFiles;

import cn.wqy.IOUtils.MyIOUtils;
import cn.wqy.InformationDialog;
import cn.wqy.SwingUtils.MySwingUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class DirectoryManager {
    private final int directoryLength;

    private final ArrayList<StringBuilder> directories;

    private final ArrayList<DirectoryEditPanel> directoryEditPanels;

    private final JDialog dialog;

    private final String rootPath;

    public DirectoryManager(int directoryLength, JDialog dialog, String rootPath) {
        this.directoryLength = directoryLength;
        this.directories = new ArrayList<>(directoryLength);
        this.directoryEditPanels = new ArrayList<>(directoryLength);
        this.dialog = dialog;
        this.rootPath = rootPath;
        init();
    }

    private void init() {
        if (directoryLength > 1) {
            directoryEditPanels.add(new DirectoryEditPanel(dialog, directoryLength, 1 , true, false));
            MySwingUtils.setDefaultFonts(directoryEditPanels.get(0));
        }
        else directoryEditPanels.add(new DirectoryEditPanel(dialog, directoryLength, 1 , true, true));
        for (int i = 1 ; i < directoryLength - 1 ; i++){
            directoryEditPanels.add(new DirectoryEditPanel(dialog, directoryLength, i + 1, false , false));
            directoryEditPanels.get(i).setPrevious(directoryEditPanels.get(i - 1));
            directoryEditPanels.get(i).setNext(null);
            directoryEditPanels.get(i - 1).setNext(directoryEditPanels.get(i));
            MySwingUtils.setDefaultFonts(directoryEditPanels.get(i));
        }
        if (directoryLength > 1) {
            directoryEditPanels.add(new DirectoryEditPanel(dialog, directoryLength, directoryLength, false, true));
            directoryEditPanels.get(directoryLength - 1).setPrevious(directoryEditPanels.get(directoryLength - 2));
            directoryEditPanels.get(directoryLength - 2).setNext(directoryEditPanels.get(directoryLength - 1));
            MySwingUtils.setDefaultFonts(directoryEditPanels.get(directoryLength - 1));
        }else directoryEditPanels.get(directoryLength - 1).setPrevious(null);
        directoryEditPanels.get(directoryLength - 1).setNext(null);
        ArrayList<StringBuilder> rootPaths = new ArrayList<>();
        rootPaths.add(new StringBuilder(rootPath).append('\\'));
        directoryEditPanels.get(0).setRootPaths(rootPaths);
    }

    public void editDirectory() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        dialog.getContentPane().removeAll();
        dialog.add(directoryEditPanels.get(0));
        directoryEditPanels.get(0).initialShow();
        dialog.repaint();
        dialog.validate();
        dialog.invalidate();
        dialog.validate();
        MySwingUtils.defaultJDialogSetting(dialog);
        dialog.setSize(new Dimension(380 , 515));
        InformationDialog.INFO_DIALOG.showInfo("开始路径编辑..." , () -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        MySwingUtils.setCenterAndVisible(dialog);
        this.directories.addAll(directoryEditPanels.get(directoryLength - 1).getDirectories());
    }

    public void moveFiles(String parentPath){
        ArrayList<File> files = getFiles();
        for (File file : files) {
            if (file.exists()){
                InformationDialog.INFO_DIALOG.setInfo("正在处理" + file.getAbsolutePath().replace(rootPath , "") + "文件中...");
                boolean succeed = file.renameTo(new File(parentPath + '\\' + file.getName()));
                if (!succeed) return;
            }
        }
    }

    public void copyFiles(String parentPath){
        ArrayList<File> files = getFiles();
        for (File file : files) {
            if (file.exists()) {
                if (file.isFile()) {
                    InformationDialog.INFO_DIALOG.setInfo("正在处理" + file.getAbsolutePath().replace(rootPath , "") + "文件中...");
                    try {
                        MyIOUtils.copyFile(file, new File(parentPath + '\\' + file.getName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (file.isDirectory()) {
                    InformationDialog.INFO_DIALOG.setInfo("正在处理" + file.getAbsolutePath().replace(rootPath , "") + "文件夹中...");
                    try {
                        MyIOUtils.copyFolder(file , new File(parentPath + '\\' + file.getName()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void deleteFiles(){
        ArrayList<File> files = getFiles();
        for (File file : files) {
            if (file.exists()){
                if (file.isFile()){
                    InformationDialog.INFO_DIALOG.setInfo("正在处理" + file.getAbsolutePath().replace(rootPath , "") + "文件中...");
                    boolean succeed = file.delete();
                    if (!succeed) return;
                }else if (file.isDirectory()) {
                    InformationDialog.INFO_DIALOG.setInfo("正在处理" + file.getAbsolutePath().replace(rootPath , "") + "文件夹中...");
                    try {
                        MyIOUtils.deleteFolder(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private ArrayList<File> getFiles(){
        ArrayList<File> files = new ArrayList<>();
        for (StringBuilder directory : directories) files.add(new File(directory.toString()));
        return files;
    }

}

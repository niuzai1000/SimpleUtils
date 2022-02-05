package cn.wqy.BatchModifyFiles;

import cn.wqy.BatchModifyFiles.DirectoryPart.MultiIntegerPart;
import cn.wqy.BatchModifyFiles.DirectoryPart.MultiStringPart;
import cn.wqy.BatchModifyFiles.DirectoryPart.StringPart;
import cn.wqy.BatchModifyFiles.DirectoryPart.MultiPart;
import cn.wqy.BatchModifyFiles.DirectoryPart.Part;
import cn.wqy.SwingUtils.MySwingUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class DirectoryEditPanel extends JPanel {
    private final JDialog window;

    private final int currentPage;

    private final boolean first;

    private final boolean last;

    private ArrayList<StringBuilder> rootPaths;





    private final JPanel topPanel = new JPanel();

    private final Box verticalBox = Box.createVerticalBox();





    private final JPanel rootPathsPanel1 = new JPanel();

    private final JLabel rootPathsLabel1 = new JLabel("   父路径：");





    private final JPanel rootPathsPanel2 = new JPanel();

    private final JLabel rootPathsLabel2 = new JLabel("             ");

    private final JTextArea rootPathsTextArea = new JTextArea(3 , 20);

    private final JScrollPane rootPathsScrollPane = new JScrollPane(rootPathsTextArea);





    private final JPanel directoryPanel1 = new JPanel();

    private final JLabel directoryLabel1 = new JLabel("   当前路径名：");





    private final JPanel directoryPanel2 = new JPanel();

    private final JLabel directoryLabel2 = new JLabel("             ");

    private final JTextArea directoryTextArea = new JTextArea(4 , 20);

    private final JScrollPane directoryScrollPane = new JScrollPane(directoryTextArea);





    private final JPanel directoryQueryPanel = new JPanel();

    private final JCheckBox directoryFuzzyQueryCheckBox = new JCheckBox("模糊查询" , false);

    private final JCheckBox directoryToLowerCaseCheckBox = new JCheckBox("区别大小写（模糊查询）" , true);





    private final JPanel directoryPanel3 = new JPanel();

    private final JLabel directoryLabel3 = new JLabel("      ");

    private final JTextField directoryStringTextField = new JTextField(16);

    private final JButton directoryStringBtn = new JButton("添加字符串");





    private final JPanel directoryPanel4 = new JPanel();

    private final JLabel directoryLabel4 = new JLabel("      ");

    private final JLabel directoryNumberBeginLabel = new JLabel("起始：");

    private final JTextField directoryNumberBeginTextField = new JTextField(3);

    private final JLabel directoryNumberEndLabel = new JLabel("结束：");

    private final JTextField directoryNumberEndTextField = new JTextField(3);

    private final JButton directoryNumberBtn = new JButton("添加连续整数");







    private final JPanel directoryPanel5 = new JPanel();

    private final JLabel directoryLabel5 = new JLabel("      ");

    private final JTextField directorySelectableStringTextField = new JTextField(8);

    private final JButton directoryAddSelectableStringBtn = new JButton("添加字符串");

    private final JButton directorySelectableStringFinishedBtn = new JButton("添加完成");





    private final JPanel directoryPanel6 = new JPanel();

    private final JButton directoryDeleteBtn = new JButton("删除最后一行");





    private final JPanel bottomPanel = new JPanel();

    private final JButton showPreviousBtn = new JButton("上一页");

    private final JTextField pageTextField = new JTextField(2);

    private final JButton showNextBtn = new JButton("下一页");

    private final JLabel maxPageLabel;






    private DirectoryEditPanel previous;

    private DirectoryEditPanel next;

    private MultiPart<?> currentMultiPart;

    //泛型里可以使用"?"表示不知道有什么类型的对象
    private final ArrayList<Part<?>> directoryNameTemp = new ArrayList<>();

    private final ArrayList<StringBuilder> directories = new ArrayList<>();

    public DirectoryEditPanel(JDialog window, int directoryLength, int currentPage, boolean first, boolean last) {
        this.window = window;
        this.currentPage = currentPage;
        this.first = first;
        this.last = last;
        this.maxPageLabel = new JLabel(" / " + directoryLength);
        otherSettings();
        addComponents();
        setListener();
    }

    private void otherSettings() {




        //layout settings
        this.setLayout(new BorderLayout());
        rootPathsPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        rootPathsPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        directoryPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        directoryPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        directoryPanel3.setLayout(new FlowLayout(FlowLayout.LEFT));
        directoryPanel4.setLayout(new FlowLayout(FlowLayout.LEFT));
        directoryPanel5.setLayout(new FlowLayout(FlowLayout.LEFT));




        //preferredSize settings
        verticalBox.setPreferredSize(new Dimension(380 , verticalBox.getComponents().length * 20 + 375));
        rootPathsPanel1.setPreferredSize(new Dimension(380 , 30));
        rootPathsPanel2.setPreferredSize(new Dimension(380 , 160));
        directoryPanel1.setPreferredSize(new Dimension(380 , 30));
        directoryPanel2.setPreferredSize(new Dimension(380 , 160));
        directoryQueryPanel.setPreferredSize(new Dimension(380 , 30));
        directoryPanel3.setPreferredSize(new Dimension(380 , 30));
        directoryPanel4.setPreferredSize(new Dimension(380 , 30));
        directoryPanel5.setPreferredSize(new Dimension(380 , 30));
        directoryPanel6.setPreferredSize(new Dimension(380 , 30));



    }

    public void initialShow(){
        refreshRootPathTextArea();
        rootPathsTextArea.setEditable(false);
        pageTextField.setText(String.valueOf(currentPage));
        directoryTextArea.setEditable(false);
        pageTextField.setEditable(false);
    }



    private void addComponents() {
        MySwingUtils.add(this , topPanel);
        MySwingUtils.add(topPanel , verticalBox);
        MySwingUtils.add(verticalBox
                , rootPathsPanel1
                , rootPathsPanel2
                , directoryPanel1
                , directoryPanel2
                , directoryQueryPanel
                , directoryPanel3
                , directoryPanel4
                , directoryPanel5
                , directoryPanel6);
        MySwingUtils.add(rootPathsPanel1 , rootPathsLabel1);
        MySwingUtils.add(rootPathsPanel2 , rootPathsLabel2 , rootPathsScrollPane);
        MySwingUtils.add(directoryPanel1 , directoryLabel1);
        MySwingUtils.add(directoryPanel2 , directoryLabel2 , directoryScrollPane);
        MySwingUtils.add(directoryQueryPanel , directoryFuzzyQueryCheckBox , directoryToLowerCaseCheckBox);
        MySwingUtils.add(directoryPanel3 , directoryLabel3 , directoryStringTextField, directoryStringBtn);
        MySwingUtils.add(directoryPanel4 , directoryLabel4 , directoryNumberBeginLabel , directoryNumberBeginTextField , directoryNumberEndLabel , directoryNumberEndTextField , directoryNumberBtn);
        MySwingUtils.add(directoryPanel5 , directoryLabel5 , directorySelectableStringTextField , directoryAddSelectableStringBtn , directorySelectableStringFinishedBtn);
        MySwingUtils.add(directoryPanel6 , directoryDeleteBtn);

        this.add(bottomPanel , BorderLayout.SOUTH);
        MySwingUtils.add(bottomPanel , showPreviousBtn , pageTextField , maxPageLabel , showNextBtn);


        //special add
        if (last){
            bottomPanel.remove(showNextBtn);
            JButton finishBtn = new JButton("完成");
            MySwingUtils.setDefaultFont(finishBtn);
            finishBtn.addActionListener(e -> {
                updateDirectories();
                window.setVisible(false);
            });
            bottomPanel.add(finishBtn);
            window.invalidate();
            window.validate();
        }






    }

    private void setListener(){
        showPreviousBtn.addActionListener(e -> {
            if (!first) showPrevious();
        });

        showNextBtn.addActionListener(e -> {
            if (!last) showNext();
        });

        directoryStringBtn.addActionListener(e -> {
            String namePart = directoryStringTextField.getText();
            if (!"".equals(namePart)) directoryNameTemp.add(new StringPart(namePart));
            updateDirectories();
            updateDirectoryTextArea();
        });

        directoryNumberBtn.addActionListener(e -> {
            int begin = Integer.parseInt(directoryNumberBeginTextField.getText());
            int end = Integer.parseInt(directoryNumberEndTextField.getText());
            if (begin > end) return;
            MultiPart<Integer> multiPart = new MultiIntegerPart();
            for (int i = begin ; i <= end ; i++) multiPart.addItem(i);
            directoryNameTemp.add(multiPart);
            updateDirectories();
            updateDirectoryTextArea();
        });

        directoryAddSelectableStringBtn.addActionListener(e -> {
            String namePart = directorySelectableStringTextField.getText();
            if (currentMultiPart == null) {
                MultiPart<String> multiPart = new MultiStringPart();
                currentMultiPart = multiPart;
                directoryNameTemp.add(multiPart);
                if (!"".equals(namePart)) multiPart.addItem(directorySelectableStringTextField.getText());
            }else {
                if (currentMultiPart instanceof MultiStringPart){
                    MultiStringPart multiStringPart = (MultiStringPart) currentMultiPart;
                    if (!"".equals(namePart)) multiStringPart.addItem(directorySelectableStringTextField.getText());
                }
            }
            updateDirectories();
            updateDirectoryTextArea();
        });

        directorySelectableStringFinishedBtn.addActionListener(e -> currentMultiPart = null);

        directoryDeleteBtn.addActionListener(e -> {
            if (directoryNameTemp.size() >= 1) directoryNameTemp.remove(directoryNameTemp.size() - 1);
            updateDirectories();
            updateDirectoryTextArea();
        });


    }

    private void updateDirectories(){
        directories.clear();
        ArrayList<StringBuilder> directoryTemp = new ArrayList<>();
        rootPaths.forEach(rootPath -> {
            ArrayList<StringBuilder> rootPathNameTemp = new ArrayList<>();
            rootPathNameTemp.add(rootPath);
            if (!directoryFuzzyQueryCheckBox.isSelected()){
                directoryNameTemp.forEach(part -> {
                    ArrayList<StringBuilder> rootPathNameTempTemp = new ArrayList<>(rootPathNameTemp);
                    rootPathNameTemp.clear();
                    if (part instanceof MultiPart){
                        MultiPart<?> multiPart = (MultiPart<?>) part;
                        multiPart.getItems().forEach(item ->
                                rootPathNameTempTemp.forEach(rootPathName ->
                                        rootPathNameTemp.add(new StringBuilder(rootPathName).append(item.toString()))));
                    }else {
                        rootPathNameTempTemp.forEach(rootPathName ->
                                rootPathNameTemp.add(new StringBuilder(rootPathName).append(part.getItem().toString())));
                    }
                });
            }else {
                ArrayList<StringBuilder> directoryContains = new ArrayList<>();
                directoryContains.add(new StringBuilder());
                directoryNameTemp.forEach(part -> {
                    ArrayList<StringBuilder> directoryContainsTemp = new ArrayList<>(directoryContains);
                    directoryContains.clear();
                    if (part instanceof MultiPart){
                        MultiPart<?> multiPart = (MultiPart<?>) part;
                        multiPart.getItems().forEach(item ->
                                directoryContainsTemp.forEach(namePart ->
                                        directoryContains.add(new StringBuilder(namePart).append(item.toString()))));
                    }else {
                        directoryContainsTemp.forEach(namePart ->
                                directoryContains.add(new StringBuilder(namePart).append(part.getItem().toString())));
                    }
                });
                ArrayList<StringBuilder> rootPathNameTempTemp = new ArrayList<>(rootPathNameTemp);
                rootPathNameTemp.clear();
                for (StringBuilder rootPathName : rootPathNameTempTemp) {
                    StringBuilder rootPathTempTemp = new StringBuilder(rootPathName).deleteCharAt(rootPathName.length() - 1);
                    File[] files = new File(rootPathTempTemp.toString()).listFiles();
                    if (files == null) continue;
                    for (File file : files){
                        AtomicBoolean exist = new AtomicBoolean(false);
                        directoryContains.forEach(fileName -> {
                            if (directoryToLowerCaseCheckBox.isSelected()){
                                if (file.getName().contains(fileName.toString())) {
                                    exist.set(true);
                                }
                            }else {
                                if (file.getName().toLowerCase().contains(fileName.toString().toLowerCase())) {
                                    exist.set(true);
                                }
                            }
                        });
                        if (exist.get()){
                            rootPathNameTemp.add(new StringBuilder(file.getAbsolutePath()));
                        }
                    }
                }
            }
            directoryTemp.addAll(rootPathNameTemp);
        });
        directories.addAll(directoryTemp);
    }

    private void updateDirectoryTextArea(){
        directoryTextArea.setText("");
        directoryNameTemp.forEach(namePart -> {
            directoryTextArea.append(namePart.toString());
            directoryTextArea.append("\n");
        });
    }

    private void refreshRootPathTextArea(){
        rootPathsTextArea.setText("");
        for (StringBuilder path : rootPaths) rootPathsTextArea.append(path.toString() + '\n');
    }

    private void showPrevious(){
        window.remove(this);
        window.add(previous);
        previous.initialShow();
        window.repaint();
        window.validate();
        window.invalidate();
        window.validate();
    }

    private void showNext(){
        window.remove(this);
        window.add(next);
        updateDirectories();
        directories.forEach(rootPath -> {
            if (!rootPath.toString().endsWith("\\")) rootPath.append('\\');
        });
        next.setRootPaths(directories);
        next.initialShow();
        window.repaint();
        window.validate();
        window.invalidate();
        window.validate();
    }

    public ArrayList<StringBuilder> getDirectories(){
        return directories;
    }

    public void setRootPaths(ArrayList<StringBuilder> rootPaths) {
        this.rootPaths = rootPaths;
    }

    public void setPrevious(DirectoryEditPanel previous) {
        this.previous = previous;
    }

    public void setNext(DirectoryEditPanel next) {
        this.next = next;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(306 , 416);
    }

}

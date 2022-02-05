package cn.wqy.BatchModifyFiles.DirectoryPart;

import java.util.ArrayList;

public interface MultiPart<T> extends Part<T> {

    ArrayList<T> getItems();

    void addItem(T t);

    void removeItem(T t);

    void removeItem(int index);

    int getCount();

}

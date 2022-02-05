package cn.wqy.BatchModifyFiles.DirectoryPart;


import java.util.ArrayList;

public abstract class MultiNumberPart<T extends Number> implements MultiPart<T> {
    @Override
    public ArrayList<T> getItems() {
        return null;
    }

    @Override
    public void addItem(T t) {}

    @Override
    public void removeItem(T t) {}

    @Override
    public void removeItem(int index) {}

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public T getItem() {
        return null;
    }

    @Override
    public void setItem(T t) {}
}

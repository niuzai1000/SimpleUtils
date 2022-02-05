package cn.wqy.BatchModifyFiles.DirectoryPart;

import java.util.ArrayList;

public class MultiIntegerPart extends MultiNumberPart<Integer> {

    private final ArrayList<Integer> items = new ArrayList<>();

    @Override
    public ArrayList<Integer> getItems() {
        return items;
    }

    @Override
    public void addItem(Integer t) {
        items.add(t);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public void removeItem(Integer t) {
        items.remove(t);
    }

    @Override
    public void removeItem(int index) {
        items.remove(index);
    }

    @Override
    public Integer getItem() { return null; }

    @Override
    public void setItem(Integer t) {}

    @Override
    public String toString() {
        StringBuilder stringTemp = new StringBuilder();
        stringTemp.append("Integer:{");
        if (items.size() > 0) items.forEach(item -> stringTemp.append(' ').append(item).append(' ').append(','));
        else stringTemp.append(' ');
        stringTemp.deleteCharAt(stringTemp.length() - 1).append('}');
        return stringTemp.toString();
    }
}

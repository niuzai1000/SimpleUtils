package cn.wqy.BatchModifyFiles.DirectoryPart;


import java.util.ArrayList;

public class MultiStringPart implements MultiPart<String> {

    private final ArrayList<String> items = new ArrayList<>();

    @Override
    public ArrayList<String> getItems() {
        return items;
    }

    @Override
    public void addItem(String t) {
        items.add(t);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public void removeItem(String t) {
        items.remove(t);
    }

    @Override
    public void removeItem(int index) {
        items.remove(index);
    }

    @Override
    public String getItem() { return null; }

    @Override
    public void setItem(String t) {}

    @Override
    public String toString() {
        StringBuilder stringTemp = new StringBuilder();
        stringTemp.append("String:{");
        if (items.size() > 0) items.forEach(item -> stringTemp.append(' ').append(item).append(' ').append(','));
        else stringTemp.append(' ');
        stringTemp.deleteCharAt(stringTemp.length() - 1).append('}');
        return stringTemp.toString();
    }
}

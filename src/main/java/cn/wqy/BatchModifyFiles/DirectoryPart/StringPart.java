package cn.wqy.BatchModifyFiles.DirectoryPart;



public class StringPart implements Part<String> {

    private String item;

    public StringPart(String item) {
        this.item = item;
    }

    @Override
    public String getItem() {
        return item;
    }

    @Override
    public void setItem(String s) {
        item = s;
    }

    @Override
    public String toString() {
        return item;
    }
}

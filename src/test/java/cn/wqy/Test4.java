package cn.wqy;

import cn.wqy.SwingUtils.MySwingUtils;

import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

public class Test4 {
    public static void main(String[] args) {
        AffineTransform affinetransform = new AffineTransform();
        FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
        System.out.println(MySwingUtils.DEFAULT_FONT.getStringBounds(" ", frc).getWidth());// 3.8349609375
    }
}

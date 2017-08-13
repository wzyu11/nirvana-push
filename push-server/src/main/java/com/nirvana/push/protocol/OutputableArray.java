package com.nirvana.push.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 可输出数组类。使用<code>CompositeByteBuf</code>的特性来组合子元素，提供输出服务。
 * Created by Nirvana on 2017/8/2.
 */
public class OutputableArray extends AbstractOutputable {

    private List<Outputable> elements = new ArrayList<>();

    /**
     * 组合ByteBuf.
     */
    private CompositeByteBuf byteBuf = Unpooled.compositeBuffer();

    public OutputableArray() {
    }

    public OutputableArray(Outputable... outputables) {
        for (Outputable outputable : outputables) {
            addElement(outputable);
        }
    }

    public OutputableArray(Collection<Outputable> outputables) {
        for (Outputable outputable : outputables) {
            addElement(outputable);
        }
    }

    /**
     * 增加组件。
     */
    protected void addElement(Outputable element) {
        if (element != null) {
            elements.add(element);
            byteBuf.addComponent(true, element.getByteBuf());
        }
    }

    /**
     * 增加组件。
     */
    protected void addElements(Outputable... elements) {
        for (Outputable element : elements) {
            addElement(element);
        }
    }

    /**
     * 子元素数量。
     */
    public int arraySize() {
        return elements.size();
    }

    /**
     * 清除组件。
     */
    public void clear() {
        elements.clear();
        byteBuf.clear();
    }

    @Override
    public ByteBuf getByteBuf() {
        return byteBuf;
    }
}

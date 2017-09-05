package com.nirvana.push.protocol.p2;

import com.nirvana.push.protocol.exception.ProtocolException;

import java.util.*;

/**
 * DST文本协议实现。
 * Created by Nirvana on 2017/9/4.
 */
public class DSTPackage {

    /*协议包原始文本*/
    private String content;

    /**
     * DST协议中的元素是有序的，且顺序有意义。
     */
    private List<DSTElement> elements = new ArrayList<>();

    /**
     * 此DSTPackage中值组织而成的Map.
     * 假如是k-v形式的DSTElement，则其在此map中对应的key值为DSTElement的key值。
     * 假如是简单DSTElement，则其在此map中对应的key值为:'-'+元素的index.
     * 此map用于如下两个方法：
     *
     * @see #get(String)
     * @see #get(int)
     */
    private Map<String, String> values = new HashMap<>();

    /**
     * 从Values构建一个DST 协议包.
     */
    public DSTPackage(String... values) {
        StringBuilder contentBuilder = new StringBuilder();
        for (String value : values) {
            DSTElement element = new DSTElement(value);
            elements.add(element);
            contentBuilder.append(element.content()).append('\n');
            this.values.put(String.valueOf(DSTDefinition.SEPARATOR) + (elements.size() - 1), value);
        }
        content = contentBuilder.toString();
    }

    /**
     * 从Elements构建一个DST协议包.
     */
    public DSTPackage(DSTElement... elements) {
        StringBuilder contentBuilder = new StringBuilder();
        for (DSTElement element : elements) {
            this.elements.add(element);
            contentBuilder.append(element.content()).append('\n');
            if (element.plain()) {
                values.put(String.valueOf(DSTDefinition.SEPARATOR) + (this.elements.size() - 1), element.getValue());
            } else {
                values.put(element.getKey(), element.getValue());
            }
        }
        content = contentBuilder.toString();
    }

    /**
     * 由一个协议文本构建协议包，对文本进行解析校验。
     */
    public DSTPackage(String data) {
        this.content = data;

        int index = 0;
        boolean crossSeparator = false;
        StringBuilder keyBuilder = new StringBuilder();
        StringBuilder valueBuilder = new StringBuilder();

        while (index < data.length()) {
            char c = data.charAt(index);
            if (c == '\r' || c == '\n') {
                StringBuilder delimiter = new StringBuilder();
                delimiter.append(c);

                while (index + 1 < data.length()) {
                    c = data.charAt(index + 1);
                    if (c == '\n' || c == '\r') {
                        delimiter.append(c);
                        index++;
                    } else {
                        break;
                    }
                }
                if (!DSTDefinition.delimiters.contains(delimiter.toString())) {
                    throw new ProtocolException("未知分隔符:index:" + index);
                }
                if (!crossSeparator) {
                    throw new ProtocolException("格式错误，缺少:\'" + DSTDefinition.SEPARATOR + "\'index:" + index);
                }

                DSTElement element = new DSTElement(keyBuilder.toString(), valueBuilder.toString());
                elements.add(element);
                if (element.plain()) {
                    values.put(String.valueOf(DSTDefinition.SEPARATOR) + (elements.size() - 1), element.getValue());
                } else {
                    values.put(element.getKey(), element.getValue());
                }

                keyBuilder = new StringBuilder();
                valueBuilder = new StringBuilder();
                crossSeparator = false;
            } else {
                if (index == data.length() - 1) {
                    throw new ProtocolException("协议解析错误，必须以分隔符结尾。");
                }
                if (c == DSTDefinition.SEPARATOR && !crossSeparator) {
                    crossSeparator = true;
                } else {
                    if (crossSeparator) {
                        valueBuilder.append(c);
                    } else {
                        keyBuilder.append(c);
                    }
                }
            }
            index++;
        }
    }

    /**
     * 获取此协议包中真实文本。
     */
    public String getContent() {
        return content;
    }

    /**
     * DSTPackage中的key-value形式的元素，通过key获取value值
     */
    public String get(String key) {
        if (key.contains(String.valueOf(DSTDefinition.SEPARATOR))) {
            throw new ProtocolException("Key值不合法");
        }
        return values.get(key);
    }

    /**
     * 根据Index获取Plain DSTElement的值。
     * 此方法不会获取key-value形式的DSTElement.
     *
     * @return 如果目标index无简单DSTElement元素，返回null.否则返回值
     */
    public String get(int index) {
        return values.get(String.valueOf(DSTDefinition.SEPARATOR) + index);
    }

    /**
     * 根据Index获取DSTElement。
     */
    public DSTElement getElement(int index) {
        return elements.get(index);
    }

    /**
     * DST协议包中元素数量。
     */
    public int size() {
        return elements.size();
    }

    /**
     * 获取所有key。
     */
    public Collection<String> keys() {
        return values.keySet();
    }

    public void print() {
        System.out.println("[elements]:");
        elements.forEach(System.out::println);
        System.out.println("[values]:");
        values.forEach((k, v) -> System.out.println(k + ":" + v));
    }

}

package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.schema.SchemaRegistry;
import io.github.yezhihao.protostar.util.Explain;
import io.netty.buffer.ByteBuf;

/**
 * 消息结构
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public abstract class BasicField<T> implements Schema<T>, Comparable<BasicField> {

    protected java.lang.reflect.Field f;
    protected Field field;
    protected int length;
    protected String desc;

    public void readAndSet(ByteBuf input, Object obj) throws Exception {
        T value = readFrom(input);
        f.set(obj, value);
    }

    public void getAndWrite(ByteBuf output, Object obj) throws Exception {
        T value = (T) f.get(obj);
        writeTo(output, value);
    }

    public void readAndSet(ByteBuf input, Object obj, Explain explain) throws Exception {
        T value = readFrom(input, explain);
        f.set(obj, value);
    }

    public void getAndWrite(ByteBuf output, Object obj, Explain explain) throws Exception {
        T value = (T) f.get(obj);
        writeTo(output, value, explain);
    }

    public BasicField build(java.lang.reflect.Field f, Field field) {
        if (this.f == null && this.field == null) {
            this.f = f;
            this.field = field;
            Integer len = SchemaRegistry.getLength(f.getType());
            length = len != null ? len : 16;
            desc = field.desc();
            if (desc.isEmpty())
                desc = f.getName();
        }
        return this;
    }

    public String fieldName() {
        return f.getName();
    }

    public String desc() {
        return desc;
    }

    /** 用于预估内存分配，不需要精确值 */
    public int length() {
        return 32;
    }

    @Override
    public int compareTo(BasicField that) {
        return Integer.compare(this.field.index(), that.field.index());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(12);
        sb.append(desc).append(' ').append(field);
        return sb.toString();
    }
}
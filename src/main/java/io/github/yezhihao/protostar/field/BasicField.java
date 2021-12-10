package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.Explain;
import io.github.yezhihao.protostar.util.Info;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * 基本类型字段
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class BasicField<T> implements Schema<T>, Comparable<BasicField> {

    protected final int lengthSize;
    protected final int length;
    protected final Field field;
    protected final Schema<T> schema;
    protected final java.lang.reflect.Field f;

    public BasicField(Field field, java.lang.reflect.Field f, Schema<T> schema) {
        this.schema = schema;
        int length = field.length();
        if (length < 0)
            length = field.type().length;
        this.length = length;
        this.lengthSize = field.lengthSize();
        this.field = field;
        this.f = f;
        try {
            f.setAccessible(true);
        } catch (Exception e) {
        }
    }

    public void set(Object message, Object value) throws IllegalAccessException {
        f.set(message, value);
    }

    public Object get(Object message) throws IllegalAccessException {
        return f.get(message);
    }

    @Override
    public T readFrom(ByteBuf input) {
        return schema.readFrom(input);
    }

    @Override
    public void writeTo(ByteBuf output, T value) {
        if (value != null)
            schema.writeTo(output, value);
    }

    public T readFrom(ByteBuf input, Explain explain) {
        int begin = input.readerIndex();

        T value = schema.readFrom(input, explain);

        int end = input.readerIndex();
        String raw = ByteBufUtil.hexDump(input, begin, end - begin);
        explain.add(Info.field(begin, field, value, raw));
        return value;
    }

    public void writeTo(ByteBuf output, T value, Explain explain) {
        int begin = output.writerIndex();

        if (value != null) {
            schema.writeTo(output, value);

            int end = output.writerIndex();
            String raw = ByteBufUtil.hexDump(output, begin, end - begin);
            explain.add(Info.field(begin, field, value, raw));
        }
    }

    public int length() {
        return length;
    }

    @Override
    public int compareTo(BasicField that) {
        return Integer.compare(this.field.index(), that.field.index());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(50);
        sb.append('[');
        sb.append("index=").append(field.index());
        sb.append(", length=").append(length);
        sb.append(", field=").append(f.getName());
        sb.append(", desc").append(field.desc());
        sb.append(']');
        return sb.toString();
    }
}
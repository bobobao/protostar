package io.github.yezhihao.protostar.field;

import io.github.yezhihao.protostar.Schema;
import io.github.yezhihao.protostar.annotation.Field;
import io.github.yezhihao.protostar.util.Explain;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * 无界字典域，只能位于消息末尾
 * @author yezhihao
 * https://gitee.com/yezhihao/jt808-server
 */
public class MapField extends BasicField {

    public final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final Supplier<Map> supplier;

    public MapField(Field field, java.lang.reflect.Field f, Schema schema) {
        super(field, f, schema);
        if (HashMap.class.isAssignableFrom(f.getType()))
            supplier = HashMap::new;
        else
            supplier = TreeMap::new;
    }

    public Object readFrom(ByteBuf input) {
        Map map = supplier.get();
        Map.Entry entry;
        try {
            do {
                entry = (Map.Entry) schema.readFrom(input);
                map.put(entry.getKey(), entry.getValue());
            } while (input.isReadable());
        } catch (Exception e) {
            log.warn("解析出错:{}", ByteBufUtil.hexDump(input, input.readerIndex(), input.writerIndex()));
        }
        return map;
    }

    public void writeTo(ByteBuf output, Object t) {
        if (t != null) {
            Map<Object, Object> map = (Map) t;
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                schema.writeTo(output, entry);
            }
        }
    }

    @Override
    public Object readFrom(ByteBuf input, Explain explain) {
        Map map = new TreeMap();
        Map.Entry entry;
        try {
            do {
                entry = (Map.Entry) schema.readFrom(input, explain);
                map.put(entry.getKey(), entry.getValue());
            } while (input.isReadable());
        } catch (Exception e) {
            log.warn("解析出错:{}", ByteBufUtil.hexDump(input, input.readerIndex(), input.writerIndex()));
        }
        return map;
    }

    @Override
    public void writeTo(ByteBuf output, Object t, Explain explain) {
        if (t != null) {
            Map<Object, Object> map = (Map) t;
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                schema.writeTo(output, entry, explain);
            }
        }
    }
}
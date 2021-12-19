package io.github.yezhihao.protostar.schema;

import io.github.yezhihao.protostar.field.BasicField;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

public class ByteBufferSchema extends BasicField<ByteBuffer> {

    @Override
    public ByteBuffer readFrom(ByteBuf input) {
        ByteBuffer message = input.nioBuffer();
        input.skipBytes(input.readableBytes());
        return message;
    }

    @Override
    public ByteBuffer readFrom(ByteBuf input, int length) {
        if (length < 0)
            length = input.readableBytes();
        ByteBuffer byteBuffer = input.nioBuffer(input.readerIndex(), length);
        input.skipBytes(length);
        return byteBuffer;
    }

    @Override
    public void writeTo(ByteBuf output, ByteBuffer value) {
        output.writeBytes(value);
    }

    @Override
    public void writeTo(ByteBuf output, int length, ByteBuffer value) {
        if (length > 0)
            value.position(value.limit() - length);
        output.writeBytes(value);
    }
}
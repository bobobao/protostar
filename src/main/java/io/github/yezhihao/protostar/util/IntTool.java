package io.github.yezhihao.protostar.util;

import io.netty.buffer.ByteBuf;

public interface IntTool {

    static IntTool getInstance(int length) {
        switch (length) {
            case 1:
                return BYTE;
            case 2:
                return WORD;
            case 3:
                return MEDIUM;
            case 4:
                return DWORD;
            default:
                throw new IllegalArgumentException("unsupported length: " + length + " (expected: 1, 2, 3, 4)");
        }
    }

    int get(ByteBuf in, int i);

    void set(ByteBuf out, int i, int n);

    int read(ByteBuf in);

    void write(ByteBuf out, int n);

    IntTool BYTE = new IntTool() {
        @Override
        public int get(ByteBuf in, int i) {
            return in.getByte(i);
        }

        @Override
        public void set(ByteBuf out, int i, int n) {
            out.setByte(i, n);
        }

        @Override
        public int read(ByteBuf in) {
            return in.readByte();
        }

        @Override
        public void write(ByteBuf out, int n) {
            out.writeByte(n);
        }
    };
    IntTool WORD = new IntTool() {
        @Override
        public int get(ByteBuf in, int i) {
            return in.getShort(i);
        }

        @Override
        public void set(ByteBuf out, int i, int n) {
            out.setShort(i, n);
        }

        @Override
        public int read(ByteBuf in) {
            return in.readShort();
        }

        @Override
        public void write(ByteBuf out, int n) {
            out.writeShort(n);
        }
    };
    IntTool MEDIUM = new IntTool() {
        @Override
        public int get(ByteBuf in, int i) {
            return in.getMedium(i);
        }

        @Override
        public void set(ByteBuf out, int i, int n) {
            out.setMedium(i, n);
        }

        @Override
        public int read(ByteBuf in) {
            return in.readMedium();
        }

        @Override
        public void write(ByteBuf out, int n) {
            out.writeMedium(n);
        }
    };
    IntTool DWORD = new IntTool() {
        @Override
        public int get(ByteBuf in, int i) {
            return in.getInt(i);
        }

        @Override
        public void set(ByteBuf out, int i, int n) {
            out.setInt(i, n);
        }

        @Override
        public int read(ByteBuf in) {
            return in.readInt();
        }

        @Override
        public void write(ByteBuf out, int n) {
            out.writeInt(n);
        }
    };
}
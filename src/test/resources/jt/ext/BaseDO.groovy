package jt.ext

import io.github.yezhihao.protostar.annotation.Field
import io.github.yezhihao.protostar.annotation.Message

/**
 * @author baoqingyun
 */
@Message(0x001)
class BaseDO {
    @Field(index = 0, length = 2, desc = "ID")
    protected int type
    @Field(index = 1, length = 20, desc = "名称")
    protected String clientId
}

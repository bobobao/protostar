package jt.ext

import io.github.yezhihao.protostar.annotation.Field

import java.time.LocalDateTime

/**
 * @author baoqingyun
 */
class Foo extends BaseDO {
    @Field(lengthUnit = 1, desc = "名称")
    private String name
    @Field(length = 2, desc = "ID")
    private int id
    @Field(charset = "BCD", desc = "日期")
    private LocalDateTime dateTime
}

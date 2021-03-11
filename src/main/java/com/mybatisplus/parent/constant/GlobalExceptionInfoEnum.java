package com.mybatisplus.parent.constant;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chentuan
 * @desc 全局异常信息
 * @date 2020-04-21
 */
public enum GlobalExceptionInfoEnum {

    //全局异常处理
    TYPE_CONVERSION_EXCEPTION(9000, "类型转换异常"),
    DELETE_DATA_EXCEPTION(9001, "删除数据异常"),
    INSERT_DATA_EXCEPTION(9002, "新增数据异常"),
    UPDATE_DATA_EXCEPTION(9003, "修改数据异常"),
    ;

    /**
     * code
     */
    private Integer code;

    /**
     * 描述
     */
    private String description;

    GlobalExceptionInfoEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 根据code获取状态值信息枚举
     *
     * @param code
     * @return 对应的枚举信息，如果code不存在，则返回null
     * @throws NullPointerException code为空则抛出该异常
     */
    public static GlobalExceptionInfoEnum getByCode(Integer code) {
        List<GlobalExceptionInfoEnum> codeAndMessageEnums = Arrays.asList(GlobalExceptionInfoEnum.values());
        Map<Integer, GlobalExceptionInfoEnum> messageEnumMapByCode =
                codeAndMessageEnums.stream().collect(Collectors.toMap(GlobalExceptionInfoEnum::getCode, e -> e));
        return messageEnumMapByCode.get(code);
    }
}

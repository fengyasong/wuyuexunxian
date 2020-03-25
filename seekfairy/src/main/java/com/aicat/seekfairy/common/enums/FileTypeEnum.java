package com.aicat.seekfairy.common.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum FileTypeEnum {
    FILE_TYPE_ZIP("application/x-zip-compressed", ".zip"),
    FILE_TYPE_RAR("application/octet-stream", ".rar");
    public String type;
    public String fileStufix;
    //private FileTypeEnum(String type, String fileStufix){}
    public static String getFileStufix(String type) {
        for (FileTypeEnum orderTypeEnum : FileTypeEnum.values()) {
            if (orderTypeEnum.type.equals(type)) {
                return orderTypeEnum.fileStufix;
            }
        }
        return null;
    }
}

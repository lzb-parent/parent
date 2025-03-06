package com.pro.common.modules.api.dependencies.message;

import com.pro.framework.api.util.JSONUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToSocket implements Serializable {
    private static final long serialVersionUID = 3110367564503529058L;
    public static final String DEFAULT_TOPIC = "/defaultTopic";
    public static final String OPT_DEFAULT = EnumSocketOpt.doUpdate.name();

    private String topic;
    private Boolean isAllUser;
    private String opt;
    private String dataClass;
    private String data;
    private List<Long> userIds;


    public static ToSocket toAllUser(String dataClass, Object data) {
        return new ToSocket(DEFAULT_TOPIC, true, OPT_DEFAULT, dataClass, getData(data), null);
    }

    public static ToSocket toUser(String dataClass, Object data, List<Long> userIds) {
        return new ToSocket(DEFAULT_TOPIC, false, OPT_DEFAULT, dataClass, getData(data), userIds);
    }

    public static ToSocket toUser(String dataClass, Object data, Long... userIds) {
        return toUser(dataClass, data, Arrays.stream(userIds).collect(Collectors.toList()));
    }

    private static String getData(Object data) {
        return null == data ? null : data instanceof String ? (String) data : JSONUtils.toString(data);
    }
}

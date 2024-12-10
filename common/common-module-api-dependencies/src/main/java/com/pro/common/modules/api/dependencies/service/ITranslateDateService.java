package com.pro.common.modules.api.dependencies.service;

import java.util.LinkedHashMap;

public interface ITranslateDateService {
    LinkedHashMap<String, String> getKeyValueMap(boolean isCommon);
}

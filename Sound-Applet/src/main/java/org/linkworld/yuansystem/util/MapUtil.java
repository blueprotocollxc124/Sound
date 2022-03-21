package org.linkworld.yuansystem.util;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/2/17
 */


import com.alibaba.fastjson.JSON;

import java.util.Map;
public class MapUtil {

    /**
     * @Description: map<Object,Object>->map<String,Object>
     * @date: 2022/2/15 20:54
     * @Param: [map]
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     */
    public static   Map<String,Object> objectToStringMap(Map<Object,Object> map) {
        String mapString = JSON.toJSONString(map);
        Map<String,Object> newMap = (Map<String,Object>)JSON.parseObject(mapString, Map.class);
        return newMap;
    }

}

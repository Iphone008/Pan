package com.example.library.utils;

import ohos.agp.components.AttrSet;

public class AttrUtil {

    AttrSet attrSet;
    private static AttrUtil instance;

    public AttrUtil() {
        super();
    }


    public static AttrUtil getInstance() {
        if (instance == null) {
            instance = new AttrUtil();
        }
        return instance;
    }

    public AttrUtil(AttrSet attrSet) {
        this.attrSet = attrSet;
    }

    public String getStringValue(String key, String defValue) {
        if (attrSet.getAttr(key).isPresent()) {
            return attrSet.getAttr(key).get().getStringValue();
        } else {
            return defValue;
        }
    }
}


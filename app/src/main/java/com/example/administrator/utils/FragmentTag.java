package com.example.administrator.utils;

/**
 * 使用枚举存储fragment
 * @ClassName FragmentTag
 * @Date 2019-05-29 20:33
 **/

public enum  FragmentTag {
    TAG_HOME("com.example.administrator.fragment.HomeFragment"),
    TAG_CATEGORY("com.example.administrator.fragment.CategoryFragment"),
    TAG_SCAN("com.example.administrator.fragment.ScanFragment"),
    TAG_CART("com.example.administrator.fragment.CartFragment"),
    TAG_MY("com.example.administrator.fragment.MyFragment");
    String tag;

    private FragmentTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}

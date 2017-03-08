package com.atguigu.shoppingmall_1020.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wangmingqiang on 2017/3/8.
 */
@Entity
public class GreenBean {
    @Id
    private Long id;

    private String name;

    @Generated(hash = 1563188897)
    public GreenBean(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 1002137420)
    public GreenBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

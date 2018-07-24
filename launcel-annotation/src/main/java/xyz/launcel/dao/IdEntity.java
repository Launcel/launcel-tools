package xyz.launcel.dao;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by launcel on 2018/7/22.
 */
@Getter
@Setter
public class IdEntity
{
    private Integer id;

    private Boolean enabled;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;
}

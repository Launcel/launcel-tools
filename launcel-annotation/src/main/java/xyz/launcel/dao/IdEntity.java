package xyz.launcel.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by launcel on 2018/7/22.
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class IdEntity implements Serializable
{
    private static final long    serialVersionUID = 6726275056311417504L;
    private              Integer id;
    private              Boolean enabled;
    private              Date    createTime;
    private              String  createUser;
    private              Date    updateTime;
    private              String  updateUser;
}

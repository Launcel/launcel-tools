package xyz.launcel.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by launcel on 2018/7/23.
 */
@Setter
@Getter
@Accessors(chain = true)
public class BaseRequest implements Serializable
{
    private static final long serialVersionUID = 664881353984645891L;

    private String userName;

    private Integer uid;

    /**
     * ios, androd, h5,xcx
     */
    private String device;


}
package xyz.x.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by launcel on 2018/7/23.
 */
@Setter
@Getter
@ToString
public class Request implements Serializable
{
    private static final long serialVersionUID = 664881353984645891L;

    private String userName;

    private Integer uid;

    /**
     * ios, androd, h5,xcx
     */
    private String device;
}

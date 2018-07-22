package xyz.launcel.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by launcel on 2018/7/22.
 */
@Getter
@Setter
public class IdEntity implements Serializable
{
    private static final long serialVersionUID = 2638391319936267941L;

    private Integer id;
}

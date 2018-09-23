package xyz.launcel.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by launcel on 2018/9/23.
 */
@Getter
@Setter
public class BaseQuery
{
    private Integer       id;
    private List<Integer> idList;
}

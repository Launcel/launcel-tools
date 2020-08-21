package xyz.launcel.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest extends Request
{
    private static final long    serialVersionUID = -5159465788965700864L;
    private              Integer pageNo           = 1;
    private transient    Integer row              = 15;
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private              Date    startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private              Date    endTime;
}

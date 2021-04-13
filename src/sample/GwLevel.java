package sample;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class GwLevel
{
    Long secondsFromBeginTime;
    LocalDateTime localDateTime;
    Float level;
    public GwLevel(Long secondsFromBeginTime, LocalDateTime localDateTime,Float level)
    {   this.localDateTime=localDateTime;
        this.secondsFromBeginTime=secondsFromBeginTime;
        this.level=level;
    }
}

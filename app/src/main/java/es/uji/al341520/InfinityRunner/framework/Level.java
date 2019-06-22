package es.uji.al341520.InfinityRunner.framework;

public enum Level
{
    EASY,
    MEDIUM,
    HARD;

    public boolean isEasy()
    {
        if(this == EASY)
        {
            return true;
        }
        return false;
    }

    public boolean isMedium()
    {
        if(this == MEDIUM)
        {
            return true;
        }
        return false;
    }

}

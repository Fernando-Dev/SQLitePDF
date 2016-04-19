package com.omarvm.pruebassqlite;

import java.util.Calendar;

/**
 * Created by userdebian on 3/23/16.
 */
public class RegistroSalida {
        int cHour, cMin,cSec,day,month,year,Intam_pm;
        String am_pm,FechaSalida;
        Calendar calendar;

    public String getSalida(){
        Calendar calendar = Calendar.getInstance();
        cHour = calendar.get(Calendar.HOUR);
        cMin = calendar.get(Calendar.MINUTE);
        cSec = calendar.get(Calendar.SECOND);

        Intam_pm = calendar.get(Calendar.AM_PM);
        if (Intam_pm == 0){am_pm = "AM";}else{am_pm = "PM";}

        day = calendar.get(Calendar.DATE);
        month = calendar.get(Calendar.MONTH)+1;
        year = calendar.get(Calendar.YEAR);

        FechaSalida = cHour + ":"+cMin+":"+cSec+" "+am_pm+" "+day+"/"+month+"/"+year;

        return FechaSalida;

    }
}

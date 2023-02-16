package com.example.firebasechat;

import android.util.Log;

public class Utils {
    long gap;
    long minutes;
    long hours;
    long days;
    long weeks;
    long months;
    long years;
    public Utils(long c,long p)
    {
        this.gap=c-p;
    }
    public String getString()
    {
       minutes= (gap/ 1000) / 60;
       Log.d("so",minutes+"");
       if(minutes>=60)
       {
           hours = minutes /60;
           if(hours>=24)
           {
               days = hours/24;
               if(days>=7)
               {
                  weeks= days/7;
                  if(weeks>=4)
                  {
                      months =weeks /4;
                      return Math.round(months) +" months ago";
                  }
                  else
                  {
                      return Math.round(weeks) +" weeks ago";
                  }
               }
               else
               {
                   return Math.round(days) +" days ago";
               }
           }
           else
           {
               return  Math.round(hours)+" hours ago";
           }

       }
       else if(minutes<1)
       {
           return "just now";
       }
       else
       {
           return Math.round(minutes)+" minutes ago";
       }

    }


}

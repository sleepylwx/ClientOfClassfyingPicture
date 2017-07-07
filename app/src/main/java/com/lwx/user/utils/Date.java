package com.lwx.user.utils;

/**
 * Created by 36249 on 2017/7/7.
 */

public class Date {

    public void judgeLeapYear(int year){


    }

    public int getMonthDayNum(int year,int month){

        if(month == 2){

            if(year%4 == 0 && year % 100 != 0 || year % 400 == 0){


                return 29;
            }else{

                return 28;
            }
        }
        else{

            switch (month){

                case 1:
                    return 31;
                case 3:
                    return 31;
                case 4:
                    return 30;
                case 5:
                    return 31;
                case 6:
                    return 30;
                case 7:
                    return 31;
                case 8:
                    return 31;
                case 9:
                    return 30;
                case 10:
                    return 31;
                case 11:
                    return 30;
                case 12:
                    return 31;

            }
        }

        return -1;
    }
}

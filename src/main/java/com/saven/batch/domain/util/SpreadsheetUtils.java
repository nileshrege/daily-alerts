package com.saven.batch.domain.util;

/**
 * Created by nrege on 1/30/2017.
 */
public class SpreadsheetUtils {

    public static String nextColumn(String current){
        double total = indexForColumn(current);
        int n = (int)total;
        return columnForIndex(n+1);
    }

    public static double indexForColumn(String current) {
        char []chr = current.toCharArray();
        double total=0;
        int pos = 0;
        for (int i=chr.length-1; i >= 0; i--){
            int num = chr[i] - 'A'+1;
            double val = Math.pow(26,pos)*num;
            total = val+total;
            pos++;
        }
        return total;
    }

    public static String columnForIndex(int n) {
        int str[] = new int[27];  // To store result (Excel column name)
        int i = 0;  // To store current index in str which is result
        while (n>0)
        {
            // Find remainder
            int rem = n%26;

            // If remainder is 0, then a 'Z' must be there in output
            if (rem==0)
            {
                str[i++] = 'Z';
                n = (n/26)-1;
            }
            else // If remainder is non-zero
            {
                str[i++] = (rem-1) + 'A';
                n = n/26;
            }
        }
        str[i] = '\0';
        StringBuffer result = new StringBuffer();
        for(int j=0; j< str.length; j++){
            if(str[j] != 0) {
                result.append(String.valueOf((char)(str[j])));
            }
        }
        return result.reverse().toString();
    }

}

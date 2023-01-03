package com.Eban.server.test;
import java.util.*;

/**
 * @description: 测试
 * @author: Libaoyun
 * @date: 2022-10-09 15:55
 **/
public class test {

    public static void main (String args[]){
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();//n个位置
        System.out.println(n);
        input.nextInt();
        int [] arr = new int[n];
        for(int i = 0; i<n; i++){
            arr[i] = input.nextInt();
        }
        int max = 1;
        for(int i = 1; i<n; i++){  //中心扩散法，0不用试了，max直接初始1即可
            int left = i - 1;
            int right = i + 1;
            int len = 1;
            while(left >= 0 && right < n){
                if(arr[left] > arr[left + 1] || arr[right] > arr[right -1]){
                    if(arr[left] > arr[left + 1]){
                        left--;
                        len += 1;
                    }
                    if(arr[right] > arr[right -1]){
                        right++;
                        len += 1;
                    }
                }else break;

            }
            max = Math.max(len, max);
        }
        System.out.println(max);
    }
}

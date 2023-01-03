package com.Eban.server.test;
import java.util.*;
/**
 * @description: 测试2
 * @author: Libaoyun
 * @date: 2022-10-09 16:26
 **/
public class testTwo {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = input.nextInt();//n个棋子
        int q = input.nextInt();
        int [] arr = new int[n];
        int result = 0;
        for (int i = 0; i < q; i++) {
            result = 0;
            int left = input.nextInt()-1;
            int right = input.nextInt()-1;
            for (; left <= right; left++){
                arr[left] = arr[left] == 0 ? 1 : 0;
            }
            for (int j = 0; j < n; j++) {
                if(arr[j] == 0){
                    result++;
                }
            }
            System.out.println(result);
        }

    }
}

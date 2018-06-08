package com.xcy.mvn;

import com.xcy.sort.HeapSort;
import com.xcy.sort.QuickSort;

import java.util.Arrays;

/**
 * Created by xuchunyang on 2018/6/7 14点36分
 */
public class QuickSortDemo {

    public static void main(String[] args) {

        int[] arr = {3,1,66,44,89,210,5,23};
//        QuickSort.quickSort(arr, 0, arr.length-1);

        HeapSort.heapSort(arr);

        System.out.println(Arrays.toString(arr));

    }
}

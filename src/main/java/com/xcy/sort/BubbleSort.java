package com.xcy.sort;

import java.util.Arrays;

/**
 * 冒泡排序
 *
 * 冒泡排序是一种简单的排序算法。它重复地走访过要排序的数列，一次比较两个元素，如果它们的顺序错误就把它们交换过来。走访数列的工作是重复地进行直到没有再需要交换，也就是说该数列已经排序完成。
 *
 * @author xcy
 *
 */
public class BubbleSort {

	public static void main(String[] args) {
		
		Integer[] array = new Integer[]{99,55,10,20,8,2,13,4,9,8,12,17,34,11};
		
		bubbleSort(array);
		
		System.out.println(Arrays.toString(array));
		
	}
	
	public static void bubbleSort(Integer[] arr) {
		
		int len = arr.length;
	    for (int i = 0; i < len; i++) {
	    	
			boolean swap = false;	// 是否执行了交换
	    	
	        for (int j = 0; j < len - 1 - i; j++) {
	            if (arr[j] > arr[j+1]) {        // 相邻元素两两对比
	                int temp = arr[j+1];        // 元素交换
	                arr[j+1] = arr[j];
	                arr[j] = temp;
	                swap = true;

//	                arr[j] = arr[j] + arr[j+1];
//					arr[j+1] = arr[j] - arr[j+1];
//					arr[j] = arr[j] - arr[j+1];

	            }
	        }
	        
	        if(!swap)
				return;
	    }
		
	}

}

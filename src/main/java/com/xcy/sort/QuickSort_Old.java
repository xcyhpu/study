package com.xcy.sort;

import java.util.Arrays;

/**
 * 快速排序
 * @author Xu Chunyang, 2016年10月24日
 *
 */
public class QuickSort_Old {
	
	public static void main(String[] args) {
		
		int[] array = new int[]{6, 2, 9, 3, 8, 7, 5, 1, 4};
//		int[] array = new int[]{99,55,10,20,8,2,13,4,9,8,12,17,34,11};
		
		sort(array, 0, array.length-1);
		
		System.out.println(Arrays.toString(array));
		
	}
	
	public static void sort(int[] a, int left, int right) {
	    if(left >= right) {
	        return ;
	    }

	    int i = left;
	    int j = right;
	    int key = a[left];
	     
	    while(i < j) {
	        while(i < j && key <= a[j])	j--;
	         
	        a[i] = a[j];	// 较小的扔到左边，可直接使用第i个位置，因为key已经存储了a[i]
	         
	        while(i < j && key >= a[i])	i++;
	         
	        a[j] = a[i];	// 较大的扔到右边，可直接使用第j个位置，因为上一步中已将a[j]存储到了a[i]
	    }
	    
	    
	    a[i] = key;		// 将本次的标记放到数组中间（第i个，也即第j个位置）
	    
	    sort(a, left, i - 1);
	    
	    sort(a, i + 1, right);

	}
	
}

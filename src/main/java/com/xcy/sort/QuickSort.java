package com.xcy.sort;

import java.util.Arrays;

/**
 * 快速排序
 *
 * 通过一趟排序将待排记录分隔成独立的两部分，其中一部分记录的关键字均比另一部分的关键字小；然后分别对这两部分记录继续进行排序，以达到整个序列有序
 *
 * @author ht_admin
 *
 */
public class QuickSort {
	
	public static void main(String[] args) {
		
//		Integer arr[] = {6,2,9,5,3,7,8,4,1,0};
		Integer arr[] = {3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
		
		quickSort(arr, 0, arr.length-1);
		
		System.out.println(Arrays.toString(arr));
		
	}
	
	public static void quickSort(Integer[] arr, int left, int right) {
	 
	    if (left < right) {
	        int partitionIndex = partition(arr, left, right);
	        
	        System.out.println("partitionValue : "+arr[partitionIndex]+"\t\t"+Arrays.toString(arr));
	        
	        /** 左分区排序 */
	        quickSort(arr, left, partitionIndex-1);
	        /** 右分区排序 */
	        quickSort(arr, partitionIndex+1, right);
	    }
	    
	}
	 
	public static int partition(Integer[] arr, int begin, int end) {     // 分区操作
		
		int pivot = begin;                      // 设定基准值（pivot）
		int index = pivot + 1;
	    for (int i = index; i <= end; i++) {
	        if (arr[i] < arr[pivot]) {
	            swap(arr, i, index);
	            index++;
	        }       
	    }
	    swap(arr, pivot, index - 1);
	    return index-1;
	    
	}
	 
	public static void swap(Integer[] arr, int i, int j) {
	    int temp = arr[i];
	    arr[i] = arr[j];
	    arr[j] = temp;
	}

}
 
 

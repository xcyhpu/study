package com.xcy.sort;

import java.util.Arrays;

/**
 * 归并排序
 *
 * 归并排序是建立在归并操作上的一种有效的排序算法。该算法是采用分治法（Divide and Conquer）的一个非常典型的应用。将已有序的子序列合并，得到完全有序的序列；即先使每个子序列有序，再使子序列段间有序。
 *
 * @author xcy
 *
 */
public class MergeSort {
	
	public static void main(String[] args) {
		
		
		Integer arr[] = {3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
		
//		Integer arr[] = {5, 1};
		
		Integer[] mergeSort = mergeSort(arr);
		
		System.out.println(Arrays.toString(mergeSort));
		
	}
	
	public static Integer[] mergeSort(Integer[] arr) {
		 
		if(arr.length < 2) {
			return arr;
		}
		
		int middle = arr.length / 2;
		
		
		Integer[] left = Arrays.copyOfRange(arr, 0, middle);
		Integer[] right = Arrays.copyOfRange(arr, middle, arr.length);
		
		return merge(mergeSort(left), mergeSort(right));
		
		
	}
	
	private static Integer[] merge(Integer[] left, Integer[] right) {
		
		Integer[] result = new Integer[left.length + right.length];
		
		int leftCursor = 0;
		int rightCursor = 0;
		
		while(leftCursor<left.length && rightCursor<right.length) {
			
			if(left[leftCursor]<right[rightCursor]) {
				result[leftCursor+rightCursor] = left[leftCursor];
				leftCursor ++ ;
			} else {
				result[leftCursor+rightCursor] = right[rightCursor];
				rightCursor ++ ;
			}
			
		}
		
		while(leftCursor<left.length) {
			result[leftCursor+rightCursor] = left[leftCursor];
			leftCursor ++;
		}
		
		while(rightCursor<right.length) {
			result[leftCursor+rightCursor] = right[rightCursor];
			rightCursor ++;
		}
		
		
		return result;
	}

}

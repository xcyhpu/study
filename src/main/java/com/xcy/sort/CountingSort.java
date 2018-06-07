package com.xcy.sort;

import java.util.Arrays;

/**
 * 计数排序
 *
 * 计数排序不是基于比较的排序算法，其核心在于将输入的数据值转化为键存储在额外开辟的数组空间中。 作为一种线性时间复杂度的排序，计数排序要求输入的数据必须是有确定范围的整数。
 *
 * @author Xu Chunyang, 2018年05月04日
 *
 */
public class CountingSort {
	
	public static void main(String[] args) {
		
		Integer arr[] = {3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
		
		countingSort(arr);
		
		System.out.println(Arrays.toString(arr));
		
	}
	
	public static void countingSort(Integer[] a) {
		
		/** 最大值 */
		int maxValue = a[0];
		for(int i=1;i<a.length;i++) {
			if(maxValue<a[i]) {
				maxValue = a[i];
			}
		}
		
		/** 初始化一个临时数组，每个元素都是0 */
		int[] bucket = new int[maxValue+1];
		
		for(int i=0;i<a.length;i++) {
			
			bucket[a[i]] = bucket[a[i]] + 1;
			
		}
		
		/** 将bucket中的数放到原始数组中 */
		int sortedInex = 0;
		for(int i=0;i<maxValue+1;i++) {
			
			if(bucket[i]>0) {
				
				a[sortedInex++] = i;
				
			}
			
		}
		
	}

}

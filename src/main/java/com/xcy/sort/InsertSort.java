package com.xcy.sort;

import java.util.Arrays;

/**
 * 直接插入排序
 *
 * 通过构建有序序列，对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入
 *
 *
 * @author Xu Chunyang, 2016年10月12日
 *
 */
public class InsertSort {
	
	public static void main(String[] args) {
		
		Integer arr[] = {3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
		
		insertSort(arr);
		
		System.out.println(Arrays.toString(arr));
		
	}
	
	public static void insertSort(Integer[] array) {
		
		if(array == null || array.length < 2)
			return;
		
		int i,j = 0,size=array.length;
		int insertValue;		// 待插入到有序数组的值
		
		for(i=1;i<size;i++) {	// 向后遍历
			
			insertValue = array[i];
			
			for(j=i-1;j>=0;j--) {	// 向前查找
				
				if(array[j] > insertValue) {	// 较大值后移一位
					
					array[j+1] = array[j];
					
				} else {		// 遇到较小的停止查找
					
					break;
					
				}
				
			}
			
			// 将新元素插入到该位置后
			array[j+1] = insertValue;
		}
	}

}

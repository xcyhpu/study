package com.xcy.sort;

import java.util.Arrays;

/**
 * 希尔排序
 *
 * 1959年Shell发明，第一个突破O(n^2)的排序算法，是直接插入排序的改进版。它与插入排序的不同之处在于，它会优先比较距离较远的元素。希尔排序又叫缩小增量排序。
 *
 * @author Xu Chunyang, 2016年10月24日
 *
 */
public class ShellSort {

	public static void main(String[] args) {

		Integer[] array = new Integer[]{99,55,10,20,8,2,13,4,9,8,12,17,34,11};

		shellSort(array);

		System.out.println(Arrays.toString(array));

	}

	public static void shellSort(Integer[] a) {
		double d1 = a.length;
		int temp = 0;
		
		while (true) {	//TODO 逐渐减小步长，直至到步长为1
			
			d1 = Math.ceil(d1 / 2);
			int d = (int) d1;
			
			// 以第一个步长内的所有元素为起始位置，以步长递增，分组对该数组所有元素执行直接插入排序
			for (int x = 0; x < d; x++) {
							
				// 直接插入排序
				for (int i = x + d; i < a.length; i += d) {
					int j = i - d;
					temp = a[i];
					for (; j >= 0 && temp < a[j]; j -= d) {
						a[j + d] = a[j];
					}
					a[j + d] = temp;
				}
			}
			if (d == 1)
				break;
		}
	}

	//Wiki
	public static void shell_sort(int[] arr) {
		int gap = 1, i, j, len = arr.length;
		int temp;
		while (gap < len / 3)
			gap = gap * 3 + 1; // <O(n^(3/2)) by Knuth,1973>: 1, 4, 13, 40, 121, ...
		for (; gap > 0; gap /= 3)
			for (i = gap; i < len; i++) {
				temp = arr[i];
				for (j = i - gap; j >= 0 && arr[j] > temp; j -= gap)
					arr[j + gap] = arr[j];
				arr[j + gap] = temp;
			}
	}
}

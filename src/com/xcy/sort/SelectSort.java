package com.xcy.sort;

import java.util.Arrays;

/**
 * 选择排序
 *
 * 首先在未排序序列中找到最小元素，存放到排序序列的起始位置，然后，再从剩余未排序元素中继续寻找最小元素，然后放到已排序序列的末尾。以此类推，直到所有元素均排序完毕。
 *
 * @author xcy
 *
 */
public class SelectSort {

	public static void main(String[] args) {

		Integer[] array = new Integer[]{3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};

		selectSort(array);

		System.out.println(Arrays.toString(array));

	}

	public static void selectSort(Integer[] arr) {

		int len = arr.length;
		int minIndex;
		for (int i = 0; i < len - 1; i++) {
			minIndex = i;
			// 保存最小数的索引
			for (int j = i + 1; j < len; j++) {
				if (arr[j] < arr[minIndex]) {
					minIndex = j;
				}
			}

			swap(arr, i, minIndex);
		}

	}

	public static void swap(Integer[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

}
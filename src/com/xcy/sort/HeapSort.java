package com.xcy.sort;

import java.util.Arrays;

/**
 * 堆排序
 *
 * 堆排序(Heapsort)是指利用堆积树（堆）这种数据结构所设计的一种排序算法。可以利用数组的特点快速定位指定索引的元素。
 * 堆分为大根堆和小根堆，是完全二叉树。大根堆的要求是每个节点的值都不大于其父节点的值，即A[PARENT[i]] >= A[i]。
 * 在数组的非降序排序中，需要使用的就是大根堆，因为根据大根堆的要求可知，最大的值一定在堆顶。
 *
 * @author Xu Chunyang, 2016年10月24日
 *
 */
public class HeapSort {


	public static void main(String[] args) {

		int[] array = new int[]{99,55,10,20,8,2,13,4,9,8,12,17,34,11};

		heapSort(array);

		System.out.println(Arrays.toString(array));

	}
	
	/**
	 * 将数组创建为一个大顶堆
	 * @param data
	 */
	private static void buildMaxHeap(int[] data) {
		int heapSize = data.length;
//		if(heapSize < 2)
//			return;
		// 从最后一个非叶子节点开始
		int startIndex = (heapSize - 2) >> 1;
		// 从尾端开始创建最大堆，每次都是正确的堆
		for (int i = startIndex; i >= 0; i--) {
			ajustHeap(data, heapSize, i);
		}
	}
	
	
	/**
	 * 从数组a的第index元素，将其调整为大顶堆
	 * @param a	待调整的数组（堆）
	 * @param depth	堆深度，最小值为1
	 * @param index	排序开始位置，最小值为0
	 */
	public static void ajustHeap(int[] a, int depth, int index) {
		
		if(depth < 2)
			return;
		
		int leftChild = (index<<1) + 1;
		int rightChild = (index<<1) + 2;
		
		int largest = index;
		
		
		/** 跟左右子节点比较，将三者中的最大值作为父节点 */
		if(leftChild<depth && a[largest]<a[leftChild]) {
			largest = leftChild;
		}
		if(rightChild<depth && a[largest]<a[rightChild]) {
			largest = rightChild;
		}
		if(largest!=index) {
			int temp = a[index];
			a[index] = a[largest];
			a[largest] = temp;
			
			/** 父子节点交换后，子节点的子节点可能比子节点大了，所以要从该子节点位置开始重新调整 */
			ajustHeap(a, depth, largest);
		}

	}

	public static void heapSort(int[] array) {

		buildMaxHeap(array);

		for(int i=array.length-1;i>=0;i--) {
			// 头尾交换，保证最大值始终在最后，循环结束即是一个升序数组
			int temp = array[i];
			array[i] = array[0];
			array[0] = temp;
			ajustHeap(array, i, 0);
		}

	}
	
}

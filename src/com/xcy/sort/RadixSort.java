package com.xcy.sort;

import java.util.Arrays;

/**
 * 基数排序
 *
 * 基数排序是按照低位先排序，然后收集；再按照高位排序，然后再收集；依次类推，直到最高位。有时候有些属性是有优先级顺序的，先按低优先级排序，再按高优先级排序。最后的次序就是高优先级高的在前，高优先级相同的低优先级高的在前。
 *
 * 参考网址：https://baike.baidu.com/item/%E5%9F%BA%E6%95%B0%E6%8E%92%E5%BA%8F/7875498?fr=aladdin
 * @author Xu Chunyang, 2018年05月04日
 *
 */
public class RadixSort {
	
	public static void main(String[] args) {
		
		Integer arr[] = {3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
		
		radixSort(arr);
		
		System.out.println(Arrays.toString(arr));
		
	}
	
	public static void radixSort(Integer[] a) {
		
		/** 找到最大值 */
		int maxValue = a[0];
		for(int i=1;i<a.length;i++) {
			if(maxValue<a[i]) {
				maxValue = a[i];
			}
		}
		
		int len = a.length;
		
		/**  取余的位数（个位除以1后对10取余，十位除以10后对10取余，百位除以100后对10取余）  */
		int n = 1;
		
		/**  数组的第一维表示可能的余数（0-9*）*/
		int[][] temp = new int[10][len];
		/** 每个余数有多少个数，如例子中6对应有三个数36，26，46，表示为numbers[6]=3 */
		int[] numbers = new int[10];
		
		while(n <= maxValue) {
			
			for(int i=0;i<len;i++) {
				
				/** 当前位（个/十/百）上的数字 */
				int mod = (a[i] / n) % 10;
				
				temp[mod][numbers[mod]] = a[i];
				
				numbers[mod] ++ ;
				
			}
			
			
			/** 将temp中的数字按本轮排序后的结果，重新放到原始数组中 */
			
			int index = 0;
			
			for(int i=0;i<10;i++) {
				
				if(temp[i][0]!=0) {
					
					/** 从numbers中取出个位数为i的数的个数  */
					for(int j=0,count=numbers[i];j<count;j++) {
						a[index++] = temp[i][j];
						/** 给temp和numbers数组复位，以提供给后一位的计数排序 */
						temp[i][j] = 0;
						numbers[i] = 0;
					}
					
				}
				
			}
			
			/** 位数调大一位 */
			n *= 10;
		}
		
	}

}

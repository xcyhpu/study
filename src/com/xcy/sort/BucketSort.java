package com.xcy.sort;

import java.util.Arrays;

/**
 * 桶排序
 * @author Xu Chunyang, 2018年05月04日
 *
 */
public class BucketSort {
	
	public static void main(String[] args) {
		
		Integer arr[] = {3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48};
		
		bucketSort(arr);
		
		System.out.println(Arrays.toString(arr));
		
	}
	
	
	public static void bucketSort(Integer data[]) {
		int n = data.length;
		int buck[][] = new int[10][n];
		int index[] = new int[10];
		
		/** 确定最高位数 */
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < n; i++) {
			max = max > (Integer.toString(data[i]).length()) ? max : (Integer.toString(data[i]).length());
		}
		
		
		/** 从低位到高位，按位排序 */
		String str;
		for (int i = max - 1; i >= 0; i--) {
			for (int j = 0; j < n; j++) {
				
				/** 每个数字前补0，保证和最高位一样长 */
				str = "";
				if (Integer.toString(data[j]).length() < max) {
					for (int k = 0; k < max - Integer.toString(data[j]).length(); k++)
						str += "0";
				}
				str += Integer.toString(data[j]);
				
				
				buck[str.charAt(i) - '0'][index[str.charAt(i) - '0']++] = data[j];
			}
			int pos = 0;
			for (int j = 0; j < 10; j++) {
				for (int k = 0; k < index[j]; k++) {
					data[pos++] = buck[j][k];
				}
			}
			for (int x = 0; x < 10; x++)
				index[x] = 0;
			
			System.out.println("第"+(max-i)+"位排完之后的结果:"+Arrays.toString(data));
			
		}
	}
	

}

package org.example.youkushengshi.num3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * @author yjz
 * @Date 2025/9/10 15:23
 * @Description
 *
 */
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] firstLine = br.readLine().split(" ");
        int n = Integer.parseInt(firstLine[0]);
        long k = Long.parseLong(firstLine[1]);
        
        int[] a = new int[n];
        String[] secondLine = br.readLine().split(" ");
        for(int i = 0; i < n; i++){
            a[i] = Integer.parseInt(secondLine[i]);
        }
        
        Arrays.sort(a);
        
        long[] prefix = new long[n+1];
        for(int i = 0; i < n; i++){
            prefix[i+1] = prefix[i] + a[i];
        }
        
        int left = 1, right = n;
        int ans= 1;
        
        while (left <= right){
            int mid = left + (right - left) / 2;
            boolean ok = false;
            
            for(int i = 0; i <= n - mid; i++){
                int j = i + mid - 1;
                int midIdx = i + (mid-1) / 2;
                
                long leftCount = midIdx - i + 1;
                long leftSum = prefix[midIdx+1] - prefix[i];
                long leftCost = a[midIdx]*leftCount - leftSum;
                
                long rightCount = j - midIdx;
                long rightSum = prefix[j+1] - prefix[midIdx+1];
                long rightCost = rightSum - a[midIdx]*rightCount;
                
                long totalCost = leftCost + rightCost;
                
                if(totalCost <= k){
                    ok = true;
                    break;
                }
            }
            if(ok){
                ans = mid;
                left = mid + 1;
            }else {
                right = mid - 1;
            }
        }
        pw.println(ans);
        pw.flush();
    }
}

package org.example.youkushengshi.num2;

import java.util.Arrays;
import java.util.Scanner;

/**
 * @author yjz
 * @Date 2025/9/10 15:04
 * @Description
 *
 */
public class Main {
    
    static class Move{
        int player;
        int pos;
        public Move(int player, int pos) {
            this.player = player;
            this.pos = pos;
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        while (T-- > 0) {
            int n = sc.nextInt();
            Move[] moves = new Move[n];
            for (int i = 0; i < n; i++) {
                int c = sc.nextInt();
                int a = sc.nextInt();
                moves[i] = new Move(c, a);
            }

            int[] dp = new int[n];
            Arrays.fill(dp, 1);
            int ans = 1;
            
            for(int i = 1; i < n; i++){
                for(int j = 0; j < i; j++){
                    if(moves[i].player != moves[j].player && moves[i].pos != moves[j].pos){
                        dp[i] = Math.max(dp[i], dp[j] + 1);
                    }
                }
                ans = Math.max(ans, dp[i]);
            }
            System.out.println(ans);
        }
        sc.close();
    }
}

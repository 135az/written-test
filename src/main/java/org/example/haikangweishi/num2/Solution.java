package org.example.haikangweishi.num2;



public class Solution {
    public ListNode mergeNode (ListNode head) {
        // write code here
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        
        while (head != null && head.next != null){
            int product = head.val * head.next.val;
            cur.next = new ListNode(product);
            cur = cur.next;
            head = head.next.next;
        }
        return dummy.next;
    }
}
package src;

import java.util.ArrayList;
import java.util.List;

public class Main {

    static class SkipList {
        final private int MAX_LEVEL = 3;
        SkipNode head;
        SkipNode tail;
        SkipList(int maxLevel) {
            this.head = new SkipNode(Integer.MIN_VALUE, null, null);
            this.tail = new SkipNode(Integer.MAX_VALUE, null, null);
            this.head.right = tail;
            setupSkipLevels();
        }

        /**
         * Set up MAX_LEVELS for the skip list
         * the setup consists of head and tail nodes with K levels
         *          H --> T
         *          |     |
         *          H --> T
         *          |     |
         *          H --> T
         */
        private void setupSkipLevels() {
            SkipNode currLevelHead = this.head, currLevelTail = this.tail;
            for (int i = 0; i < MAX_LEVEL - 1; i++) {
                SkipNode sentinelHead = new SkipNode(Integer.MIN_VALUE, null, null);
                SkipNode sentinelTail = new SkipNode(Integer.MAX_VALUE, null, null);
                sentinelHead.right = sentinelTail;

                currLevelHead.down = sentinelHead;
                currLevelTail.down = sentinelTail;

                currLevelHead = sentinelHead;
                currLevelTail = sentinelTail;
            }
        }

        /**
         *
         * @param e element to be deleted
         * @return boolean
         */
        public boolean remove(int e) {
            SkipNode curr = this.head;
            // For every level move horizontal find the node and remove it
            // move down to new level
            boolean deleted = false;
            while(curr != null) {
                while(curr.right != null && curr.right.value < e) {
                    curr = curr.right;
                }

                while(curr.right != null && curr.right.value == e) {
                    deleted = true;
                    SkipNode temp = curr.right.right;
                    curr.right = temp;
                }
                curr = curr.down;
            }

            return deleted;
        }

        public boolean search(int e) {
            SkipNode curr = this.head;
            while(curr != null) {
                while(curr.right != null && curr.right.value < e) {
                    curr = curr.right;
                }
                if(curr.right != null && curr.right.value == e) {
                    return true;
                }
                curr = curr.down;
            }

            return false;
        }

        public void add(int ele) {
            List<SkipNode> levelNodes = new ArrayList<SkipNode>();

            SkipNode curr = this.head;
            // Find the exact position where we need to add the node
            while(curr != null) {
                while(curr.right != null && curr.right.value < ele) {
                    curr = curr.right;
                }
                levelNodes.add(curr);
                curr = curr.down;
            }

            int levelsForNode = MAX_LEVEL - 1; // level as index hence minus 1
            // deciding max level for the incoming node/element
            // > 1 ensures the node is always present at the zeroth level
            while(Math.random() > 0.5 && levelsForNode > 1) levelsForNode--;
            // start from the bottom node which means we iterate array in reverse way
            SkipNode nodeToAdded = null;

            int len = levelNodes.size();
            for (int i = (len - 1 - levelsForNode); i < len; i++) {
                if(nodeToAdded == null) {
                    nodeToAdded = new SkipNode(ele, null, null);
                }
                SkipNode nodeOnLeft = levelNodes.get(i);
                System.out.println("Level = " + i + nodeOnLeft);
                SkipNode nodeOnRight = nodeOnLeft.right;
                nodeOnLeft.right = nodeToAdded;
                nodeToAdded.right = nodeOnRight;
                if(i != len - 1) {
                    SkipNode nodeOnDown = new SkipNode(ele, null, null);
                    nodeToAdded.down = nodeOnDown;
                    nodeToAdded = nodeOnDown;
                }
            }
        }

//        /**
//         * Use it for debugging
//         */
//        public void print() {
//            if(head == null || head.right == null) {
//                System.out.println("Skip List is null");
//            }
//            SkipNode verticalCurr = head;
//            while(verticalCurr != null) {
//                SkipNode horizontalCurr = verticalCurr;
//                while(horizontalCurr != null) {
//                    int downVal = -1;
//                    if(horizontalCurr.down != null) {
//                        downVal = horizontalCurr.down.value;
//                    }
//                    System.out.print(horizontalCurr.value + " - " + horizontalCurr + " " + "d(" + downVal + " " + horizontalCurr.down + ")" + " --> ");
//                    horizontalCurr = horizontalCurr.right;
//                }
//                System.out.println();
//                verticalCurr = verticalCurr.down;
//            }
//        }
    }

    /**
     * SkipNode class for nodes within skip list
     */
    static class SkipNode {
        int value;
        SkipNode right, down;

        SkipNode(int value, SkipNode right, SkipNode down) {
            this.value = value;
            this.right = right;
            this.down = down;
        }
    }

    public static void main(String[] args) {
        SkipList skiplist = new SkipList(5);
        skiplist.add(10);
        skiplist.add(9);
        skiplist.add(99);
        skiplist.add(88);
        skiplist.add(9);
        skiplist.add(9);
//        skiplist.print();
        skiplist.remove(9);
//        skiplist.print();
        System.out.println(skiplist.search(9));
    }
}

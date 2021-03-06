public class Solution {
    public List<int[]> getSkyline(int[][] buildings) {
        List<int[]> heights = new ArrayList();
        for (int[] b : buildings) {
            heights.add(new int[]{b[0], -b[2]}); // l, h
            heights.add(new int[]{b[1], b[2]}); // r, h
        }
        Collections.sort(heights, (a, b) -> (a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]));
        TreeMap<Integer,Integer> map = new TreeMap();
        map.put(0, 1); // height : count
        int pre = 0; // preheight
        List<int[]> res = new ArrayList();
        for (int[] h : heights) {
            if (h[1] < 0) { // if it's left, count++
                map.compute(-h[1], (key, value) -> {
                    if (value != null) return value + 1;
                    return 1;
                });
            } else { // r, h
                map.compute(h[1], (key, value) -> {
                    if (value == 1) return null;
                    return value - 1;
                });
            }
            // the last key is the biggest height
            int cur = map.lastKey(); // current height
            
            // if the curheight is different with pre height, which means height is changed, than add this curheight to res
            if (cur != pre) {
                res.add(new int[]{h[0], cur});
                pre = cur;
            }
        }
        return res;
    }
}

// divide and conquer

public class Solution {
    public List<int[]> getSkyline(int[][] buildings) {
        if (buildings.length == 0 || buildings[0].length == 0)
            return new ArrayList<int[]>();
        return recurSkyline(buildings, 0, buildings.length - 1);
    }
    
    private LinkedList<int[]> recurSkyline(int[][] buildings, int l, int r) {
        if (l < r) {
            int mid = l + (r - l)/2;
            return merge(recurSkyline(buildings, l, mid), recurSkyline(buildings, mid+1, r));
        } else {
            LinkedList<int[]> res = new LinkedList<>();
            res.add(new int[]{buildings[r][0], buildings[r][2]});
            res.add(new int[]{buildings[r][1], 0});
            return res;
        }
    }
    
    private LinkedList<int[]> merge(LinkedList<int[]> l1, LinkedList<int[]> l2) {
        LinkedList<int[]> res = new LinkedList<>();
        int h1 = 0, h2 = 0;
        while (l1.size() > 0 && l2.size() > 0) {
            int x = 0, h = 0;
            if (l1.getFirst()[0] < l2.getFirst()[0]) {
                x = l1.getFirst()[0];
                h1 = l1.getFirst()[1];
                h = Math.max(h1, h2);
                l1.removeFirst();
            } else if (l1.getFirst()[0] > l2.getFirst()[0]) {
                x = l2.getFirst()[0];
                h2 = l2.getFirst()[1];
                h = Math.max(h2, h1);
                l2.removeFirst();
            } else {
                x = l1.getFirst()[0];
                h1 = l1.getFirst()[1];
                h2 = l2.getFirst()[1];
                h = Math.max(h1, h2);
                l1.removeFirst();
                l2.removeFirst();
            }
            if (res.size() == 0 || h != res.getLast()[1]) {
                res.add(new int[]{x, h});
            }
        }
        res.addAll(l1);
        res.addAll(l2);
        return res;
    }
}

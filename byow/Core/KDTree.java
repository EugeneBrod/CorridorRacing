package byow.Core;

import java.util.List;

public class KDTree implements PointSet {

    private class Node2 {
        Node2 parent;
        Point point;
        Node2 smaller;
        Node2 bigger;
        int dim;
        double [] axis;

        Node2(Node2 parent, Point point, int dim, Node2 smaller, Node2 bigger) {
            this.parent = parent;
            this.point = point;
            this.dim = dim;
            axis = new double[2];
            axis[0] = point.getX();
            axis[1] = point.getY();
            this.smaller = smaller;
            this.bigger = bigger;
        }

        double getpos() {
            return axis[dim];
        }

        Node2[] goodSidebadSide(Node2 curr, Node2 target) {
            Node2[] result = new Node2[2];
            if (target.axis[curr.dim] >= curr.axis[curr.dim]) {
                result[0] = curr.bigger;
                result[1] = curr.smaller;
                return result;
            }
            result[0] = curr.smaller;
            result[1] = curr.bigger;
            return result;
        }

        double axisdistancetotarget(Node2 curr, Node2 target) {
            return Math.abs(curr.axis[curr.dim] - target.axis[curr.dim]);
        }

        void setChild(Node2 a) {
            if (this.axis[this.dim] <= a.axis[this.dim]) {
                if (this.bigger != null) {
                    this.bigger.setChild(a);
                    return;
                }
                this.bigger = a;
                this.bigger.dim = (this.dim + 1) % 2;
                this.bigger.parent = this;
                return;
            }
            if (this.smaller != null) {
                this.smaller.setChild(a);
                return;
            }
            this.smaller = a;
            this.smaller.dim = (this.dim + 1) % 2;
            this.smaller.parent = this;
        }

        private double distanceTo(Node2 target) {
            return Point.distance(this.point, target.point);
        }

        private Node2 findClosest(Node2 curr, Node2 target, Node2 best) {
            if (curr == null) {
                return best;
            }
            if (curr.distanceTo(target) <= best.distanceTo(target)) {
                best = curr;
            }

            Node2[] goodSidebadSide = goodSidebadSide(curr, target);
            best = findClosest(goodSidebadSide[0], target, best);

            if (best.distanceTo(target) >= axisdistancetotarget(curr, target)) {
                best = findClosest(goodSidebadSide[1], target, best);
            }
            return best;
        }
    }

    private Node2 root;
    private int size;

    public KDTree(List<Point> points) {
        root = new Node2(null, points.remove(0), 0, null, null);
        size = 1;
        int j = points.size();
        for (int i = 0; i < j; i++) {
            Node2 n = new Node2(null, points.remove(0), 0, null, null);
            size += 1;
            root.setChild(n);
        }
    }


    @Override
    public Point nearest(double x, double y) {
        Point point = new Point(x, y);
        Node2 target = new Node2(null, point, 0, null, null);
        Node2 result = root.findClosest(root, target, root);
        return result.point;
    }
}

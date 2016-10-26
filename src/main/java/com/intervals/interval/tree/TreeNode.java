package com.intervals.interval.tree;

import com.intervals.interval.PersonalInterval;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

    private TreeNode left;
    private TreeNode right;
    private List<PersonalInterval> intervals = new ArrayList<>();
    private long vertex;
    private long minStart;
    private long maxEnd;

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public List<PersonalInterval> getIntervals() {
        return new ArrayList<>(intervals);
    }

    public TreeNode getLeft() {
        return left;
    }

    public TreeNode getRight() {
        return right;
    }

    public long getVertex() {
        return vertex;
    }

    public TreeNode(TreeNode left, TreeNode right, PersonalInterval interval, long vertex) {
        this.left = left;
        this.right = right;
        this.intervals.add(interval);
        this.vertex = vertex;
        this.minStart = interval.getStartMillis();
        this.maxEnd = interval.getEndMillis();
    }

    public TreeNode(long vertex) {
        this.vertex = vertex;
    }

    public TreeResult intersect(PersonalInterval i) {
        TreeResult result = new TreeResult();

        for (PersonalInterval interval : intervals) {
            result.and(interval.getStartMillis() <= i.getStartMillis() && interval.getEndMillis() >= i.getEndMillis(), interval.getOwner());
        }

        return result;
    }

    public void addIntervals(List<PersonalInterval> intervals) {
        for (PersonalInterval i : intervals) {
            if (vertex >= i.getStartMillis() && vertex <= i.getEndMillis()) {
                this.intervals.add(i);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TreeNode node = (TreeNode) o;

        if (maxEnd != node.maxEnd) return false;
        if (minStart != node.minStart) return false;
        if (vertex != node.vertex) return false;
        if (intervals != null ? !intervals.equals(node.intervals) : node.intervals != null) return false;
        if (left != null ? !left.equals(node.left) : node.left != null) return false;
        if (right != null ? !right.equals(node.right) : node.right != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        result = 31 * result + (intervals != null ? intervals.hashCode() : 0);
        result = 31 * result + (int) (vertex ^ (vertex >>> 32));
        result = 31 * result + (int) (minStart ^ (minStart >>> 32));
        result = 31 * result + (int) (maxEnd ^ (maxEnd >>> 32));
        return result;
    }
}


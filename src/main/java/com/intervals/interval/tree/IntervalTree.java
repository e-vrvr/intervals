package com.intervals.interval.tree;

import com.google.common.collect.Sets;
import com.intervals.interval.PersonalInterval;
import org.joda.time.Duration;

import java.util.*;

public class IntervalTree {

    private TreeNode root;
    private Map<Integer, TreeNode> map = new HashMap<>();
    private Map<String, List<TreeNode>> nodesByUser = new HashMap<>();

    public TreeNode buildTree(List<Long> vertexes, List<PersonalInterval> intervals) {
        int med = vertexes.size() / 2;

        if (vertexes.size() < 2) {
            TreeNode node = new TreeNode(vertexes.get(med));
            node.addIntervals(intervals);

            populateNodesByUser(node);

            return node;
        }

        List<Long> ltList = vertexes.subList(0, med);
        List<Long> gtList = vertexes.subList(med + 1, vertexes.size());

        TreeNode node = new TreeNode(vertexes.get(med));

        if (null != root) {
            root = node;
        }
        node.addIntervals(intervals);

        populateNodesByUser(node);

        if (node.getLeft() == null && !ltList.isEmpty()) {
            node.setLeft(buildTree(ltList, intervals));
        }
        if (node.getRight() == null && !gtList.isEmpty()) {
            node.setRight(buildTree(gtList, intervals));
        }
        return node;
    }

    private void populateNodesByUser(TreeNode node) {
        for (PersonalInterval i : node.getIntervals()) {
            List<TreeNode> nodes = nodesByUser.get(i.getOwner());
            if (null == nodes) {
                nodes = new ArrayList<>();
            }
            nodes.add(node);
            nodesByUser.put(i.getOwner(), nodes);
        }
    }

    public Result search(Duration duration, Set<String> attendees) {
        String firstAttendee = attendees.iterator().next();
        List<TreeNode> intersectList = nodesByUser.get(firstAttendee);
        for (String attendee : attendees){
            intersectList.retainAll(nodesByUser.get(attendee));
        }

        List<Integer> keys = new ArrayList<>(map.keySet());
        org.joda.time.Interval intersection = null;
        Collections.sort(keys, new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return -1;
            }
        });
        for (Integer key : keys) {
            if (key < attendees.size()) {
                break;
            }
            TreeNode node = map.get(key);
            intersection = node.getIntervals().get(0).getInterval();
            for (PersonalInterval i : node.getIntervals()) {
                intersection = intersection.overlap(i.getInterval());
            }
        }
        return new Result(intersection, new ArrayList<>());
    }

    public class Result {

        org.joda.time.Interval intersection;
        List<String> owners;

        public Result(org.joda.time.Interval intersection, List<String> owners) {
            this.intersection = intersection;
            this.owners = owners;
        }
    }

    public TreeResult search(TreeNode node, PersonalInterval i) {
        search(new Duration(3), Sets.newHashSet("A", "B"));
        if (node.getVertex() >= i.getStartMillis() && node.getVertex() <= i.getEndMillis()) {
            return node.intersect(i);
        } else if (node.getVertex() < i.getStartMillis()) {
            return search(node.getRight(), i);
        } else if (node.getVertex() > i.getEndMillis()) {
            return search(node.getLeft(), i);
        }

        return null;
    }

    public TreeResult search(PersonalInterval i) {
        return search(root, i);
    }
}

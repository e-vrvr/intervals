package com.intervals.interval;

import com.intervals.interval.tree.IntervalTree;
import com.intervals.interval.tree.TreeNode;
import com.intervals.interval.tree.TreeResult;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class IntervalTreeTest {

    private IntervalTree tree = new IntervalTree();
    private TreeNode root;

    @Before
    public void setUp() {
        PersonalInterval i1 = new PersonalInterval("A", 0, 100);
        PersonalInterval i2 = new PersonalInterval("B", 0, 60);
        PersonalInterval i3 = new PersonalInterval("C", 40, 120);
        PersonalInterval i4 = new PersonalInterval("D", 50, 90);
        PersonalInterval i5 = new PersonalInterval("E", 0, 200);

        List<PersonalInterval> intervals = Arrays.asList(i1, i2, i3, i4, i5);
        Set<Long> vertexes = new HashSet<>();

        for (PersonalInterval i : intervals) {
            vertexes.add(i.getStartMillis());
            vertexes.add(i.getEndMillis());
        }

        List<Long> sorted = new ArrayList(vertexes);
        Collections.sort(sorted);

        root = tree.buildTree(sorted, intervals);
    }

    @Test
    public void shouldReturnResultsOfDurationSearching() throws IOException {
        // given

        // when
        TreeResult result = tree.search(root, new PersonalInterval(0, 30));

        // then
        assertThat(result.getResult(), equalTo(true));
        assertThat(result.getOwners(), hasSize(3));
        assertThat(result.getOwners(), containsInAnyOrder("A", "B", "E"));

        // when
        TreeResult result3 = tree.search(root, new PersonalInterval(0, 300));

        // then
        assertThat(result3.getResult(), equalTo(false));
        assertThat(result3.getOwners(), hasSize(0));
    }

    @Test
    public void shouldReturnResultsOfIntervalSearching() throws IOException {
        // given

        // when
        TreeResult result = tree.search(root, new PersonalInterval(0, 30));

        // then
        assertThat(result.getResult(), equalTo(true));
        assertThat(result.getOwners(), hasSize(3));
        assertThat(result.getOwners(), containsInAnyOrder("A", "B", "E"));

        // when
        TreeResult result2 = tree.search(root, new PersonalInterval(50, 60));

        // then
        assertThat(result2.getResult(), equalTo(true));
        assertThat(result2.getOwners(), hasSize(5));
        assertThat(result2.getOwners(), containsInAnyOrder("A", "B", "E", "C", "D"));

        // when
        TreeResult result3 = tree.search(root, new PersonalInterval(0, 300));

        // then
        assertThat(result3.getResult(), equalTo(false));
        assertThat(result3.getOwners(), hasSize(0));
    }
}

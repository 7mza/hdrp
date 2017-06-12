package ujm.wi.m1.algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import ujm.wi.m1.db.Entry;
import ujm.wi.m1.util.Util;

@Component
public class Knn {

	private class Comparison implements Comparable<Comparison> {

		@Getter
		@Setter
		private int distance;

		@Getter
		@Setter
		private int value;

		public Comparison(int distance, int value) {
			this.distance = distance;
			this.value = value;
		}

		@Override
		public int compareTo(Comparison comparison) {
			return this.distance - comparison.distance;
		}

	}

	@Autowired
	private Util util;

	@Autowired
	private EditDistance ed;

	public int classify(String freemanCode, List<Entry> entries, int k) {
		List<Comparison> comparisons = new ArrayList<>();
		for (Entry tmp : entries)
			comparisons.add(new Comparison(ed.getDistance(freemanCode, tmp.getFreeman()), tmp.getValue()));
		Collections.sort(comparisons);
		if (k > comparisons.size())
			k = comparisons.size();
		int[] near = new int[k];
		for (int i = 0; i < k; i++)
			near[i] = comparisons.get(i).getValue();
		return this.util.findPopular(near);
	}

	public boolean isClassified(Entry entry, List<Entry> entries) {
		int value = classify(entry.getFreeman(), entries, 1);
		if (value == entry.getValue())
			return true;
		return false;
	}

}

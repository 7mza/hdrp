package ujm.wi.m1.algo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ujm.wi.m1.db.Entry;

@Component
public class Reduce {
	private static final Logger log = LogManager.getLogger(Reduce.class.getName());

	@Autowired
	private Knn knn;

	public List<Entry> cleanOutliers(List<Entry> entries) {
		log.info("cleanin outliers and bayesian error region");
		log.info("1st clean : found " + entries.size() + " entry");
		List<Entry> sub1 = new ArrayList<>(entries.subList(0, (int) entries.size() / 2));
		List<Entry> sub2 = new ArrayList<>(entries.subList((int) entries.size() / 2, (int) entries.size()));
		int cpt = cleanSub(sub1, sub2);
		cpt = cpt + cleanSub(sub2, sub1);
		sub1.addAll(sub2);
		log.info("1st clean : removed " + cpt + " outlier entry");
		return sub1;
	}

	public List<Entry> cleanIrrelevant(List<Entry> entries) {
		// TODO
		if (entries.size() < 2)
			return entries;
		log.info("Cleanin irrelevant examples");
		log.info("2nd clean : found " + entries.size() + " entry");
		List<Entry> storage = new ArrayList<>(), dustbin = new ArrayList<>();
		double randomIndex = Math.random() * entries.size();
		Entry randomEntry = entries.get((int) randomIndex);
		storage.add(randomEntry);
		// entries.remove((int) randomIndex);
		boolean stabilisation = false;
		while (!stabilisation) {
			for (Entry x : entries) {
				if (knn.isClassified(x, storage)) {
					dustbin.add(x);
					stabilisation = true;
				} else
					stabilisation = false;
				storage.add(x);
			}
			for (Entry x : dustbin)
				storage.remove(x);
		}
		log.info("2st clean : removed " + (entries.size() - storage.size()) + " irrelevants entry");
		return storage;
	}

	private int cleanSub(List<Entry> sub1, List<Entry> sub2) {
		int cpt = 0;
		Iterator<Entry> it = sub1.iterator();
		while (it.hasNext()) {
			if (!knn.isClassified(it.next(), sub2)) {
				it.remove();
				cpt++;
			}
		}
		return cpt;
	}

}

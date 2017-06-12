package ujm.wi.m1.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepo extends JpaRepository<Entry, Long> {

	public int countByTest(boolean test);

	public int countByValueAndTest(int value, boolean test);

	public List<Entry> findAllByTest(boolean test);

}

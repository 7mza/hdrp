package ujm.wi.m1.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestResultRepo extends JpaRepository<TestResult, Long> {

	public int countByValueAndAlgo(int value, TestResult.Algo algo);

	public int countByValueAndGuessAndAlgo(int value, int guess, TestResult.Algo algo);

}

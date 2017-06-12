package ujm.wi.m1.db;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString(exclude = "id")
public class TestResult implements Serializable {
	private static final long serialVersionUID = -3552070751893617369L;

	public enum Algo {
		knn3(3), knn5(5), knn7(7);

		@Getter
		private int id;

		private Algo(int id) {
			this.id = id;
		}

	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Getter
	private Long id;

	@Getter
	@Setter
	@Enumerated(EnumType.STRING)
	private Algo algo;

	@Getter
	@Setter
	private int value;

	@Getter
	@Setter
	private int guess;

	public TestResult(Algo algo, int value, int guess) {
		this.algo = algo;
		this.value = value;
		this.guess = guess;
	}

	public TestResult() {
	}

}

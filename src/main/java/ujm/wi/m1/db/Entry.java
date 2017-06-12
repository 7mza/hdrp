package ujm.wi.m1.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString(exclude = "id")
@EqualsAndHashCode(exclude = "id")
public class Entry implements Serializable {
	private static final long serialVersionUID = -7010007913119907916L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Getter
	private Long id;

	@Column(nullable = false)
	@Getter
	@Setter
	private int value;

	@Column(columnDefinition = "text", nullable = false)
	@Getter
	@Setter
	private String freeman;

	@Column(columnDefinition = "text", nullable = false)
	@Getter
	@Setter
	private String pixels;

	@Getter
	@Setter
	private boolean test;

	public Entry(int value, String freeman, String pixels, boolean test) {
		this.value = value;
		this.freeman = freeman;
		this.pixels = pixels;
		this.test = test;
	}

	public Entry() {
	}

}

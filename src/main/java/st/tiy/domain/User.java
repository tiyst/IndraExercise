package st.tiy.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "SUSERS")
@Getter
@Setter
@ToString
public class User {

	@Id
	@Column(name = "USER_ID")
	private long id;

	@Column(name = "USER_GUID")
	private String guid;

	@Column(name = "USER_NAME")
	private String username;

	public User(long id, String guid, String username) {
		this.id = id;
		this.guid = guid;
		this.username = username;
	}

	public User() {

	}

}

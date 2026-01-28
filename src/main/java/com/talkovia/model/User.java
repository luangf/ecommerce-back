package com.talkovia.model;

import com.talkovia.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
public class User implements UserDetails {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_seq",
            allocationSize = 50
    )
	@Column(name = "id")
    private Long id;

	@Column(name = "email", unique = true, nullable = false, length = 254)
	private String email;

	@Column(name = "username", unique = true, nullable = false, length = 30)
	private String username;

	@Column(name = "password", nullable = false, length = 60)
	private String password;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;

	@Column(name = "last_login_at")
	private Instant lastLoginAt;

	@Column(name = "active", nullable = false)
	private boolean active = true;
	
	@Column(name = "email_verified", nullable = false)
	private boolean emailVerified = false;
	
	// convert to S3 AWS imagePath; private String imagePath;
	@Lob
	@Column(name = "image")
    private byte[] image;

	@Enumerated(EnumType.STRING)
	private UserRole role = UserRole.USER;

    protected User() {}

	public User(String email, String username, String password){
		this.email=email;
		this.username=username;
		this.password=password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if(role == UserRole.ADMIN){
			return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
		}else{
			return List.of(new SimpleGrantedAuthority("ROLE_USER"));
		}
	}

//	@Override
//	public String getUsername(){
//		return email;
//	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}

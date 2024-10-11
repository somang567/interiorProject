package com.keduit.interiors.entity;
import com.keduit.interiors.constant.Role;
import com.keduit.interiors.dto.MemberDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "username")
	private String name;

	@Column(unique = true)
	private String email;

	private String password;

	private String address;

	@Enumerated(EnumType.STRING)
	private Role role;

	public static Member createMember(MemberDTO memberDTO, PasswordEncoder passwordEncoder) {
		Member member = new Member();
		member.setName(memberDTO.getUserName());
		member.setEmail(memberDTO.getEmail());
		member.setAddress(memberDTO.getAddress());
		String password = passwordEncoder.encode(memberDTO.getPassword());
		member.setPassword(password);
		member.setRole(Role.USER);

		return member;
	}
}

package com.keduit.interiors.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
	// Null Check , length = 0인 애들.
	@NotBlank(message = "이름은 필수입력입니다.")
	private String name;

	@NotEmpty(message = "이메일은 필수입력입니다.")
	@Email(message = "이메일 형식으로 입력해주세요.")
	private String email;

	@NotEmpty(message = "비밀번호는 필수입력입니다.")
	@Length(min = 8, max = 16, message = "비밀번호는 8자이상 16자이하로 입력해주세요.")
	private String password;

	@NotEmpty(message = "주소는 필수입력사항입니다.")
	private String address;

	private String role;
}

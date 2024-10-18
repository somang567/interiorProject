//package com.keduit.interiors.entity;
//
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//
//import javax.persistence.*;
//
//@Entity
//@Getter
//@Setter
//@ToString
//public class MegazineScrap {
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "scrap_id")
//	private Long id;
//
//	@ManyToOne
//	@JoinColumn(name = "member_id")
//	private Member member;
//
//	@ManyToOne
//	@JoinColumn(name = "megazine_id")
//	private Megazine megazine;
//}

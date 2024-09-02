package kr.co.ibk.domain.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepTreetInfo {
	private String realDepth;
	private String parId;
	private String deptId;
	private String deptCode;
	private String deptName;
	private String deptEngName;
	private String deptFullPath;
	private int deptDepth;
	private int deptOrder;
	private String deptStat;
	private String deptRegId;
	private String deptRegDt;
	private String deptRegTime;
	private String deptModId;
	private String deptModDt;
	private String deptModTime;
}



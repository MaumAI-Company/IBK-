package kr.co.ibk.domain.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MngMenuBreadcrumb  {
    private Long menuPid;
    private String menuNm;
    private String menuUrl;
    private Long prntMenuPid;

    private MngMenuBreadcrumb children;
}
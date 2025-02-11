package kr.co.ibk_monitoring.repository;

import kr.co.ibk_monitoring.domain.web.MemberInfo;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AdminMemberRepository {

    MemberInfo getSender();
    List<MemberInfo> getReceiverList();
	
}

package hongik.Todoing.domain.member.adaptor;

import hongik.Todoing.domain.member.domain.Member;
import hongik.Todoing.domain.member.exception.MemberNotFoundException;
import hongik.Todoing.domain.member.repository.MemberRepository;
import hongik.Todoing.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberAdaptor {

    private final MemberRepository memberRepository;

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> MemberNotFoundException.EXCEPTION);
    }
}

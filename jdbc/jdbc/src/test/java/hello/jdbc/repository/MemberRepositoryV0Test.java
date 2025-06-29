package hello.jdbc.repository;

import hello.domain.Member;
import hello.repository.MemberRepositoryV0;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 memberRepository =new MemberRepositoryV0();

    @Test
    void curd() throws SQLException {

        //Member save
        Member member = new Member("memberV1", 10000);
        memberRepository.save(member);

        //findById
        Member findMember = memberRepository.findById(member.getMemberId());
        log.info("findMember={}", findMember);
        Assertions.assertThat(findMember).isEqualTo(member);

        //update: money -> 20000
        memberRepository.update(member.getMemberId(), 20000);
        Member updatedMember = memberRepository.findById(member.getMemberId());
        Assertions.assertThat(updatedMember.getMoney()).isEqualTo(20000);

        //delete
        memberRepository.delete(member.getMemberId());
        Assertions.assertThatThrownBy(() -> memberRepository.findById(member.getMemberId()))
                .isInstanceOf(NoSuchElementException.class);
    }
}

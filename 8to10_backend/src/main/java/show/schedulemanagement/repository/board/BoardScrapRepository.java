package show.schedulemanagement.repository.board;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardScrap;
import show.schedulemanagement.domain.member.Member;

public interface BoardScrapRepository extends JpaRepository<BoardScrap, Long> {

    Optional<BoardScrap> findByMemberAndBoardId(Member member, Long boardId);

    @EntityGraph("board")
    @Query("select s from BoardScrap s where s.member = :member")
    List<BoardScrap> findAllByMemberWithBoard(Member member);

    @Modifying
    @Query("delete from BoardScrap s where s.board.id = :boardId")
    void deleteScrapByBoardId(@Param(value = "boardId") Long boardId);

    boolean existsByMemberAndBoardId(Member member, Long boardId);
}

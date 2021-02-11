package example.air.repositores;

import example.air.entity.Message;
import example.air.entity.Users;
import example.air.entity.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface MessageRepository extends CrudRepository<Message, Long> {
    @Query("select new example.air.entity.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "group by m")
    Page<MessageDto> findAll(Pageable pageable, Users user);

    @Query("select new example.air.entity.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :user then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.tag = :tag " +
            "group by m")
    Page<MessageDto> findByTag(String tag, Pageable pageable, Users user);

    @Query("select new example.air.entity.dto.MessageDto(" +
            "   m, " +
            "   count(ml), " +
            "   sum(case when ml = :author then 1 else 0 end) > 0" +
            ") " +
            "from Message m left join m.likes ml " +
            "where m.author = :author " +
            "group by m")
    Page<MessageDto> findByUser(Pageable pageable, Users author);
}
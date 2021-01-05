package top.gmfcj.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.gmfcj.anno.Read;
import top.gmfcj.anno.Write;
import top.gmfcj.bean.CountResult;

import java.util.List;

/**
 * 编写SQL语句

 @Query("SELECT O FROM UserDO O WHERE O.name = :name1  OR O.name = :name2 ")
 List<UserDO> findTwoName(@Param("name1") String name1, @Param("name2") String name2);

 @Query(nativeQuery = true, value = "SELECT * FROM AUTH_USER WHERE name = :name1  OR name = :name2 ")
 List<UserDO> findSQL(@Param("name1") String name1, @Param("name2") String name2);

 @Query("SELECT U.* FROM AUTH_USER U ,AUTH_ROLE_USER RU WHERE U.id = RU.user_id AND RU.role_id = :roleId")
 List<UserDO> findUsersByRole(@Param("roleId") Long roleId);

 */
@Repository
public interface IndexService extends JpaRepository<CountResult, Long> {

    @Write
    List<CountResult> findByWord(String word);

    // 多个字段一起声明
    @Read
    CountResult findByIdAndWord(Long id, String word);

    // 查询 total 大于多少的数据
    @Read
    List<CountResult> findAllByTotalGreaterThan(Integer total);

}

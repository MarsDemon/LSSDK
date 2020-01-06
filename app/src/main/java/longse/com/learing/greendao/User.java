package longse.com.learing.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;


/**
 * @Entity: 定义的表名
 * @Id: 定义了表的主键
 * @Property: 定义表中的行名称
 */
@Entity(nameInDb = "user")
public class User {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "name")
    private String name;

    @Property(nameInDb = "created_at")
    private String createAt;

    @Property(nameInDb = "updated_at")
    @NotNull
    private String updatedAt;
}

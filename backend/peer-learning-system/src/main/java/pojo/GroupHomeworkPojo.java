package pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * @author YXH_XianYu
 * @date 2023/10/7 22:42
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "groupHomework")
public class GroupHomeworkPojo {

    @TableId(value = "uuid", type = IdType.ASSIGN_ID)
    private String uuid;

    @TableField(value = "name")
    private String name;

    @TableField(value = "problemUUID")
    private String problemUUID;

    @TableField(value = "state")
    private Integer state;

    @TableField(value = "submitDeadline")
    private Date submitDeadline;

    @TableField(value = "ratingDeadline")
    private Date ratingDeadline;

}

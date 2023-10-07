package pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YXH_XianYu
 * @date 2023/10/7 22:56
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "rating")
public class RatingPojo {

    @TableId(value = "uuid", type = IdType.ASSIGN_UUID)
    private String uuid;

    @TableField(value = "homeworkUUID")
    private String homeworkUUID;

    @TableField(value = "userUUID")
    private String userUUID;

    @TableField(value = "score")
    private Float score;

}

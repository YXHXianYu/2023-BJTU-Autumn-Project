package com.yxhxianyu.peerlearningsystem.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

/**
 * Pojo类只储存信息，不做任何其他操作（如构造函数等）
 * 所有操作应该在DAO中完成
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

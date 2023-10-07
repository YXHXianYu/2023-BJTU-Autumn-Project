package com.yxhxianyu.peerlearningsystem.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pojo类只储存信息，不做任何其他操作（如构造函数等）
 * 所有操作应该在DAO中完成
 * @author YXH_XianYu
 * @date 2023/10/7 22:49
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "homework")
public class HomeworkPojo {

    @TableId(value = "uuid", type = IdType.ASSIGN_ID)
    private String uuid;

    @TableField(value = "groupHomeworkUUID")
    private String groupHomeworkUUID;

    @TableField(value = "userUUID")
    private String userUUID;

    @TableField(value = "answer")
    private String answer;

    @TableField(value = "isExcellentHomework")
    private Boolean isExcellentHomework;

    @TableField(value = "haveChecked")
    private Boolean haveChecked;

    @TableField(value = "checkedScore")
    private Float checkedScore;

}
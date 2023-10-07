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

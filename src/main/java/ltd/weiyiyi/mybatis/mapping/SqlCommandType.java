package ltd.weiyiyi.mybatis.mapping;

/**
 * @author Wei Han
 * @description sql command enum
 * @date 2024/2/17 12:08
 * @domain www.weiyiyi.ltd
 */
public enum SqlCommandType {

    /**
     * 未知
     */
    UNKNOWN,
    /**
     * 插入
     */
    INSERT,
    /**
     * 更新
     */
    UPDATE,
    /**
     * 删除
     */
    DELETE,
    /**
     * 查找
     */
    SELECT;

}

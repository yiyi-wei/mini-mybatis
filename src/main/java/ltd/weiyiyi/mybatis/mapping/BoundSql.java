package ltd.weiyiyi.mybatis.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Wei Han
 * @description xml node info in mapper.xml
 * @date 2024/2/19 13:59
 * @domain www.weiyiyi.ltd
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoundSql {

    /**
     * sql text
     */
    private String sql;

    /**
     * parameterType
     */
    private String parameterType;

    /**
     * parameters  <idx, param>
     */
    private Map<Integer, String> parameters;

    /**
     * resType class name
     */
    private String resultType;


}

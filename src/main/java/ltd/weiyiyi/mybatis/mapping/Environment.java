package ltd.weiyiyi.mybatis.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import ltd.weiyiyi.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;

/**
 * @author Wei Han
 * @description Provides an environment for sql running
 * @date 2024/2/19 14:07
 * @domain www.weiyiyi.ltd
 */
@Data
@AllArgsConstructor
public class Environment {

    /**
     * xml env id
     */
    private final String id;

    /**
     * transactionFactory
     */
    private final TransactionFactory transactionFactory;

    /**
     * db info such as username password
     */
    private final DataSource dataSource;

}

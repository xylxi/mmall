package com.mmall.dao;

import com.mmall.pojo.Cart;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 *     为什么不需要加 @Component 注解呢？因为是在 applicationContext-datasource.xml 设置了
 *     <bean name="mapperScannerConfigurer" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
 *         <property name="basePackage" value="com.mmall.dao"/>
 *     </bean>
 *     会生成 代理的bean，然后将其加入到 IOC 容器中
 */

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdProductIds(@Param("userId") Integer userId, @Param("productIdList") List<String> productIdList);

    int checkedOrUncheckedProduct(@Param("userId") Integer userId,
                                  @Param("checked") Integer checked,
                                  @Param("productId") Integer productId);

    int selectCarProductCount(Integer userId);

    List<Cart> selectCheckedCartByUserId(Integer userId);
}
/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.2.6
 * Version: 1.0
 */

package xyz.launcel.redis.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "jdbc.role")
public class JdbcRolePropertites
{
    @NonNull
    private String authenticationQuery;
    @NonNull
    private String userRoleQuery;
}

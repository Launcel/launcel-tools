package xyz.launcel.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.sql.DataSource;

@Getter
@Setter
public class RoleDataSourceHolder
{

    private DataSource dataSource;

}

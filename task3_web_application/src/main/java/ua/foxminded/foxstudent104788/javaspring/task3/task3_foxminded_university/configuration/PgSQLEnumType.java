package ua.foxminded.foxstudent104788.javaspring.task3.task3_foxminded_university.configuration;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.EnumType;
import org.springframework.orm.hibernate5.HibernateSystemException;

public class PgSQLEnumType extends EnumType<Enum> {

	public void nullSafeSet(PreparedStatement ps, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateSystemException, SQLException {

		ps.setObject(index, value != null ? ((Enum) value).name() : null, Types.OTHER);
	}

}
package com.github.fyfhust;

import com.github.fyfhust.model.Student;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.transactions.Transaction;

import javax.cache.Cache;
import java.util.LinkedList;
import java.util.List;

public class TransactionTest {
    public static void main(String[] args) {
        IgniteCache<String, Student> studentCache = IgniteSingleton.studentCache;

        try (Transaction tx = IgniteSingleton.ignite.transactions().txStart()) {
            Student stu = new Student();
            stu.setId("1");
            stu.setName("fuyf");

            studentCache.put(stu.getId(), stu);

            Student stu2 = new Student();
            stu2.setId("2");
            stu2.setName("liuhj");
            stu2.setAddress("shanghai");

            studentCache.put(stu2.getId(), stu2);

            SqlQuery sql = new SqlQuery(Student.class, "address is null");
            try (QueryCursor<Cache.Entry<Long, Student>> cursor = studentCache.query(sql)) {
                for (Cache.Entry<Long, Student> e : cursor)
                    System.out.println(e.getValue().toString());
            }

            SqlFieldsQuery sql2 = new SqlFieldsQuery(
                    "select * from Student where address is not null");

            try (QueryCursor<List<?>> cursor = studentCache.query(sql2)) {
                for (List<?> row : cursor)
                    System.out.println("student " + row.get(0) + row.get(1) + row.get(2) + row.get(3));
            }

            System.out.println("======================");
            List<List<?>> ret1 = selectSql(studentCache, "select * from Student where name = ?", "liuhj");
            for (List<?> row : ret1) {
                System.out.println(row.get(0) + ": " + row.get(1) + " at " + row.get(3));
            }


            tx.commit();
        }
    }

    static List<List<?>> selectSql(IgniteCache<String, Student> cache, String sql, Object... args) {
        SqlFieldsQuery query = new SqlFieldsQuery(sql);
        List<List<?>> ret = new LinkedList<>();
        try (QueryCursor<List<?>> cursor = cache.query(query.setArgs(args))) {
            for (List<?> row : cursor) {
                ret.add(row);
            }
        }

        return ret;
    }
}

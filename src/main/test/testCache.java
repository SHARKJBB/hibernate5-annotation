import org.hibernate.Session;
import org.junit.Test;
import tedu.sharkj.pojo.Student;
import tedu.sharkj.util.HibernateUtil;

import java.util.Iterator;
import java.util.List;

public class testCache {
    @Test
    public void test01() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            List<Student> students = session.createQuery
                    ("select stu from Student stu left join " +
                            "stu.classRoom cla where cla.id=:id")
                    .setParameter("id", 1)
                    .list();
            for (Student student : students) {
                System.out.println(student.getName() + "," + student.getSex() + "," + student.getClassRoom().getName());
            }
            System.out.println("-------------------------------------");
            //由于该生的信息已经在session中了，不会再发送sql语句了
            Student stu = (Student) session.load(Student.class, 50);
            System.out.println(stu.getName());
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            HibernateUtil.close(session);
        }
//sessiong关闭了，其中的数据全部清空，再次打开session时，需要重发sql
        try {
            session = HibernateUtil.openSession();
            //由于该生的信息已经在session中了，不会再发送sql语句了
            Student stu = (Student) session.load(Student.class, 50);
            System.out.println(stu.getName());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            HibernateUtil.close(session);
        }
    }

    @Test
    public void test02() {
        Session session = null;
        try {
            session = HibernateUtil.openSession();
            Iterator<Student> students = session.createQuery("select stu from Student stu")
                    .setFirstResult(0).setMaxResults(15)
                    .iterate();
            while (students.hasNext()) {
                Student student = students.next();
                System.out.println(student.getId());
//                System.out.println(student.getName() + "," + student.getSex());
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            HibernateUtil.close(session);
        }

    }
}

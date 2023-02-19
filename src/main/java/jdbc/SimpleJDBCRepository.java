package jdbc;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private Connection connection = null;
    private PreparedStatement ps = null;
    private Statement st = null;

    private static final String createUserSQL = "INSERT INTO myusers values(?, ?, ?, ?)";
    private static final String updateUserSQL = "update myusers set firstname=(?), lastname=(?), age=(?) where id=(?)";
    private static final String deleteUser = "delete from myusers where id=(?)";
    private static final String findUserByIdSQL = "SELECT * FROM myusers where id=(?)";
    private static final String findUserByNameSQL = "select * from myusers where firstname=(?)";
    private static final String findAllUserSQL = "SELECT * from myusers";

    public Long createUser() {
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(createUserSQL);
            ps.setLong(1, 1);
            ps.setString(2, "Bekzod");
            ps.setString(3, "Keldiyarov");
            ps.setInt(4, 22);
            ps.execute();
            return 1L;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findUserById(Long userId) {
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(findUserByIdSQL);
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return new User(rs.getLong(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getInt(4));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User findUserByName(String userName) {
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(findUserByNameSQL);
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return new User(rs.getLong(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getInt(4));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> findAllUser() {
        List<User> users = new ArrayList<>();
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(findAllUserSQL);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4)));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public User updateUser() {
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(updateUserSQL);
            ps.setString(1, "bek updated");
            ps.setString(2, "keldi updated");
            ps.setInt(3, 99);
            ps.setLong(4, 1);
            ps.executeQuery();
            return findUserById(1L);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser(Long userId) {
        try {
            connection = CustomDataSource.getInstance().getConnection();
            ps = connection.prepareStatement(deleteUser);
            ps.setLong(1, userId);
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

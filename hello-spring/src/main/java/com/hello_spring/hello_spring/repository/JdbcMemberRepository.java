package com.hello_spring.hello_spring.repository;

import com.hello_spring.hello_spring.domain.Member;

import org.springframework.jdbc.datasource.DataSourceUtils;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {

    // java에서 여러 종류의 데이터베이스 커넥션을 설정하고 관리하기 위한 인터페이스이며, Spring과 같은 프레임워크와 통합되어 트랜잭션 동기화를 지원한다.
    // DataSource는 DriverManager 대신 사용되며, 커넥션 풀링을 지원하여 데이터베이스 연결을 재사용하고 트랜잭션 관리를 지원하여 데이터 일관성과 무결성을 유지시킨다.
    private final DataSource dataSource;

    // 빈 구성 파일에서 DataSource을 구현한 빈을 의존성 자동 주입하도록 설정
    public JdbcMemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(name) values(?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // DB 연결 시도, 성공시 DB 연결 객체 반환한다. DB 연결 객체를 사용하여 SQL 쿼리를 실행 가능하다.
            conn = getConnection();

            // SQL 쿼리를 준비하는 과정
            // PreparedStatement: SQL 실행 객체
            // Statement.RETURN_GENERATED_KEYS: 데이터베이스가 자동으로 생성한 키를 반환하도록 설정
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // SQL 쿼리에 매개변수를 설정 (인덱스 1부터 시작 설정)
            pstmt.setString(1, member.getName());

            // 업데이트 쿼리 날림
            pstmt.executeUpdate();

            // SQL 삽입 작업 후에 데이터베이스가 자동으로 생성한 키 값을 가져오기 위한 코드
            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                member.setId(rs.getLong(1));
            } else {
                throw new SQLException("id 조회 실패");
            }
            return member;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            // 데이터베이스는 결국 파일 시스템을 사용하여 데이터를 영구 저장한다.
            // 파일을 열은 후에 닫지 않으면, 파일 디스크립터가 참조하는 자원이 메모리에서 해제되지 않아서 자원 누수(Resource Leak)가 발생할 수 있다.
            // 데이터베이스 서버에서 사용하는 메모리 자원 해제
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from member where id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public List<Member> findAll() {
        String sql = "select * from member";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            List<Member> members = new ArrayList<>();
            while(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                members.add(member);
            }
            return members;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    @Override
    public Optional<Member> findByName(String name) {
        String sql = "select * from member where name = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));
                return Optional.of(member);
            }
            return Optional.empty();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            close(conn, pstmt, rs);
        }
    }

    // 스프링 기반의 애플리케이션에서 DB에 연결시에는 DataSourceUtils를 통해서 커넥션을 한다. 이러면 동일한 커넥션에서 안전하게 트랙잭션이 수행된다.
    // 만일, 여러 커넥션을 생성하여 커넥션 마다 트랜잭션이 관리된다면, 각 트랜잭션에서 동일한 데이터에 접근시에 동시성 이슈가 발생할 수 있기 때문이다.
    private Connection getConnection() {
        // DB 접속 정보를 가지고 데이터베이스와 연결 시도 후 성공시 연결 객체 반환
        return DataSourceUtils.getConnection(dataSource);
    }

    // 애플리케이션의 데이터 처리에 필요했던 DB 서버의 리소스(파일 디스크립터가 참조하는 메모리 자원) 모두 반환
    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 스프링 프레임워크에서 DataSourceUtils를 통해서 DB와 커넥션한 소켓을 닫아줌
    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}

package com.hello_spring.hello_spring.repository;

import com.hello_spring.hello_spring.domain.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMemberRepository implements MemberRepository {

    // DataSource는 java에서 여러 종류의 데이터베이스 커넥션을 설정하고 관리하기 위한 인터페이스이며, Spring과 같은 프레임워크와 통합되어 커낵션 풀링과 트랜잭션 동기화를 지원한다.
    // DataSource는 DriverManager 대신 사용되며, 커넥션 풀링을 지원하여 데이터베이스 연결을 재사용하고 트랜잭션 관리를 지원하여 데이터 일관성과 무결성을 유지시킨다.
    // DataSource는 내부적으로 JDBC 드라이버를 사용하여 데이터베이스 연결을 관리한다. (DataSource는 데이터베이스와의 연결을 설정하고 관리하는 고수준 인터페이스로, 실제 데이터베이스와의 통신은 JDBC 드라이버를 통해 이루어잔다.)
    private final DataSource dataSource;

    // 빈 구성 파일에서 DataSource을 구현한 빈을 의존성 자동 주입하도록 설정 (생성자 매개변수 하나일때 @Autowired 생략 가능)
    public JdbcMemberRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Member save(Member member) {
        // SQL문
        // member 테이블에 삽입할 name 열(column)
        // 자리 표시자 ?은 name에 삽입할 파라매터 바인딩(parameter binding) 설정 -> 추후, 동적으로 member.getName() 바인딩
        String sql = "insert into member(name) values(?)";

        // Connection은 JDBC를 사용하여 Java와 DB 간의 연결을 관리를 위한 인터페이스이다.
        // Connection 구현체는 특정 데이터베이스와의 세션을 나타내며, SQL 문을 실행하고 트랜잭션을 관리하는데 사용된다.
        Connection conn = null;

        // SQL 문을 실행하기 위해 사용되는 객체로, 동적 값 설정 지원을 한다. (자리 표시자(?)에 실제 값을 안전하게 대입하여, SQL 인젝션 공격을 방지)
        PreparedStatement pstmt = null;

        // 쿼리 실행 결과를 저장하는 객체
        ResultSet rs = null;
        try {
            // DB 연결 성공시 DB 연결 객체 반환되어 저장한다. DB 연결 객체를 사용하여 SQL 쿼리를 실행 가능하다.
            conn = getConnection();

            // SQL 쿼리를 준비하는 과정
            // PreparedStatement: SQL문 실행을 위한 객체
            // Statement.RETURN_GENERATED_KEYS: 데이터베이스가 자동으로 생성한 키를 반환하도록 설정
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // SQL 쿼리에 매개변수를 설정 (인덱스 1부터 시작 설정, member.getName() 값을 파라매터 바인딩)
            pstmt.setString(1, member.getName());

            // 업데이트 쿼리 날림
            pstmt.executeUpdate();

            // SQL 삽입 작업 후에 데이터베이스가 자동으로 생성한 키 값(인덱스)을 가져오기 위한 코드
            rs = pstmt.getGeneratedKeys();

            // DB 서버으로부터의 반환 값이 있으면
            if (rs.next()) {
                // ResultSet 구현체에서 첫 번째 열(column)의 값을 읽어 들임
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

    // Connection 인터페이스의 구현체는 데이터베이스 공급자(vendor) 또는 커넥션 풀링 라이브러리에 의해 제공될수 있다.
    // 여기서는 Connection 인터페이스의 구현체는 스프링에서 디폴트로 지정된 HikariCP 커넥션 풀링 라이브러리에 의해서, DataSource 구현체에 포함된 접속 정보로 DB 종류와 일치하는 DB 커넥션 객체를 제공된다.
    // DataSource 구현체인 HikariCPDataSource가 의존성 주입된다.
    private Connection getConnection() {
        // application.properties에서 설정한 H2 DB 등의 접속정보를 포함한, DataSource 구현체를 가지고 데이터베이스와 연결 시도 후 성공시 연결 객체 반환한다.
        // 스프링 기반의 애플리케이션에서 DB에 연결시에는 DataSourceUtils를 통해서 커넥션을 한다. 이는 동일한 커넥션에서 안전하게 트랙잭션이 수행된다.
        // 만일, 여러 커넥션을 생성하여 커넥션 마다 트랜잭션이 관리된다면, 각 트랜잭션에서 동일한 데이터에 접근시에 동시성 이슈가 발생할 수 있기 때문이다.
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

    // 스프링 프레임워크에서 DataSourceUtils를 통해서 DB와 커넥션을 닫아 소켓이 해제된다.
    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, dataSource);
    }
}

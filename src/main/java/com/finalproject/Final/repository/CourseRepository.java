package com.finalproject.Final.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.CourseBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

@Repository
public class CourseRepository {

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private final CourseRowMapper mapper = new CourseRowMapper();

	// 🔹 GET ALL COURSES
	// student view
	public List<CourseBean> findAll() {

//    	String sql =
//    		    "SELECT c.*, " +
//    		    "sc.name AS subcategory_name, " +
//    		    "cc.name AS category_name, " +
//    		    "u.name AS teacher_name " +
//    		    "FROM course c " +
//    		    "JOIN subcategory sc ON c.subcategoryID = sc.subcategoryID " +
//    		    "JOIN course_category cc ON c.courseCategoryID = cc.courseCategoryID " +
//    		    "JOIN user u ON c.teacherID = u.userID " +
//    		    "ORDER BY c.created_at DESC";

		String sql = "SELECT c.*,\r\n" + "       sc.name AS subcategory_name,\r\n"
				+ "       cc.name AS category_name,\r\n" + "       u.name AS teacher_name\r\n" + "FROM course c\r\n"
				+ "JOIN subcategory sc ON c.subcategoryID = sc.subcategoryID\r\n"
				+ "JOIN course_category cc ON c.courseCategoryID = cc.courseCategoryID\r\n"
				+ "JOIN user u ON c.teacherID = u.userID\r\n" + "WHERE c.status = 'Open'\r\n"
				+ "AND c.is_active = 1\r\n" + "ORDER BY c.created_at DESC;";

		return jdbc.query(sql, mapper);
	}

	public List<CourseBean> adminViewActiveCourseList() {

	    String sql = """
	        SELECT c.*,
	               sc.name AS subcategory_name,
	               cc.name AS category_name,
	               u.name AS teacher_name
	        FROM course c
	        JOIN subcategory sc 
	            ON c.subcategoryID = sc.subcategoryID
	        JOIN course_category cc 
	            ON c.courseCategoryID = cc.courseCategoryID
	        JOIN user u 
	            ON c.teacherID = u.userID
	        WHERE c.is_active = 1
	        ORDER BY c.created_at DESC
	        """;


	    return jdbc.query(sql, mapper);
	}
	
	//admin
//	public List<CourseBean> findArchivedCourses() {
//
//	    String sql = """
//	        SELECT c.*,
//	               sc.name AS subcategory_name,
//	               cc.name AS category_name,
//	               u.name AS teacher_name
//	        FROM course c
//	        JOIN subcategory sc 
//	            ON c.subcategoryID = sc.subcategoryID
//	        JOIN course_category cc 
//	            ON c.courseCategoryID = cc.courseCategoryID
//	        JOIN user u 
//	            ON c.teacherID = u.userID
//	        WHERE c.is_active = 0
//	        ORDER BY c.updated_at DESC
//	        """;
//
//
//	    return jdbc.query(sql, mapper);
//	}

	// 🔹 GET BY ID
	public CourseBean findById(String courseId) {

		String sql = "SELECT c.*, " + "sc.name AS subcategory_name, " + "cc.name AS category_name, "
				+ "u.name AS teacher_name " + "FROM course c "
				+ "JOIN subcategory sc ON c.subcategoryID = sc.subcategoryID "
				+ "JOIN course_category cc ON c.courseCategoryID = cc.courseCategoryID "
				+ "JOIN user u ON c.teacherID = u.userID " + "WHERE c.courseID = ?";

		return jdbc.queryForObject(sql, mapper, courseId);
	}

	// 🔹 SAVE
	public void save(CourseBean c) {

		String sql = "INSERT INTO course (" + "courseID," + "courseCategoryID," + "subcategoryID," + "teacherID,"
				+ "createdByID," + "name," + "description," + "duration_weeks," + "fee," + "level," + "status,"
				+ "seats_total," + "seats_available," + "thumbnail_path," + "allow_installment," + "allow_scholarship,"
				+ "created_at," + "updated_at," + "is_active"
				+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), 1)";

		jdbc.update(sql,

				c.getCourseId(), c.getCourseCategoryId(), c.getSubcategoryId(), c.getTeacherId(), c.getCreatedBy(),

				c.getName(), c.getDescription(), c.getDurationWeeks(),

				c.getFee(),

				c.getLevel(), c.getStatus(),

				c.getSeatsTotal(), c.getSeatsTotal(),

				c.getThumbnailPath(),

				c.getAllowedInstallment(), c.getAllowedScholarship()

		);
	}

	// 🔹 UPDATE
	public void update(CourseBean c) {

		String sql = "UPDATE course SET " + "courseCategoryID=?," + "subcategoryID=?," + "teacherID=?,"
				+ "createdByID=?," + "name=?," + "description=?," + "duration_weeks=?," + "fee=?," + "level=?,"
				+ "status=?," + "seats_total=?," + "seats_available=?," + "thumbnail_path=?," + "allow_installment=?,"
				+ "allow_scholarship=?," + "updated_at=NOW() " + "WHERE courseID=?";

		// test
		System.out.println("===== REPOSITORY =====");
		System.out.println(c.getCreatedBy());
		System.out.println(c.getSeatsAvailable());

		jdbc.update(sql,

				c.getCourseCategoryId(), c.getSubcategoryId(), c.getTeacherId(), c.getCreatedBy(),

				c.getName(), c.getDescription(), c.getDurationWeeks(),

				c.getFee(),

				c.getLevel(), c.getStatus(),

				c.getSeatsTotal(), c.getSeatsAvailable(),

				c.getThumbnailPath(),

				c.getAllowedInstallment(), c.getAllowedScholarship(),

				c.getCourseId());
	}

	public int getSeatsAvailable(String courseId) {

		String sql = "SELECT seats_available FROM course WHERE courseID = ?";

		return jdbc.queryForObject(sql, Integer.class, courseId);
	}

	public void decreaseSeat(String courseId) {

		String sql = "UPDATE course " + "SET seats_available = seats_available - 1 " + "WHERE courseID = ? "
				+ "AND seats_available > 0";

		jdbc.update(sql, courseId);
	}

	// 🔹 DELETE
	// must be soft delete
	// set is_active = 0
//	public void delete(String courseId) {
//
//		jdbc.update("DELETE FROM course WHERE courseID = ?", courseId);
//	}

	public List<CourseBean> getCoursesByIds(List<String> ids) {

		if (ids == null || ids.isEmpty()) {
			return new ArrayList<>();
		}

		String sql = "SELECT * " + "FROM course " + "WHERE courseID IN (:ids)";

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("ids", ids);

		return namedParameterJdbcTemplate.query(sql, params, mapper);
	}

	public List<CourseBean> findByCategory(String categoryId) {

		String sql = "SELECT c.*, " + "sc.name AS subcategory_name, " + "cc.name AS category_name, "
				+ "u.name AS teacher_name " + "FROM course c "
				+ "JOIN subcategory sc ON c.subcategoryID=sc.subcategoryID "
				+ "JOIN course_category cc ON c.courseCategoryID=cc.courseCategoryID "
				+ "JOIN user u ON c.teacherID=u.userID " + "WHERE c.courseCategoryID=?";

		return jdbc.query(sql, mapper, categoryId);

	}

	// new
	public int countAllCourses() {

		String sql = "SELECT COUNT(*) FROM course WHERE is_active = 1";

		return jdbc.queryForObject(sql, Integer.class);

	}

	public int countByStatus(String status) {

		String sql = """
				SELECT COUNT(*)
				FROM course
				WHERE status=?
				""";

		return jdbc.queryForObject(sql, Integer.class, status);

	}

	//soft delete
	public void delete(String courseId, String adminId) {

		String sql = """
		        UPDATE course
		        SET 
		            is_active = 0,
		            deleted_at = NOW(),
		            deletedByID = ?
		        WHERE courseID = ?
		        """;

		jdbc.update(sql, adminId, courseId);
	}

	public void restore(String courseId){

	    String sql = """
	        UPDATE course
	        SET 
	            is_active = 1,
	            deleted_at = NULL,
	            deletedByID = NULL
	        WHERE courseID = ?
	        """;


	    jdbc.update(sql, courseId);

	}
	
	public int countArchivedThisMonth(){

	    String sql = """
	        SELECT COUNT(*)
	        FROM course
	        WHERE is_active = 0
	        AND MONTH(deleted_at) = MONTH(CURRENT_DATE())
	        AND YEAR(deleted_at) = YEAR(CURRENT_DATE())
	        """;


	    return jdbc.queryForObject(
	        sql,
	        Integer.class
	    );

	}
	
	
	public List<CourseBean> archivedCourseList(){

	    String sql = """
	        SELECT c.*,
	               sc.name AS subcategory_name,
	               cc.name AS category_name,
	               u.name AS teacher_name
	        FROM course c
	        JOIN subcategory sc 
	        ON c.subcategoryID=sc.subcategoryID

	        JOIN course_category cc
	        ON c.courseCategoryID=cc.courseCategoryID

	        JOIN user u
	        ON c.teacherID=u.userID

	        WHERE c.is_active = 0

	        ORDER BY c.deleted_at DESC
	        """;


	    return jdbc.query(sql, mapper);

	}
	
	public int countArchivedCourses(){

	    String sql = """
	        SELECT COUNT(*)
	        FROM course
	        WHERE is_active = 0
	        """;


	    return jdbc.queryForObject(
	            sql,
	            Integer.class
	    );
	}
	
	
//	public List<CourseBean> searchAdminCourses(String keyword) {
//
//	    String sql = """
//	        SELECT c.*,
//	               sc.name AS subcategory_name,
//	               cc.name AS category_name,
//	               u.name AS teacher_name
//	        FROM course c
//	        JOIN subcategory sc 
//	            ON c.subcategoryID = sc.subcategoryID
//	        JOIN course_category cc 
//	            ON c.courseCategoryID = cc.courseCategoryID
//	        JOIN user u 
//	            ON c.teacherID = u.userID
//	        WHERE c.is_active = 1
//	        AND c.name LIKE ?
//	        ORDER BY c.created_at DESC
//	        """;
//
//
//	    return jdbc.query(
//	            sql,
//	            mapper,
//	            "%" + keyword + "%"
//	    );
//	}
	
	
	
	public List<CourseBean> searchAndFilterCourses(
	        String keyword,
	        String status) {


	    String sql = """
	        SELECT c.*,
	               sc.name AS subcategory_name,
	               cc.name AS category_name,
	               u.name AS teacher_name
	        FROM course c
	        JOIN subcategory sc
	            ON c.subcategoryID = sc.subcategoryID
	        JOIN course_category cc
	            ON c.courseCategoryID = cc.courseCategoryID
	        JOIN user u
	            ON c.teacherID = u.userID
	        WHERE c.is_active = 1
	        """;


	    List<Object> params = new ArrayList<>();



	    if(keyword != null && !keyword.isBlank()) {

	        sql += """
	            AND c.name LIKE ?
	            """;

	        params.add("%" + keyword + "%");

	    }



	    if(status != null && !status.isBlank()) {

	        sql += """
	            AND c.status = ?
	            """;

	        params.add(status);

	    }



	    sql += """
	        ORDER BY c.created_at DESC
	        """;


	    return jdbc.query(
	            sql,
	            mapper,
	            params.toArray()
	    );

	}
	
	
	public List<CourseBean> searchAndFilterArchivedCourses(
	        String keyword,
	        String status) {


	    String sql = """
	        SELECT c.*,
	               sc.name AS subcategory_name,
	               cc.name AS category_name,
	               u.name AS teacher_name
	        FROM course c

	        JOIN subcategory sc
	            ON c.subcategoryID = sc.subcategoryID

	        JOIN course_category cc
	            ON c.courseCategoryID = cc.courseCategoryID

	        JOIN user u
	            ON c.teacherID = u.userID

	        WHERE c.is_active = 0
	        """;


	    List<Object> params = new ArrayList<>();



	    if(keyword != null && !keyword.isBlank()) {


	        sql += """
	            AND c.name LIKE ?
	            """;


	        params.add(
	            "%" + keyword + "%"
	        );

	    }



	    if(status != null && !status.isBlank()) {


	        sql += """
	            AND c.status = ?
	            """;


	        params.add(status);

	    }



	    sql += """
	        ORDER BY c.deleted_at DESC
	        """;



	    return jdbc.query(
	            sql,
	            mapper,
	            params.toArray()
	    );

	}
	
	//student course search
	public List<CourseBean> searchStudentCourses(String keyword) {

	    String sql = """
	        SELECT c.*,
	               sc.name AS subcategory_name,
	               cc.name AS category_name,
	               u.name AS teacher_name
	        FROM course c
	        JOIN subcategory sc 
	            ON c.subcategoryID = sc.subcategoryID
	        JOIN course_category cc 
	            ON c.courseCategoryID = cc.courseCategoryID
	        JOIN user u 
	            ON c.teacherID = u.userID
	        WHERE c.is_active = 1
	        AND c.status = 'Open'
	        AND c.name LIKE ?
	        ORDER BY c.created_at DESC
	        """;


	    return jdbc.query(
	            sql,
	            mapper,
	            "%" + keyword + "%"
	    );
	}
}
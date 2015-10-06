package com.bko.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bko.domain.PatchTask;



public class PatchTaskDaoImpl implements PatchTaskDao{
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate jdbcTemplate2;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate2 = new NamedParameterJdbcTemplate(dataSource);
	}

	public List<PatchTask> getPatchId(String taskId) {
		try {
		      
		      MapSqlParameterSource params = new MapSqlParameterSource();
		      params.addValue("taskId", taskId);
		      
		      
		      //String sql = "SELECT REFPAT FROM YSW11 WHERE SYNDPR  = (SELECT SYNDPR FROM YSW10 WHERE NAMLOT = :NAMLOT)";
		      String sql = "select refpat,syntsk from ysw07 where syntsk in (select syntsk from ysw01 where syntsk=:taskId)";

		      List<PatchTask> patchTaskList = jdbcTemplate2.query(sql,  params, new PatchTaskListRowMapper());
		      
		      return patchTaskList;
		    } catch ( DataAccessException exc ) {
		      //logger.error("FAILED to get PatchList List", exc);
		    	System.out.println("FAILED to get patch, task list "+ exc);
		      return new ArrayList<PatchTask>();
		    }
	}
	
	public class PatchTaskListRowMapper implements RowMapper {

		public PatchTask mapRow(ResultSet rs, int rowNum) throws SQLException {
			// I use JDK 5 so I do not have to wrap int with an Integer object
			PatchTask patchTask = new PatchTask();
			patchTask.setPatchId(rs.getString("REFPAT"));
			//patchTask.setPatchId(rs.getString("SYNTSK"));

			return patchTask;
		}
	}
}

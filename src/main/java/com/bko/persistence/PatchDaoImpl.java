package com.bko.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bko.domain.Patch;
import com.bko.domain.PatchMember;
import com.bko.domain.TransferOperation;


public class PatchDaoImpl implements PatchDao{
	
	private final Logger logger = Logger.getLogger(PatchDaoImpl.class);
	//private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}


	@SuppressWarnings("unchecked")
	public List<PatchMember> getPatchMembers( String REFMAI ) {
	    try {
	      
	      MapSqlParameterSource params = new MapSqlParameterSource();
	      params.addValue("REFMAI", REFMAI);
	      
	      
	      //String sql = "SELECT REFPAT FROM YSW11 WHERE SYNDPR  = (SELECT SYNDPR FROM YSW10 WHERE NAMLOT = :NAMLOT)";
	      //String sql = "SELECT * FROM YPD02_SYN WHERE REFPAT = :REFPAT";
	      String sql = "SELECT * FROM YED02 WHERE REFMAI = :REFMAI";
          logger.info("getPatchMember"  + sql);
	      List<PatchMember> patchMembersList = jdbcTemplate.query(sql,  params, new PatchMembersRowMapper());
	      
	      return patchMembersList;
	    } catch ( DataAccessException exc ) {
	      logger.error("FAILED to get PatchList List", exc);
	      return new ArrayList<PatchMember>();
	    }
	  }
	public List<Patch> getPatchDescription( String refpat ) {
	    try {
	    	
	    	logger.debug( "REFPAT: " + refpat);
	      
	        MapSqlParameterSource params = new MapSqlParameterSource();
	        params.addValue("REFPAT", refpat);
	      
	      
	        String sql = "SELECT * FROM YSW06 WHERE REFPAT = :REFPAT";
	        //String sql = "select a.refpat, a.stapat, a.nomgrp, a.typevl, b.sujpat, trim(a.ittpat) from ysw06 a, ypd01_syn b where a.refpat = b.refpat and a.refpat = :REFPAT";
	        logger.debug("SQL:" +  sql );

	        List<Patch> patches = jdbcTemplate.query(sql,  params, new PatchDescriptionRowMapper());
	        
	        return patches;
	    } catch ( DataAccessException exc ) {
	        logger.error("FAILED to get PatchList List", exc);
	        return new ArrayList<Patch>();
	    }
	  }
	
	public List<TransferOperation> getTransferOperation(String REFMAI) {
		
		 try {
		MapSqlParameterSource params = new MapSqlParameterSource();
	      params.addValue("REFMAI", REFMAI);
		//String query1 = "SELECT * FROM YPD23_SYN WHERE REFMAI =:REFMAI";
	      String query1 = "SELECT * FROM YED23 WHERE REFMAI =:REFMAI";
		/**
		 * Implement the RowMapper callback interface
		 */
		//return (List)jdbcTemplate.queryForObject(query1, new Cwd81RowMapper());
		List<TransferOperation> transferOperationList = jdbcTemplate.query(query1, params,new TransferOperationsRowMapper());
		return transferOperationList;
		 } catch ( DataAccessException exc ) {
		      //logger.error("FAILED to get PatchList List", exc);
		    	System.out.println("FAILED to get Transfer operations List "+ exc);
		      return new ArrayList<TransferOperation>();
		}
	}
	
	/**
	* rowmapper is used by Spring to read a line from a database table 
	* and to fill an instance of the class with the values
	*/
	public class PatchMembersRowMapper implements RowMapper {

		public PatchMember mapRow(ResultSet rs, int rowNum) throws SQLException {
			// I use JDK 5 so I do not have to wrap int with an Integer object
			PatchMember patchMember = new PatchMember();
			patchMember.setPatchId(rs.getString("REFMAI"));
			patchMember.setPatchMember(rs.getString("NOMMBR"));
			patchMember.setMemberType(rs.getString("TYPMBR"));
			patchMember.setTypAct(rs.getString("TYPACT"));
			return patchMember;
		}
	}
	public class TransferOperationsRowMapper implements RowMapper {

		public TransferOperation mapRow(ResultSet rs, int rowNum) throws SQLException {
			// I use JDK 5 so I do not have to wrap int with an Integer object
			TransferOperation transferOperation = new TransferOperation();
			transferOperation.setIttCmd(rs.getString("ITTCMD"));
			transferOperation.setPatchRef(rs.getString("REFMAI"));
			//transferOperation.setBypass(rs.getString("BYPASS"));
			//transferOperation.setSwiChk(rs.getString("SWICHK"));
			//transferOperation.setSwiMan(rs.getString("SWIMAN"));
			//transferOperation.setTypTft(rs.getString("TYPTFT"));
			return transferOperation;
		}
	}
	public class PatchDescriptionRowMapper implements RowMapper {

		public Patch mapRow(ResultSet rs, int rowNum) throws SQLException {
			// I use JDK 5 so I do not have to wrap int with an Integer object
			//System.out.println("PatchListRowMapper");
			Patch patch = new Patch();
			patch.setPatchId(rs.getString("REFPAT"));
			patch.setNomGrp(rs.getString("NOMGRP"));
			//patch.setVerPat(rs.getString("VERPAT"));
			//patch.setSujPat(rs.getString("SUJPAT"));
			patch.setSynopsis(rs.getString("ITTPAT"));
			patch.setTypEvl(rs.getString("TYPEVL"));
			patch.setStatus(rs.getString("STAPAT"));
			//patch.setTy ;
			//System.out.println("RESULT: " + rs.getString("REFPAT"));
			return patch;
		}
	}

	public Patch getPatch(String patchId) {
		// TODO Auto-generated method stub
		return null;
	}

}

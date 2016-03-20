package com.bko.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.bko.domain.DeploymentRequestTransferOperation;
import com.bko.domain.Patch;
import com.bko.domain.PatchMember;
import com.bko.domain.DeploymentRequestTransferOperation;

public class DeploymentRequestDaoImpl implements DeploymentRequestDao {

	private final Logger logger = Logger
			.getLogger(DeploymentRequestDaoImpl.class);
	// private JdbcTemplate jdbcTemplate;
	// private BeanPropertySqlParameterSource namedParameters;
	private NamedParameterJdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		// this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@SuppressWarnings("unchecked")
	public List<Patch> getPatchList(String NAMLOT) {
		try {

			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("NAMLOT", NAMLOT);

			//String sql = "SELECT REFPAT FROM YSW11 WHERE SYNDPR  = (SELECT SYNDPR FROM YSW10 WHERE NOMDPR = :NAMLOT)";
			String sql = "SELECT refmai FROM yfd06 WHERE reflot  = (select reflot from yfd05 where namlot =:NAMLOT)";

			logger.info("SQL getPatchList: " + ":" + NAMLOT + ":" + sql);

			List<Patch> patches = jdbcTemplate.query(sql, params,
					new PatchListRowMapper());
			return patches;

		} catch (DataAccessException exc) {
			logger.error("FAILED to get PatchList List", exc);
			return new ArrayList<Patch>();
		}
	}

	public List<PatchMember> getDRMembers(String drName) {

		logger.info("getDRMembers");

		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("NOMDPR", drName);
			StringBuilder builder = new StringBuilder();
			//builder.append(" select refpat, nommbr, typmbr, typact from ypd02_syn where refpat in ( ");
			builder.append(" select refmai, nommbr, typmbr, typact from yed02 where refmai in ( ");
			builder.append(" SELECT refmai FROM yfd06 WHERE reflot  = (select reflot from yfd05 where namlot =:NOMDPR))");
			//builder.append(" select syndpr from ysw10 where nomdpr =:NOMDPR ) )");
			builder.append(" order by refmai ");

			String query = builder.toString();
			logger.info("SQL get DRmembers:" + query);

			List<PatchMember> drMembers = jdbcTemplate.query(query, params,
					new PatchMembersListRowMapper());

			return drMembers;
		} catch (DataAccessException exc) {
			logger.error("FAILED to get DR members " + exc);
			return new ArrayList<PatchMember>();
		}
	}

	public int getNumberOfPatches(String deploymentRequestName) {
		logger.info("getNumberOfPatches");

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("NAMLOT", deploymentRequestName);
		//String query = "select count(*) FROM YSW11 WHERE SYNDPR  = (SELECT SYNDPR FROM YSW10 WHERE NOMDPR = :NAMLOT)";
		String query = "select count(*) FROM yfd06 WHERE reflot  = (select reflot from yfd05 where namlot =:NAMLOT)";

		// return jdbcTemplate.queryForInt(query, params); --> deprecated so use
		// queryForObject
		int numberOfPatches= jdbcTemplate.queryForObject(query, params, Integer.class);
		logger.info("No. of patches:" + numberOfPatches);
		return numberOfPatches;
	}

	public List<DeploymentRequestTransferOperation> getTransferOperation(String NAMLOT) {

		logger.info("getTransferOperation");
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("NAMLOT", NAMLOT);
			String query = "select * from yfd07 where reflot = (select reflot from yfd05 where namlot =:NAMLOT)";

			List<DeploymentRequestTransferOperation> transferOperationList = jdbcTemplate.query(
					query, params, new TransferOperationsRowMapper());
			return transferOperationList;
		} catch (DataAccessException exc) {
			logger.error("FAILED to get transfer op. List " + exc);
			return new ArrayList<DeploymentRequestTransferOperation>();
		}
	}

	public class TransferOperationsRowMapper implements RowMapper {

		public DeploymentRequestTransferOperation mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			// I use JDK 5 so I do not have to wrap int with an Integer object
			DeploymentRequestTransferOperation transferOperation = new DeploymentRequestTransferOperation();
			transferOperation.setIttcmd(rs.getString("ITTCMD"));
			transferOperation.setRefmai(rs.getString("REFMAI"));
			transferOperation.setBypass(rs.getString("BYPASS"));
			transferOperation.setSwichk(rs.getString("SWICHK"));
			transferOperation.setSwiman(rs.getString("SWIMAN"));
			transferOperation.setTyptft(rs.getString("TYPTFT"));
			transferOperation.setStpall(rs.getString("STPALL"));
			transferOperation.setReflot(rs.getString("REFLOT"));
			transferOperation.setOrdopn(rs.getString("ORDOPN"));
			transferOperation.setNumstp(rs.getString("NUMSTP"));
			transferOperation.setTypopn(rs.getString("TYPOPN"));
			transferOperation.setVernum(rs.getString("VERNUM"));
			transferOperation.setSwimlt(rs.getString("SWIMLT"));
			transferOperation.setNomgrp(rs.getString("NOMGRP"));
			//transferOperation.setIdtent(rs.getString("IDTENT"));
			transferOperation.setIttcmd(rs.getString("ITTCMD"));
			return transferOperation;
		}
	}

	public class PatchCompleteListRowMapper implements RowMapper<Patch> {

		public Patch mapRow(ResultSet rs, int rowNum) throws SQLException {
			// I use JDK 5 so I do not have to wrap int with an Integer object
			// System.out.println("PatchListRowMapper");
			Patch patch = new Patch();
			patch.setPatchId(rs.getString("REFPAT"));
			patch.setNomGrp(rs.getString("NOMGRP"));
			patch.setVerPat(rs.getString("VERPAT"));
			patch.setSujPat(rs.getString("SUJPAT"));
			// System.out.println("RESULT: " + rs.getString("REFPAT"));
			return patch;
		}
	}

	/**
	 * rowmapper is used by Spring to read a line from a database table and to
	 * fill an instance of the class with the values
	 */
	public class PatchListRowMapper implements RowMapper {

		public Patch mapRow(ResultSet rs, int rowNum) throws SQLException {
			// I use JDK 5 so I do not have to wrap int with an Integer object
			// System.out.println("PatchListRowMapper");
			Patch patch = new Patch();
			patch.setPatchId(rs.getString("REFMAI"));
			// System.out.println("RESULT: " + rs.getString("REFPAT"));
			return patch;
		}
	}

	public class PatchMembersListRowMapper implements RowMapper {

		public PatchMember mapRow(ResultSet rs, int rowNum) throws SQLException {
			// I use JDK 5 so I do not have to wrap int with an Integer object
			PatchMember patchMember = new PatchMember();
			//patchMember.setPatchId(rs.getString("REFPAT"));
			patchMember.setPatchId(rs.getString("REFMAI"));
			patchMember.setPatchMember(rs.getString("NOMMBR"));
			patchMember.setMemberType(rs.getString("TYPMBR"));
			patchMember.setTypAct(rs.getString("TYPACT"));
			return patchMember;
		}
	}

	@Override
	public int getnumberOfTransferOperations(String deploymentRequestName) {
		logger.info("getnumberOfTransferOperations");

		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("NAMLOT", deploymentRequestName);
		String query = "select count(*) from yfd07 where reflot = (select reflot from yfd05 where namlot =:NAMLOT)";

		// return jdbcTemplate.queryForInt(query, params); --> deprecated so use
		// queryForObject
		int numberOfTransferOp= jdbcTemplate.queryForObject(query, params, Integer.class);
		logger.info("No. of transfer operations:" + numberOfTransferOp);
		return numberOfTransferOp;
	}
	public int getNumberOfManualTransferOperations(String deploymentRequestName){
		logger.info("getNumberOfManualTransferOperations");
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("NAMLOT", deploymentRequestName);
		String query = "select count(*) from yfd07 where reflot = (select reflot from yfd05 where namlot =:NAMLOT) and swiman = 'Y'";
		int numberOfManualTransferOp= jdbcTemplate.queryForObject(query, params, Integer.class);
		logger.info("No. of transfer operations:" + numberOfManualTransferOp);
		return numberOfManualTransferOp;
		
	}
	public int getNumberOfSubjects(String deploymentRequestName){
		logger.info("getNumberOfSubjects");
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("NAMLOT", deploymentRequestName);
		//String query = "select count(distinct sujpat) from ypd01_syn where refpat in (select refpat from ysw11 where syndpr = (select syndpr from ysw10 where nomdpr=:NAMLOT))";
		String query = "select count(distinct sujpat) from ysw12 where refpat in (select refpat from ysw11 where syndpr = (select syndpr from ysw10 where nomdpr=:NAMLOT))";
		try {
		  int numberOfSubjects= jdbcTemplate.queryForObject(query, params, Integer.class);
		  logger.info("No. of numberOfSubjects:" + numberOfSubjects);
		  return numberOfSubjects;
		} catch (EmptyResultDataAccessException e) {
			return -1; // TODO until a good way return an error/warning is devised 
		}
	}
	public String getEnvDst(String deploymentRequestName){
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("NAMLOT", deploymentRequestName);
		String query = "select envdst from ysw10 where nomdpr=:NAMLOT";
		try {
		   String envDst= jdbcTemplate.queryForObject(query, params, String.class);
		   logger.info("DR destination environment:" + envDst);
		   return envDst;
		} catch (EmptyResultDataAccessException e) {
			return "WARNING: Empty enventory table";
		}
	}
	public String getEnvSrc(String deploymentRequestName){
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("NAMLOT", deploymentRequestName);
		String query = "select envsrc from ysw10 where nomdpr=:NAMLOT";
		try {
		   String envSrc= jdbcTemplate.queryForObject(query, params, String.class);
		   logger.info("DR source environment:" + envSrc);
		   return envSrc;
		} catch (EmptyResultDataAccessException e) {
			return "WARNING: Empty enventory table";
		}
	}
	public String getSynopsis(String deploymentRequestName){
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("NAMLOT", deploymentRequestName);
		String query = "select ittdpr from ysw10 where nomdpr=:NAMLOT";
		try{
		    String ittdpr= jdbcTemplate.queryForObject(query, params, String.class);
		
		    logger.info("DR synopsis:" + ittdpr);
		    return ittdpr;
		} catch (EmptyResultDataAccessException e) {
			return "WARNING: Empty enventory table";
		}	
	}

}

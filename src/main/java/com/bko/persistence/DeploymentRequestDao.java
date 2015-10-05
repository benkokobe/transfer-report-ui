package com.bko.persistence;

import java.util.List;

import com.bko.domain.DeploymentRequestTransferOperation;
import com.bko.domain.Patch;
import com.bko.domain.PatchMember;



public interface DeploymentRequestDao {
	public int getNumberOfPatches(String deploymentRequestName);
	public int getnumberOfTransferOperations(String deploymentRequestName);
	public int getNumberOfManualTransferOperations(String deploymentRequestName);
	public int getNumberOfSubjects(String deploymentRequestName);
	/* public List<Patch> getPatchList(String deploymentRequest);*/
	public List<Patch> getPatchList(String deploymentRequestName);
	/*public List<Patch> getPatchListComplete( String NAMLOT ); */
	public List<PatchMember> getDRMembers(String deploymentRequestName);
	public List<DeploymentRequestTransferOperation> getTransferOperation(String deploymentRequestName);
	public String getEnvDst(String deploymentRequestName);
	public String getEnvSrc(String deploymentRequestName);
	public String getSynopsis(String deploymentRequestName);

}

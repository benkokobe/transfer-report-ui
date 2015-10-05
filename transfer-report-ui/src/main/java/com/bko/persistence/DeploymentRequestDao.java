package com.bko.persistence;

import java.util.List;

import com.bko.domain.Patch;
import com.bko.domain.PatchMember;
import com.bko.domain.TransferOperation;



public interface DeploymentRequestDao {
	public int getNumberOfPatches(String deploymentRequestName);
	public int getnumberOfTransferOperations(String deploymentRequestName);
	/* public List<Patch> getPatchList(String deploymentRequest);*/
	public List<Patch> getPatchList(String deploymentRequestName);
	/*public List<Patch> getPatchListComplete( String NAMLOT ); */
	public List<PatchMember> getDRMembers(String deploymentRequestName);
	public List<TransferOperation> getTransferOperation(String deploymentRequestName);

}
